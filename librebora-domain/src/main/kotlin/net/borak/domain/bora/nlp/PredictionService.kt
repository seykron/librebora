package net.borak.domain.bora.nlp

import org.tartarus.snowball.ext.SpanishStemmer
import java.text.Normalizer

class PredictionService(private val classifiersConfig: List<ClassifierConfig>) {

    companion object {
        private val REGEX_UNACCENT: Regex = Regex("\\p{InCombiningDiacriticalMarks}+")
    }

    private val stemmer: SpanishStemmer = SpanishStemmer()
    private val classifiers: MutableMap<String, List<String>> = mutableMapOf()

    fun loadAndTrain() {
        classifiersConfig.forEach { classifierConfig ->
            val entries: List<String> = Thread.currentThread().contextClassLoader.getResource(
                classifierConfig.resourceLocation
            ).readText().split("\n").map { classifier ->
                normalize(classifier)
            }

            classifiers[classifierConfig.category] = entries
        }
    }

    fun predict(text: String,
                candidateClassifiers: List<String>,
                caseSensitive: Boolean = false): String? {
        val resolvedText = normalize(text, caseSensitive)

        return candidateClassifiers.find { classifier ->
            resolvedText.contains(normalize(classifier, caseSensitive))
        }
    }

    private fun normalize(text: String,
                          caseSensitive: Boolean = false): String {

        val normalizedText: String = Normalizer
            .normalize(text, Normalizer.Form.NFD)
            .replace(REGEX_UNACCENT, "")

        return normalizedText.split(" ").map { word ->
            stemmer.current = word
            stemmer.stem()
            if (caseSensitive) {
                stemmer.current.trim()
            } else {
                stemmer.current.trim().toLowerCase()
            }
        }.joinToString(" ")
    }
}
