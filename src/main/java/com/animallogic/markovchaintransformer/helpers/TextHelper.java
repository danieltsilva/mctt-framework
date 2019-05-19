package com.animallogic.markovchaintransformer.helpers;

public class TextHelper {

    public static String cleanTextContent(String text) {
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

}
