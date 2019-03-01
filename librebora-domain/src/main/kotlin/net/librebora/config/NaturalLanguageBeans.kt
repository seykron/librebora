package net.librebora.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.librebora.support.nlp.ClassifierConfig
import net.librebora.support.nlp.PredictionService
import net.librebora.connector.bora.nlp.CompanyParser
import net.librebora.connector.bora.nlp.FileInfoParser
import net.librebora.connector.bora.nlp.PartnersParser
import net.librebora.connector.bora.nlp.SectionTagger
import net.librebora.connector.bora.nlp.ClassifiedDocumentTokenizer
import net.librebora.connector.bora.nlp.IndexedDocumentTokenizer
import net.librebora.connector.bora.nlp.WordTokenizer
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
