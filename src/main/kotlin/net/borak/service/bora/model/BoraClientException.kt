package net.borak.service.bora.model

class BoraClientException(val code: Int,
                          override val message: String): RuntimeException(message)
