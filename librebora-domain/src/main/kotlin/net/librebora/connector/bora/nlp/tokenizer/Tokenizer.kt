package net.librebora.connector.bora.nlp.tokenizer

interface Tokenizer {
    fun tokenize(document: ByteArray): List<Token>
}
