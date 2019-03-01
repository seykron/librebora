package net.librebora.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.librebora.connector.bora.nlp.ClassifierConfig
import net.librebora.connector.bora.nlp.PredictionService
import net.librebora.connector.bora.nlp.parser.CompanyParser
import net.librebora.connector.bora.nlp.parser.FileInfoParser
import net.librebora.connector.bora.nlp.parser.PartnersParser
import net.librebora.connector.bora.nlp.parser.SectionTagger
import net.librebora.connector.bora.nlp.tokenizer.ClassifiedDocumentTokenizer
import net.librebora.connector.bora.nlp.tokenizer.IndexedDocumentTokenizer
import net.librebora.connector.bora.nlp.tokenizer.WordTokenizer
import org.springframework.context.support.beans

object NaturalLanguageBeans {
    fun beans() = beans {
        val mainConfig: Config = ConfigFactory.defaultApplication().resolve()

        bean {
            mainConfig.getConfigList("classifiers").map { classifierConfig ->
                ClassifierConfig(
                    category = classifierConfig.getString("category"),
                    resourceLocation = classifierConfig.getString("resource-location")
                )
            }
        }

        bean {
            PredictionService(
                classifiersConfig = ref()
            )
        }
        bean<ClassifiedDocumentTokenizer>()
        bean<IndexedDocumentTokenizer>()
        bean<WordTokenizer>()
        bean<SectionTagger>()
        bean<CompanyParser>()
        bean<PartnersParser>()
        bean<FileInfoParser>()
    }
}
