package net.borak.connector.bora.nlp.tokenizer

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.nio.charset.Charset

data class Token(val start: Int,
                 val end: Int,
                 val name: ByteArray,
                 val data: ByteArray) {

    companion object {
        fun open(name: ByteArray,
                 position: Int): Token {
            return Token(
                start = position,
                end = -1,
                data = ByteArray(0),
                name = name
            )
        }
    }

    fun close(position: Int,
              dropTail: Int = 0): Token {
        return if (dropTail > 0) {
            copy(
                end = position,
                data = data.sliceArray(0..(data.size - dropTail - 1))
            )
        } else {
            copy(
                end = position,
                data = data
            )
        }
    }

    fun append(vararg chunks: ByteArray): Token {
        return copy(
            data = chunks.fold(data) { acc, chunk ->
                acc + chunk
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        return EqualsBuilder.reflectionEquals(this, other)
    }

    override fun hashCode(): Int {
        return HashCodeBuilder.reflectionHashCode(this)
    }

    override fun toString(): String {
        return "${name.toString(Charset.defaultCharset())}=${data.toString(Charset.defaultCharset())}"
    }
}
