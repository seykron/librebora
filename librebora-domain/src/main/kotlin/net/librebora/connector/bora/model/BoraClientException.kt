package net.librebora.connector.bora.model

class BoraClientException(val code: Int,
                          override val message: String): RuntimeException(message)
