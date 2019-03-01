package net.librebora.support.nlp

interface Tokenizer {
    fun tokenize(document: ByteArray): List<Token>
}
