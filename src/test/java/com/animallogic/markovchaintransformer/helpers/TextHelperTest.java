package com.animallogic.markovchaintransformer.helpers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.animallogic.markovchaintransformer.helpers.TextHelper.cleanTextContent;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TextHelperTest {

    private final String DIRTY_TEXT = "Blá blé blí bló blú !@#$%*()_+=-0987654321\n\t";
    private final String CLEAN_TEXT = "Blá blé blí bló blú !@#$%*()_+=-0987654321";

    @Test
    public void cleanText() {
        Assert.assertEquals(CLEAN_TEXT, cleanTextContent(DIRTY_TEXT));
    }

}
