package me.ahoo.costore.core.provider

interface NullableEndpointCapable {
    val endpoint: String?
}

interface EndpointCapable : NullableEndpointCapable {
    override val endpoint: String
}
