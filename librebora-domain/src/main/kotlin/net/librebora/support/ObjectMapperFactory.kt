package net.librebora.support

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

object ObjectMapperFactory {

    val snakeCaseMapper: ObjectMapper = defaultObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    val camelCaseMapper: ObjectMapper = defaultObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)

    private fun defaultObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(JodaModule())
            .registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }
}