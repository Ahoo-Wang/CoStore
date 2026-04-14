package me.ahoo.costore.s3

import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DefaultDeleteObjectRequest
import me.ahoo.costore.core.model.DefaultGetObjectRequest
import me.ahoo.costore.core.model.DefaultHeadObjectRequest
import me.ahoo.costore.core.model.DefaultListObjectsRequest
import me.ahoo.costore.core.model.DefaultPutObjectRequest
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PresignRequest
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI
import java.time.Duration

@EnabledIfEnvironmentVariable(named = "OSS_ACCESS_KEY_ID", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_SECRET_ACCESS_KEY", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_ENDPOINT", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_BUCKET", matches = ".+")
class S3ObjectStoreIT {
    private lateinit var store: S3ObjectStore
    private lateinit var bucket: BucketName

    @BeforeEach
    fun setup() {
        var endpoint = "https://s3.oss-cn-shanghai.aliyuncs.com"
        val accessKey = System.getenv("OSS_ACCESS_KEY_ID")!!
        val secretKey = System.getenv("OSS_SECRET_ACCESS_KEY")!!
        bucket = System.getenv("OSS_BUCKET") as BucketName
        val s3Configuration = S3Configuration.builder()
            .pathStyleAccessEnabled(false)
            .chunkedEncodingEnabled(false)
            .build()
        val awsCredentials = AwsBasicCredentials.create(accessKey, secretKey)
        val awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials)
        val endpointUri = URI(endpoint)

        val client = S3Client.builder()
            .endpointOverride(endpointUri)
            .region(Region.US_EAST_1)
            .credentialsProvider(awsCredentialsProvider)
            .serviceConfiguration(s3Configuration)
            .build()

        val presigner = S3Presigner.builder()
            .endpointOverride(endpointUri)
            .region(Region.US_EAST_1)
            .credentialsProvider(awsCredentialsProvider)
            .serviceConfiguration(s3Configuration)
            .build()

        store = S3ObjectStore(client, presigner)
    }

    @AfterEach
    fun cleanup() {
        store.close()
    }

    private fun generateKey(prefix: String): ObjectKey {
        return "costore/test/s3-$prefix-${System.currentTimeMillis()}" as ObjectKey
    }

    @Test
    fun `should head object`() {
        val key = generateKey("head")
        try {
            store.putObject(
                DefaultPutObjectRequest(
                    bucket = bucket,
                    key = key,
                    content = "content".byteInputStream(),
                    contentType = "text/plain"
                )
            )

            val headRequest = DefaultHeadObjectRequest(bucket = bucket, key = key)
            val response = store.headObject(headRequest)

            with(response) {
                bucket.assert().isEqualTo(bucket)
                key.assert().isEqualTo(key)
                eTag.assert().isNotNull()
            }
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should put and get object`() {
        val key = generateKey("put-get")
        val content = "Hello S3 via OSS!".repeat(100)
        try {
            val putRequest = DefaultPutObjectRequest(
                bucket = bucket,
                key = key,
                content = content.byteInputStream(),
                contentType = "text/plain"
            )
            val putResponse = store.putObject(putRequest)
            putResponse.eTag.assert().isNotNull()

            val getRequest = DefaultGetObjectRequest(bucket = bucket, key = key)
            val getResponse = store.getObject(getRequest)
            getResponse.metadata.bucket.assert().isEqualTo(bucket)
            getResponse.metadata.key.assert().isEqualTo(key)
            val readContent = getResponse.content.bufferedReader().readText()
            readContent.assert().isEqualTo(content)
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should delete object`() {
        val key = generateKey("delete")
        try {
            store.putObject(
                DefaultPutObjectRequest(
                    bucket = bucket,
                    key = key,
                    content = "to be deleted".byteInputStream(),
                    contentType = "text/plain"
                )
            )

            val deleteResponse = store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
            deleteResponse.deleteMarker.assert().isFalse()
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should list objects`() {
        val prefix = "costore/test/s3-list-${System.currentTimeMillis()}"
        val key1: ObjectKey = "$prefix-1"
        val key2: ObjectKey = "$prefix-2"

        try {
            listOf(key1, key2).forEach { k ->
                store.putObject(
                    DefaultPutObjectRequest(
                        bucket = bucket,
                        key = k,
                        content = "content".byteInputStream(),
                        contentType = "text/plain"
                    )
                )
            }

            val listRequest = DefaultListObjectsRequest(
                bucket = bucket,
                prefix = "$prefix-",
                maxKeys = 100
            )
            val listResponse = store.listObjects(listRequest)
            listResponse.objects.assert().hasSize(2)
            listResponse.isTruncated.assert().isFalse()
        } finally {
            listOf(key1, key2).forEach { k ->
                store.deleteObject(DefaultDeleteObjectRequest(bucket, k))
            }
        }
    }

    @Test
    fun `should presign get object`() {
        val key = generateKey("presign-get")
        try {
            store.putObject(
                DefaultPutObjectRequest(
                    bucket = bucket,
                    key = key,
                    content = "content".byteInputStream(),
                    contentType = "text/plain"
                )
            )

            val presignRequest = PresignRequest.Get(
                bucket = bucket,
                key = key,
                expiration = Duration.ofMinutes(15)
            )
            val response = store.presignGetObject(presignRequest)

            response.url.assert().isNotNull()
            response.expiration.assert().isNotNull()
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should presign put object`() {
        val key = generateKey("presign-put")

        try {
            val presignRequest = PresignRequest.Put(
                bucket = bucket,
                key = key,
                expiration = Duration.ofMinutes(15),
                contentType = "text/plain"
            )
            val response = store.presignPutObject(presignRequest)

            response.url.assert().isNotNull()
            response.expiration.assert().isNotNull()
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should presign delete object`() {
        val key = generateKey("presign-delete")

        try {
            store.putObject(
                DefaultPutObjectRequest(
                    bucket = bucket,
                    key = key,
                    content = "content".byteInputStream(),
                    contentType = "text/plain"
                )
            )

            val presignRequest = PresignRequest.Delete(
                bucket = bucket,
                key = key,
                expiration = Duration.ofMinutes(15)
            )
            val response = store.presignDeleteObject(presignRequest)

            response.url.assert().isNotNull()
            response.expiration.assert().isNotNull()
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }
}
