package me.ahoo.costore.s3.provider

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.provider.AbstractObjectStoreProvider
import me.ahoo.costore.core.provider.StoreProviderCredentials
import me.ahoo.costore.s3.S3ObjectStore
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

class S3ObjectStoreProvider : AbstractObjectStoreProvider<S3Credentials>() {

    override fun sync(credentials: S3Credentials): ObjectStore {
        val awsCredentials = AwsBasicCredentials.create(
            credentials.accessKey,
            credentials.secretKey
        )
        val client = S3Client.builder()
            .region(Region.of(credentials.region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build()
        val presigner = S3Presigner.builder()
            .region(Region.of(credentials.region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build()
        return S3ObjectStore(client, presigner)
    }
}

data class S3Credentials(
    val region: String,
    val accessKey: String,
    val secretKey: String
) : StoreProviderCredentials
