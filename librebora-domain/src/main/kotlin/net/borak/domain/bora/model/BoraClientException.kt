package net.borak.domain.bora.model

class BoraClientException(val code: Int,
                          override val message: String): RuntimeException(message)
