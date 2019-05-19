package com.animallogic.markovchaintransformer.helpers;

public class MarkovChainHelper {

    public static boolean checkFirstLinkMarkovChain(String baseText, String markovText, int order) {
        String[] markovChain = markovText.split(" ");
        StringBuilder markovPrefixSuffix = new StringBuilder();
        for (int i = 0; i < order; i++  ) {
            markovPrefixSuffix.append(markovChain[i]+" ");
        }
        markovPrefixSuffix.append(markovChain[order]);
        return baseText.contains(markovPrefixSuffix);
    }

}
