package com.animallogic.markovchaintransformer;

import com.animallogic.markovchaintransformer.properties.FileStorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class MarkovChainTransformerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarkovChainTransformerApplication.class, args);
		log.info("Initializing application Markov Chain Transformer.");
	}

}
