package me.ahoo.costore.core.provider

interface NullableRegionCapable {
    val region: String?
}

interface RegionCapable : NullableRegionCapable {
    override val region: String
}
