package me.ahoo.costore.core.provider

/** Interface for credentials or configurations that may specify an AWS region. */
interface NullableRegionCapable {
    /** The AWS region (e.g., "us-east-1"), or null to use the provider's default. */
    val region: String?
}

/** Interface for credentials or configurations that require an AWS region. */
interface RegionCapable : NullableRegionCapable {
    override val region: String
}
