package net.librebora.connector.bora.nlp

import net.librebora.support.nlp.DocumentStream
import net.librebora.connector.bora.nlp.SectionName.*
import net.librebora.support.nlp.Token
import net.librebora.support.nlp.Tokenizer
import net.librebora.support.nlp.TokenizerContext
import java.nio.charset.Charset
import java.text.Normalizer

/** Tokenizer that uses classifiers words to detect tokens within a BORA file.
 * @property wordTokenizer Tokenizer to split document stream into words.
 */
class ClassifiedDocumentTokenizer(private val wordTokenizer: WordTokenizer) : Tokenizer {

    companion object {
        private val REGEX_UNACCENT: Regex = Regex("\\p{InCombiningDiacriticalMarks}+")
    }

    private val classifiedTokens: Map<String, SectionName> = mapOf(
        normalize("Socios") to PARTNERS,
        normalize("Constitucion") to CONSTITUTION,
        normalize("Denominacion") to COMPANY_NAME,
        normalize("Duracion") to DURATION,
        normalize("Objeto") to OBJECT,
        normalize("Capital") to CAPITAL,
        normalize("Administracion") to ADMINISTRATION,
        normalize("Fiscalizacion") to AUDIT,
        normalize("Ejercicio Social") to BUSINESS_YEAR_END,
        normalize("Cierre de Ejercicio") to BUSINESS_YEAR_END,
        normalize("Autorizado") to AUTHORIZATION,
        normalize("Presidente") to AUTHORITIES,
        normalize("Sede Social") to COMPANY_ADDRESS
    )

    override fun tokenize(document: ByteArray): List<Token> {
        return DocumentStream(document).fold(
            TokenizerContext.new()
        ) { context, stream, chunk ->
            val words: List<String> = wordTokenizer.tokenize(context.data).map { wordToken ->
                wordToken.data.toString(Charset.defaultCharset())
            }.filter { word ->
                word.trim().isNotEmpty()
            }

            when {
                // Forces default section if no classifier found after three words.
                words.size >= 3 && context.isClosed() -> context.open(
                    name = CONSTITUTION.name.toByteArray(),
                    position = stream.position()
                )
                words.size >= 3 || context.isOpen() -> {
                    tokenize(stream, context, words) ?: context.collect(chunk)
                }
                else -> context.collect(chunk)
            }
        }.finish(document.size)
    }

    private fun tokenize(documentStream: DocumentStream,
                         context: TokenizerContext,
                         words: List<String>): TokenizerContext? {

        // Finds three-words classifier, then two-words classifier,
        // then single-word classifier.
        return findClassifier(
            normalize(words.takeLast(3).joinToString(" ")),
            normalize(words.takeLast(2).joinToString(" ")),
            normalize(words.takeLast(1).joinToString(" "))
        )?.let { classifier ->
            context.open(
                name = classifier.name.toByteArray(),
                position = documentStream.position()
            )
        }
    }

    private fun findClassifier(vararg words: String): SectionName? {
        return words.find { word ->
            classifiedTokens.containsKey(word)
        }?.let { word ->
            classifiedTokens[word]
        }
    }

    private fun normalize(text: String): String {
        return Normalizer
            .normalize(text, Normalizer.Form.NFD)
            .replace(REGEX_UNACCENT, "")
    }
}
