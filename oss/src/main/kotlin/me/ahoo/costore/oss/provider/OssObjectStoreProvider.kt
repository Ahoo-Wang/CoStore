package me.ahoo.costore.oss.provider

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.provider.AbstractObjectStoreProvider
import me.ahoo.costore.core.provider.CommonStoreProviderCredentials
import me.ahoo.costore.core.provider.EndpointCapable
import me.ahoo.costore.oss.OssObjectStore

data class OssCredentials(
    override val endpoint: String,
    override val accessKey: String,
    override val secretKey: String,
) : CommonStoreProviderCredentials, EndpointCapable

class OssObjectStoreProvider : AbstractObjectStoreProvider<OssCredentials>() {

    override fun sync(credentials: OssCredentials): ObjectStore {
        val client: OSS = OSSClientBuilder().build(
            credentials.endpoint,
            credentials.accessKey,
            credentials.secretKey
        )
        return OssObjectStore(client)
    }
}
