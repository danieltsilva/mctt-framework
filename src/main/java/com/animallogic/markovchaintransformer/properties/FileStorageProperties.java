package com.animallogic.markovchaintransformer.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir = "/tmp/markov-files";

}
