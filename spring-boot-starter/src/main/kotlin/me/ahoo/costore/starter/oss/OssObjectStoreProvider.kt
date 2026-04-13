package me.ahoo.costore.starter.oss

import com.aliyun.oss.OSS
import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.provider.AbstractObjectStoreProvider
import me.ahoo.costore.oss.OssObjectStore

class OssObjectStoreProvider : AbstractObjectStoreProvider<OssCredentials>() {

    override fun sync(credentials: OssCredentials): ObjectStore {
        val client: OSS = com.aliyun.oss.OSSClientBuilder().build(
            credentials.endpoint,
            credentials.accessKey,
            credentials.secretKey
        )
        return OssObjectStore(client)
    }
}
