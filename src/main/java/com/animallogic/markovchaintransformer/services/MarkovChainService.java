package com.animallogic.markovchaintransformer.services;

/**
 * This service consists of methods to generate a random text, using Markov Chain Algorithm and
 * base on a file previously uploaded.
 *
 * <p> In this first version, all the files are uploaded in file system.
 *
 * @author danieltsilva
 *
 */
public interface MarkovChainService {

    /**
     * Create a random text, based on mapping a previous uploaded text file and using Markov Chain
     * Algorithm to determine the sequence of the text.
     *
     * @param   fileName
     *          The string filename.
     *
     * @param   order
     *          Number of words that defines a Markov Chain prefix. This number needs to be lesser than
     *          base text size (count in number of words) and lesser than outputSize parameter
     *
     * @param   outputSize
     *          Number of words the generated text will have at maximum. If before complete outputSize
     *          the Algorithm reach a final state (the last order-th words from the base text), the
     *          text returned will have less words than defined on outputSize.
     *
     * @return  Return the text {@code String} generated randomly, following the Markov Chain transitional
     *          matrix.
     *
     */
    public String markovChainText(String fileName, int order, int outputSize);

    /**
     * Create a random text, based on mapping a previous uploaded text file and using Markov Chain
     * Algorithm to determine the sequence of the text.
     *
     * @param   fileName
     *          The string filename.
     *
     * @param   order
     *          Number of words that defines a Markov Chain prefix. This number needs to be lesser than
     *          base text size (count in number of words) and lesser than outputSize parameter
     *
     * @param   outputSize
     *          Number of words the generated text will have at maximum. If before complete outputSize
     *          the Algorithm reach a final state (the last order-th words from the base text), the
     *          text returned will have less words than defined on outputSize.
     *
     * @param   cleanText
     *          If true, before the algorithm process, the text will be filtered. It Will be erased
     *          from the text all the ASCII control characters and non-printable characters from Unicode.
     *
     * @return  Return the text {@code String} generated randomly, following the Markov Chain transitional
     *          matrix.
     *
     */
    public String markovChainTextWithFilter(String fileName, int order, int outputSize, boolean cleanText);

}
