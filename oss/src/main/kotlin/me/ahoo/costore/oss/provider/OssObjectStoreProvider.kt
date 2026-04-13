package me.ahoo.costore.oss.provider

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.provider.AbstractObjectStoreProvider
import me.ahoo.costore.core.provider.CommonStoreProviderCredentials
import me.ahoo.costore.core.provider.EndpointCapable
import me.ahoo.costore.oss.OssObjectStore

/**
 * Aliyun OSS credentials for authentication.
 *
 * Unlike S3, OSS requires an explicit endpoint as OSS is not region-based.
 *
 * @property endpoint The OSS endpoint URL (e.g., "https://oss-cn-hangzhou.aliyuncs.com")
 * @property accessKeyId The OSS access key ID
 * @property secretAccessKey The OSS secret access key
 */
data class OssCredentials(
    override val endpoint: String,
    override val accessKeyId: String,
    override val secretAccessKey: String,
) : CommonStoreProviderCredentials, EndpointCapable

/**
 * Provider for creating OSS [ObjectStore] instances from [OssCredentials].
 */
class OssObjectStoreProvider : AbstractObjectStoreProvider<OssCredentials>() {

    override fun sync(credentials: OssCredentials): ObjectStore {
        val client: OSS = OSSClientBuilder().build(
            credentials.endpoint,
            credentials.accessKeyId,
            credentials.secretAccessKey
        )
        return OssObjectStore(client)
    }
}
