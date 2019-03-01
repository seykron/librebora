package net.librebora.support.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.librebora.support.ObjectMapperFactory
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Base64OutputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object EntitySerialization {
    val objectMapper: ObjectMapper = ObjectMapperFactory.snakeCaseMapper

    fun serialize(entity: Any): String {
        val content = objectMapper.writeValueAsString(entity)
        val output = ByteArrayOutputStream()
        val b64Output = Base64OutputStream(output)
        GZIPOutputStream(b64Output).bufferedWriter(UTF_8).use { writer ->
            writer.write(content)
        }
        return output.toString("UTF-8")
    }

    inline fun <reified T> deserialize(entity: String): T {
        val decodedInput = Base64.decodeBase64(entity)
        return GZIPInputStream(decodedInput.inputStream()).use { gzipInput ->
            objectMapper.readValue(gzipInput.readBytes())
        }
    }
}