package net.borak.domain.bora.nlp.tokenizer

interface Tokenizer {
    fun tokenize(document: ByteArray): List<Token>
}
