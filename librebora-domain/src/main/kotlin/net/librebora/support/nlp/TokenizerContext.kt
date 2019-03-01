package net.librebora.support.nlp

import net.librebora.connector.bora.nlp.ParseException
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.nio.charset.Charset

data class TokenizerContext(val tokens: List<Token>,
                            val currentToken: Token? = null,
                            val data: ByteArray) {

    companion object {
        private val NUMBERIC_CHAR_CODES: IntRange = 48..57

        fun new(): TokenizerContext {
            return TokenizerContext(
                tokens = emptyList(),
                data = ByteArray(0)
            )
        }
    }

    fun open(position: Int): TokenizerContext {
        return open(position, data)
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
                currentToken = newToken
            )
        } ?: copy(
            data = ByteArray(0),
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

    fun close(
        position: Int,
        dropTail: Int = 0
    ): TokenizerContext {
        return currentToken?.let { currentToken ->
            copy(
                data = ByteArray(0),
                tokens = tokens + currentToken.close(position, dropTail),
                currentToken = null
            )
        } ?: throw ParseException("There's no open token")
    }

    fun collect(vararg chunks: ByteArray): TokenizerContext {
        return copy(
            data = chunks.fold(data) { acc, chunk ->
                acc + chunk
            },
            currentToken = currentToken?.append(*chunks)
        )
    }

    fun reset(): TokenizerContext {
        return copy(
            data = ByteArray(0)
        )
    }

    fun isNumeric(): Boolean {
        return data.isNotEmpty() && data.all { byte ->
            NUMBERIC_CHAR_CODES.contains(byte)
        }
    }

    fun size(): Int {
        return data.size
    }

    fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    fun isClosed(): Boolean {
        return currentToken == null
    }

    fun isOpen(): Boolean {
        return currentToken != null
    }

    fun matches(regex: Regex): Boolean {
        return regex.matches(data.toString(Charset.defaultCharset()))
    }

    override fun equals(other: Any?): Boolean {
        return EqualsBuilder.reflectionEquals(this, other)
    }

    override fun hashCode(): Int {
        return HashCodeBuilder.reflectionHashCode(this)
    }
}
