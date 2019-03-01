package net.librebora.support.nlp

import java.io.ByteArrayInputStream
import java.nio.charset.Charset

class DocumentStream(private val document: ByteArray) {

    private val stream: ByteArrayInputStream = document.inputStream()
    private var cursor: Int = 0
    private var buffer: ByteArray = ByteArray(1)

    fun<T> fold(initialValue: T, reader: (T, DocumentStream, ByteArray) -> T): T {
        var nextValue: T = initialValue

        while (stream.available() > 0) {
            nextValue = reader(nextValue, this, readNext())
        }

        return nextValue
    }

    fun skip(bytesToSkip: Long) {
        cursor += stream.skip(bytesToSkip).toInt()
    }

    fun position(): Int {
        return cursor
    }

    fun contains(data: ByteArray): Boolean {
        // TODO: check performance.
        return document.toString(Charset.defaultCharset()).toLowerCase().contains(
            data.toString(Charset.defaultCharset()).toLowerCase()
        )
    }

    fun contains(expression: Regex): Boolean {
        // TODO: check performance.
        return document.toString(Charset.defaultCharset()).toLowerCase().contains(expression)
    }

    private fun readNext(): ByteArray {
        cursor += stream.read(buffer, 0, buffer.size)
        return buffer
    }
}