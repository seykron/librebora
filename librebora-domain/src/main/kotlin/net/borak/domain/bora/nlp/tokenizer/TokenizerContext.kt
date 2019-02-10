package net.borak.domain.bora.nlp.tokenizer

import net.borak.domain.bora.nlp.parser.ParseException
import java.nio.charset.Charset

data class TokenizerContext(val collector: StreamCollector,
                            val tokens: List<Token>,
                            val currentToken: Token? = null) {

    companion object {
        fun new(): TokenizerContext {
            return TokenizerContext(
                collector = StreamCollector.new(),
                tokens = emptyList()
            )
        }
    }

    fun open(position: Int): TokenizerContext {
        return open(position, collector.data)
    }

    fun open(
        position: Int,
        name: ByteArray
    ): TokenizerContext {

        val newToken = Token.open(
            name = name,
            position = position
        )

        return currentToken?.let {
            close(
                position = position - name.size,
                dropTail = name.size
            ).copy(
                collector = collector.reset(),
                currentToken = newToken
            )
        } ?: copy(
            collector = collector.reset(),
            currentToken = newToken
        )
    }

    fun finish(position: Int): List<Token> {
        return if (!isClosed()) {
            close(
                position = position,
                dropTail = 0
            ).tokens
        } else {
            tokens
        }
    }

    fun close(position: Int,
              dropTail: Int = 0): TokenizerContext {
        return currentToken?.let { currentToken ->
            copy(
                collector = collector.reset(),
                tokens = tokens + currentToken.close(position, dropTail),
                currentToken = null
            )
        } ?: throw ParseException("There's no open token")
    }

    fun collect(vararg chunks: ByteArray): TokenizerContext {
        return copy(
            collector = collector.put(*chunks),
            currentToken = currentToken?.append(*chunks)
        )
    }

    fun reset(): TokenizerContext {
        return copy(
            collector = collector.reset()
        )
    }

    fun isNumeric(): Boolean {
        return collector.isNumeric()
    }

    fun isEmpty(): Boolean {
        return collector.data.isEmpty()
    }

    fun isClosed(): Boolean {
        return currentToken == null
    }

    fun isOpen(): Boolean {
        return currentToken != null
    }

    fun matches(regex: Regex): Boolean {
        return regex.matches(collector.data.toString(Charset.defaultCharset()))
    }
}
