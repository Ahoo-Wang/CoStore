package me.ahoo.costore.oss

import com.aliyun.oss.ClientBuilderConfiguration
import com.aliyun.oss.OSSClient
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.common.auth.DefaultCredentialProvider

/**
 * Configuration for building an [OssStorageClient].
 *
 * @property endpoint        OSS service endpoint, e.g. `"https://oss-cn-hangzhou.aliyuncs.com"`.
 *                           For a full list of endpoints see the OSS documentation.
 * @property accessKeyId     Alibaba Cloud access key ID
 * @property accessKeySecret Alibaba Cloud access key secret
 * @property clientConfig    optional [ClientBuilderConfiguration] for fine-tuning
 *                           connection pool, timeout, and retry settings
 */
data class OssStorageClientConfig(
    val endpoint: String,
    val accessKeyId: String,
    val accessKeySecret: String,
    val clientConfig: ClientBuilderConfiguration = ClientBuilderConfiguration(),
)

/**
 * Factory that creates [OssStorageClient] instances from [OssStorageClientConfig].
 *
 * Example usage:
 * ```kotlin
 * val client = OssStorageClientFactory.create(
 *     OssStorageClientConfig(
 *         endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
 *         accessKeyId = "ACCESS_KEY_ID",
 *         accessKeySecret = "ACCESS_KEY_SECRET",
 *     )
 * )
 * ```
 */
object OssStorageClientFactory {

    /**
     * Creates and returns a fully configured [OssStorageClient].
     */
    fun create(config: OssStorageClientConfig): OssStorageClient {
        val credentialProvider = DefaultCredentialProvider(config.accessKeyId, config.accessKeySecret)
        val ossClient = OSSClientBuilder()
            .build(config.endpoint, credentialProvider, config.clientConfig) as OSSClient
        return OssStorageClient(ossClient, config.accessKeyId)
    }
}
