package com.animallogic.markovchaintransformer.services.impl;

import com.animallogic.markovchaintransformer.services.FileStorageService;
import com.animallogic.markovchaintransformer.services.MarkovChainService;
import com.animallogic.markovchaintransformer.services.exceptions.MyFileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class MarkovChainServiceImpl implements MarkovChainService {

    @Autowired
    FileStorageService fileStorageService;

    private static Random r = new Random();

    @Override
    public String markovChainText(String fileName, int order, int outputSize) {
        String mcText = "";
        try {
            mcText =  markovTransformation(fileName, order, outputSize);
        } catch (MyFileNotFoundException ex) {
            ex.printStackTrace();
        }
        return mcText;
    }

    private Map<String, List<String>> createTransitionMatrix(String[] words, int order) {
        log.debug("SERVICE: createTransitionMatrix countOfWords:{} order:{}", words.length, order);

        Map<String, List<String>> transitionMatrix = new HashMap<>();

        // Iterating through all words
        for (int i = 0; i < (words.length - order); ++i) {

            // Creating prefixes according param order
            StringBuilder prefix = new StringBuilder(words[i]);
            for (int j = i + 1; j < i + order; ++j) {
                prefix.append(' ').append(words[j]);
            }

            // Getting prefix's suffix
            String value = (i + order < words.length) ? words[i + order] : "";

            // Map new tuple ( prefix -> suffix )
            transitionMatrix
                    .computeIfAbsent( prefix.toString(),  k -> new ArrayList<>())
                    .add(value);

        }
        log.debug("Transition Matrix created. Number of prefixes mapped: {}", transitionMatrix.size());

        return transitionMatrix;
    }

    private String markovTransformation(String fileName, int keySize, int outputSize) throws MyFileNotFoundException {
        log.info("SERVICE: markovTransformation {} {} {}", fileName, keySize, outputSize);

        if (keySize < 1) throw new IllegalArgumentException("Key size can't be less than 1");

        byte[] bytes = fileStorageService.loadFileAsBytes(fileName);

        //TODO improve text cleaning
        String[] words = new String(bytes).trim().split(" ");
        if (outputSize < keySize || outputSize >= words.length) {
            log.error("outputSize: {}, keySize: {}, wordsLenght: {}", outputSize, keySize, words.length);
            throw new IllegalArgumentException("Output size is out of range");
        }

        // Creating Transitional Matrix
        Map<String, List<String>> transMatrix = createTransitionMatrix(words, keySize);

        int n = 0;
        int rn = r.nextInt(transMatrix.size());
        String prefix = (String) transMatrix.keySet().toArray()[rn];
        List<String> output = new ArrayList<>(Arrays.asList(prefix.split(" ")));

        //TODO decouple text generator logic
        while (true) {
            List<String> suffix = transMatrix.get(prefix);
            if (suffix.size() == 1) {
                if (Objects.equals(suffix.get(0), "")) return output.parallelStream().reduce("", (a, b) -> a + " " + b);
                output.add(suffix.get(0));
            } else {
                rn = r.nextInt(suffix.size());
                output.add(suffix.get(rn));
            }
            if (output.size() >= outputSize) return output.parallelStream().limit(outputSize).reduce("", (a, b) -> a + " " + b);
            n++;
            prefix = output.parallelStream().skip(n).limit(keySize).reduce("", (a, b) -> a + " " + b).trim();
        }
    }

}
