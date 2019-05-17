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
    public String markovChainText() {
        String mcText = "";
        try {
            mcText =  markovTransformation("sample.txt", 2, 20);
        } catch (MyFileNotFoundException ex) {
            ex.printStackTrace();
        }
        return mcText;
    }

    private Map<String, List<String>> createTransitionMatrix(String[] words, int order) {

        Map<String, List<String>> transitionMatrix = new HashMap<>();

        //TODO improve loops and try to use Streams
        for (int i = 0; i < (words.length - order); ++i) {
            StringBuilder key = new StringBuilder(words[i]);
            for (int j = i + 1; j < i + order; ++j) {
                key.append(' ').append(words[j]);
            }
            String value = (i + order < words.length) ? words[i + order] : "";
            if (!transitionMatrix.containsKey(key.toString())) {
                ArrayList<String> list = new ArrayList<>();
                list.add(value);
                transitionMatrix.put(key.toString(), list);
            } else {
                transitionMatrix.get(key.toString()).add(value);
            }
        }

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
        Map<String, List<String>> dict = new HashMap<>();

        //TODO decouple transitional matrix logic
        for (int i = 0; i < (words.length - keySize); ++i) {
            StringBuilder key = new StringBuilder(words[i]);
            for (int j = i + 1; j < i + keySize; ++j) {
                key.append(' ').append(words[j]);
            }
            String value = (i + keySize < words.length) ? words[i + keySize] : "";
            if (!dict.containsKey(key.toString())) {
                ArrayList<String> list = new ArrayList<>();
                list.add(value);
                dict.put(key.toString(), list);
            } else {
                dict.get(key.toString()).add(value);
            }
        }

        int n = 0;
        int rn = r.nextInt(dict.size());
        String prefix = (String) dict.keySet().toArray()[rn];
        List<String> output = new ArrayList<>(Arrays.asList(prefix.split(" ")));

        //TODO decouple text generator logic
        while (true) {
            List<String> suffix = dict.get(prefix);
            if (suffix.size() == 1) {
                if (Objects.equals(suffix.get(0), "")) return output.stream().reduce("", (a, b) -> a + " " + b);
                output.add(suffix.get(0));
            } else {
                rn = r.nextInt(suffix.size());
                output.add(suffix.get(rn));
            }
            if (output.size() >= outputSize) return output.stream().limit(outputSize).reduce("", (a, b) -> a + " " + b);
            n++;
            prefix = output.stream().skip(n).limit(keySize).reduce("", (a, b) -> a + " " + b).trim();
        }
    }

}
