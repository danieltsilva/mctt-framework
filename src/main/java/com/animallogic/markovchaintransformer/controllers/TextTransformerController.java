package com.animallogic.markovchaintransformer.controllers;

import com.animallogic.markovchaintransformer.beans.MarkovRequest;
import com.animallogic.markovchaintransformer.beans.MarkovResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class TextTransformerController {

    @GetMapping("/markov")
    public MarkovResponse markovTraining(@RequestBody MarkovRequest markovRequest)
    {
        log.info("CONTROLLER: markovTraining");
        log.info(markovRequest.toString());

        return MarkovResponse.builder()
                .markovText("Text create by Markov Chain using base text ".concat(markovRequest.getFileName()))
                .build();
    }

}
