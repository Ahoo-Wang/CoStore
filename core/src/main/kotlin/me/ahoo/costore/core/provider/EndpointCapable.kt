package me.ahoo.costore.core.provider

/** Interface for credentials or configurations that may have a custom endpoint. */
interface NullableEndpointCapable {
    /** The custom endpoint URL, or null to use the provider's default. */
    val endpoint: String?
}

/** Interface for credentials or configurations that require a custom endpoint. */
interface EndpointCapable : NullableEndpointCapable {
    override val endpoint: String
}
