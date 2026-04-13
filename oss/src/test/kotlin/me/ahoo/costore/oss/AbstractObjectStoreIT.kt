package me.ahoo.costore.oss

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable

@EnabledIfEnvironmentVariable(named = "OSS_ACCESS_KEY_ID", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_SECRET_ACCESS_KEY", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_ENDPOINT", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_BUCKET", matches = ".+")
abstract class AbstractObjectStoreIT {

    protected lateinit var bucket: BucketName

    @BeforeEach
    fun setup() {
        val endpoint = System.getenv("OSS_ENDPOINT")!!
        val accessKey = System.getenv("OSS_ACCESS_KEY_ID")!!
        val secretKey = System.getenv("OSS_SECRET_ACCESS_KEY")!!
        bucket = System.getenv("OSS_BUCKET") as BucketName

        val client: OSS = OSSClientBuilder().build(endpoint, accessKey, secretKey)
        initStore(OssObjectStore(client))
    }

    protected abstract fun initStore(syncStore: OssObjectStore)

    protected abstract fun closeStore()

    protected fun newKey(prefix: String): ObjectKey {
        return prefix.withTestPrefix()
    }

    protected fun headObjectTest(content: String = "content") {
        val key = newKey("head")
        try {
            doPutObject(key, content)
            val response = doHeadObject(key)
            response.bucket.assert().isEqualTo(bucket)
            response.key.assert().isEqualTo(key)
            response.eTag.assert().isNotNull()
        } finally {
            doDeleteObject(key)
        }
    }

    protected fun putAndGetObjectTest(content: String = "Hello OSS!".repeat(100)) {
        val key = newKey("put-get")
        try {
            val putResponse = doPutObject(key, content)
            putResponse.eTag.assert().isNotNull()

            val getResponse = doGetObject(key)
            getResponse.metadata.bucket.assert().isEqualTo(bucket)
            getResponse.metadata.key.assert().isEqualTo(key)
            val readContent = getResponse.content.bufferedReader().readText()
            readContent.assert().isEqualTo(content)
        } finally {
            doDeleteObject(key)
        }
    }

    protected fun deleteObjectTest() {
        val key = newKey("delete")
        try {
            doPutObject(key, "to be deleted")
            val deleteResponse = doDeleteObject(key)
            deleteResponse.deleteMarker.assert().isFalse()
        } finally {
            doDeleteObject(key)
        }
    }

    protected abstract fun doPutObject(key: ObjectKey, content: String): PutObjectResponse
    protected abstract fun doGetObject(key: ObjectKey): GetObjectResponse
    protected abstract fun doHeadObject(key: ObjectKey): HeadObjectResponse
    protected abstract fun doDeleteObject(key: ObjectKey): DeleteObjectResponse

    @AfterEach
    fun cleanup() {
        closeStore()
    }
}

fun String.withTestPrefix(): ObjectKey {
    return "costore/test/$this-${System.currentTimeMillis()}" as ObjectKey
}
