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
        log.info("SERVICE: markovTransformation {} {} {}", fileName, order, outputSize);

        String mcText = "";
        try {
            mcText =  markovTransformation(fileName, order, outputSize);
        } catch (MyFileNotFoundException ex) {
            log.error("File not found", ex);
            log.error("fileName: {}, order: {}, outputSize: {}", outputSize, order, outputSize);
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

    private String generateTextWithMarkovChain(Map<String, List<String>> transMatrix, int order, int outputSize) {
        log.debug("SERVICE: generateTextWithMarkovChain transMatrix.size:{} order:{} outputSize:{}",transMatrix.size(),order,outputSize);

        // Index to follow output
        int n = 0;

        // Get one random prefix
        String prefix = (String) transMatrix.keySet().toArray()[r.nextInt(transMatrix.size())];

        // Initialize the output according the random prefix
        List<String> output = new ArrayList<>(Arrays.asList(prefix.split(" ")));
        log.debug("Starting with prefix=[{}] and output=[{}]", prefix, output.toString());

        while (true) {

            // If the transitional matrix doesn't have the prefix,
            // it means the prefix is the last one and the text will
            // be finished before reach outputSize
            if(!transMatrix.containsKey(prefix)) break;

            List<String> suffix = transMatrix.get(prefix);

            if (suffix.size() == 1) {

                if (Objects.equals(suffix.get(0), "")) break;

                // if there is just one suffix for that prefix, add it to the output
                output.add(suffix.get(0));
            } else {
                // if there are more then one option of suffix for that prefix, choose one randomly between their options
                output.add(suffix.get(r.nextInt(suffix.size())));
            }

            // Return text if output size reached
            if (output.size() >= outputSize) return output.stream().limit(outputSize).reduce("", (a, b) -> a + " " + b).trim();
            n++;
            prefix = output.stream().skip(n).limit(order).reduce("", (a, b) -> a + " " + b).trim();
            //TODO Improvement: trim doesn't work if changed to parallelStream
        }

        //  Building text output
        return output.stream().reduce("", (a, b) -> a + " " + b).trim();
    }

    private String markovTransformation(String fileName, int order, int outputSize) throws MyFileNotFoundException {
        log.debug("SERVICE: markovTransformation {} {} {}", fileName, order, outputSize);

        if (order < 1) throw new IllegalArgumentException("Order can't be less than 1");

        byte[] bytes = fileStorageService.loadFileAsBytes(fileName);

        //TODO improve text cleaning
        String[] words = new String(bytes).trim().split(" ");
        if (outputSize < order || outputSize >= words.length) {
            log.error("outputSize: {}, order: {}, wordsLenght: {}", outputSize, order, words.length);
            throw new IllegalArgumentException("Output size is out of range");
        }

        // Creating Transitional Matrix
        Map<String, List<String>> transMatrix = createTransitionMatrix(words, order);

        // Generate text using the markov chain transitional matrix
        return generateTextWithMarkovChain(transMatrix, order, outputSize);
    }

}
