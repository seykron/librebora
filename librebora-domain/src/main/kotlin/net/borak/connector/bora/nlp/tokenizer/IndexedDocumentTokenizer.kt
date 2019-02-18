package net.borak.connector.bora.nlp.tokenizer

import net.borak.connector.bora.nlp.DocumentStream

class IndexedDocumentTokenizer : Tokenizer {

    companion object {
        private const val CLOSING_PARENTHESIS: Byte = 41
    }

    override fun tokenize(document: ByteArray): List<Token> {
        return DocumentStream(document).fold(
            TokenizerContext.new()
        ) { context, stream, chunk ->
            val isToken: Boolean = context.isNumeric() && chunk.contains(CLOSING_PARENTHESIS)
            val isCandidate: Boolean = context.isNumeric() || context.isEmpty()

            when {
                isToken -> context.collect(chunk).open(
                    position = stream.position()
                )
                isCandidate -> context.collect(chunk)
                else -> context.reset().collect(chunk)
            }
        }.finish(document.size)
    }
}