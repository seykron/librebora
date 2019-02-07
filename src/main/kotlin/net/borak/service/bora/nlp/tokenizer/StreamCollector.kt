package net.borak.service.bora.nlp.tokenizer

data class StreamCollector(val data: ByteArray) {

    companion object {
        private val NUMBERIC_CHAR_CODES: IntRange = 48..57

        fun new(): StreamCollector {
            return StreamCollector(ByteArray(0))
        }
    }

    fun put(vararg chunks: ByteArray): StreamCollector {
        return copy(
            data = chunks.fold(data) { acc, chunk ->
                acc + chunk
            }
        )
    }

    fun reset(): StreamCollector {
        return new()
    }

    fun isNumeric(): Boolean {
        return data.isNotEmpty() && data.all { byte ->
            NUMBERIC_CHAR_CODES.contains(byte)
        }
    }

    fun size(): Int {
        return data.size
    }

    override fun equals(other: Any?): Boolean {
        return if (other is StreamCollector) {
            other.data.contentEquals(this.data)
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}