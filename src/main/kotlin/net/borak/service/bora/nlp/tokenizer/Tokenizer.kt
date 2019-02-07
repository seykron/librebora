package net.borak.service.bora.nlp.tokenizer

interface Tokenizer {
    fun tokenize(document: ByteArray): List<Token>
}
