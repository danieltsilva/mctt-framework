package com.animallogic.markovchaintransformer.controllers;

import com.animallogic.markovchaintransformer.beans.MarkovRequest;
import com.animallogic.markovchaintransformer.beans.MarkovResponse;
import com.animallogic.markovchaintransformer.services.MarkovChainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class TextTransformerController {

    @Autowired
    MarkovChainService markovChainSerice;

    @GetMapping("/markov")
    public MarkovResponse markovTraining(@RequestBody MarkovRequest markovRequest) {
        log.info("CONTROLLER: markovTraining");

        String mrkvTxt = markovChainSerice.markovChainTextWithFilter(
                markovRequest.getFileName(),
                markovRequest.getOrder(),
                markovRequest.getOutputSize(),
                markovRequest.isCleanText()
        );

        return MarkovResponse.builder()
                .markovText(mrkvTxt)
                .build();
    }

}
