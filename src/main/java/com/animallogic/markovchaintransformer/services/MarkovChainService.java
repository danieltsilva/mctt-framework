package com.animallogic.markovchaintransformer.services;

public interface MarkovChainService {

    public String markovChainText(String fileName, int order, int outputSize);

}
