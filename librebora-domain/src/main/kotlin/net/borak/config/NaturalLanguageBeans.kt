package net.borak.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.borak.domain.bora.nlp.ClassifierConfig
import net.borak.domain.bora.nlp.PredictionService
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
    }
}
