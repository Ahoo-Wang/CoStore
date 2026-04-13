package me.ahoo.costore.s3.provider

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.provider.AbstractObjectStoreProvider
import me.ahoo.costore.core.provider.CommonStoreProviderCredentials
import me.ahoo.costore.core.provider.NullableEndpointCapable
import me.ahoo.costore.core.provider.NullableRegionCapable
import me.ahoo.costore.s3.S3ObjectStore
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

data class S3Credentials(
    override val accessKeyId: String,
    override val secretAccessKey: String,
    override val region: String? = null,
    override val endpoint: String? = null,
) : NullableRegionCapable, CommonStoreProviderCredentials, NullableEndpointCapable

class S3ObjectStoreProvider : AbstractObjectStoreProvider<S3Credentials>() {

    override fun sync(credentials: S3Credentials): ObjectStore {
        val endpointURI = credentials.endpoint?.let {
            URI(it)
        }
        val awsCredentials = AwsBasicCredentials.create(
            credentials.accessKeyId,
            credentials.secretAccessKey
        )
        val awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials)
        val awsRegion = credentials.region?.let {
            Region.of(it)
        }
        val client = S3Client.builder()
            .endpointOverride(endpointURI)
            .region(awsRegion)
            .credentialsProvider(awsCredentialsProvider)
            .build()
        val presigner = S3Presigner.builder()
            .region(awsRegion)
            .credentialsProvider(awsCredentialsProvider)
            .build()
        return S3ObjectStore(client, presigner)
    }
}
