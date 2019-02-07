package net.borak.service.bora.nlp.tokenizer

import net.borak.service.bora.nlp.DocumentStream

class WordTokenizer : Tokenizer {

    companion object {
        private const val SPACE: Byte = 32
        private const val COMMA: Byte = 44
        private const val DOT: Byte = 46
        private const val COLON: Byte = 58
        private const val SEMICOLON: Byte = 59
        private const val OPENING_PARENTHESIS: Byte = 40
        private const val CLOSING_PARENTHESIS: Byte = 41
        private const val OPENING_SQUARE_BRACKET: Byte = 91
        private const val CLOSING_SQUARE_BRACKET: Byte = 93
        private const val OPENING_BRACKET: Byte = 123
        private const val CLOSING_BRACKET: Byte = 125
        private const val QUOTE: Byte = 34
        private const val SINGLE_QUOTE: Byte = 39
        private const val LINE_FEED: Byte = 13
        private const val CARRIAGE_RETURN: Byte = 10
        private val STOP_CHARACTERS: List<Byte> = listOf(
            SPACE,
            COMMA,
            DOT,
            COLON,
            SEMICOLON,
            OPENING_PARENTHESIS,
            CLOSING_PARENTHESIS,
            OPENING_SQUARE_BRACKET,
            CLOSING_SQUARE_BRACKET,
            OPENING_BRACKET,
            CLOSING_BRACKET,
            QUOTE,
            SINGLE_QUOTE,
            LINE_FEED,
            CARRIAGE_RETURN
        )
    }

    override fun tokenize(document: ByteArray): List<Token> {
        return DocumentStream(document).fold(
            TokenizerContext.new()
        ) { context, stream, chunk ->
            val wordEnds: Boolean = STOP_CHARACTERS.contains(chunk.first())

            when {
                wordEnds && !context.isClosed() -> context
                    .close(stream.position())
                    .open(stream.position() + 1)
                context.isEmpty() -> if (wordEnds) {
                    context
                        .open(stream.position())
                } else {
                    context
                        .open(stream.position() + 1)
                        .collect(chunk)
                }
                else -> context.collect(chunk)
            }
        }.finish(document.size)
    }
}