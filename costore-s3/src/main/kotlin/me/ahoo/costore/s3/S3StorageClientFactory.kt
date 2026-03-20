package me.ahoo.costore.s3

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

/**
 * Configuration for building an [S3StorageClient].
 *
 * @property region          AWS region string (e.g. `"us-east-1"`). Defaults to `"us-east-1"`.
 * @property accessKeyId     AWS access key ID; when null the SDK default credential
 *                           chain is used
 * @property secretAccessKey AWS secret access key; required when [accessKeyId] is set
 * @property endpointOverride optional endpoint URL for local testing (e.g. LocalStack)
 *                           or S3-compatible services
 */
data class S3StorageClientConfig(
    val region: String = "us-east-1",
    val accessKeyId: String? = null,
    val secretAccessKey: String? = null,
    val endpointOverride: String? = null,
)

/**
 * Factory that creates [S3StorageClient] instances from [S3StorageClientConfig].
 *
 * Example usage:
 * ```kotlin
 * val client = S3StorageClientFactory.create(
 *     S3StorageClientConfig(
 *         region = "us-east-1",
 *         accessKeyId = "AKID...",
 *         secretAccessKey = "SECRET",
 *     )
 * )
 * ```
 */
object S3StorageClientFactory {

    /**
     * Creates and returns a fully configured [S3StorageClient].
     */
    fun create(config: S3StorageClientConfig): S3StorageClient {
        val credentialsProvider = buildCredentialsProvider(config)
        val region = Region.of(config.region)

        val s3Client = S3Client.builder()
            .region(region)
            .apply {
                credentialsProvider?.let { credentialsProvider(it) }
                config.endpointOverride?.let { endpointOverride(URI.create(it)) }
            }
            .build()

        val presigner = S3Presigner.builder()
            .region(region)
            .apply {
                credentialsProvider?.let { credentialsProvider(it) }
                config.endpointOverride?.let { endpointOverride(URI.create(it)) }
            }
            .build()

        return S3StorageClient(s3Client, presigner)
    }

    private fun buildCredentialsProvider(config: S3StorageClientConfig): AwsCredentialsProvider? {
        if (config.accessKeyId == null) return null
        requireNotNull(config.secretAccessKey) {
            "secretAccessKey must be set when accessKeyId is provided"
        }
        val credentials: AwsCredentials = AwsBasicCredentials.create(
            config.accessKeyId,
            config.secretAccessKey,
        )
        return StaticCredentialsProvider.create(credentials)
    }
}
