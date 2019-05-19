package com.animallogic.markovchaintransformer.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkovRequest {

    private Integer order;
    private Integer outputSize;
    private String fileName;
    private boolean cleanText = false;

}
