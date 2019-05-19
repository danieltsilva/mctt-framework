package com.animallogic.markovchaintransformer.services;

import com.animallogic.markovchaintransformer.services.exceptions.MyFileNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static com.animallogic.markovchaintransformer.helpers.MarkovChainHelper.checkFirstLinkMarkovChain;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MarkovChainServiceTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private MarkovChainService markovChainService;

    private static Random random = new Random();

    private String FILE_NAME    = "text.txt";
    private int ORDER           = 2;
    private int OUTPUTSIZE      = 15;
    private String BASE_TEXT    = "A Bb C D Ee F G H I J C H I D C A J I BLA";

    @Before
    public void init() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Test
    public void shouldLoadContext() {
        assertThat(markovChainService).isNotNull();
    }

    @Test
    public void shouldMarkovChainReturnSucceed() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, ORDER, OUTPUTSIZE);

        // then
        Assert.assertNotNull(markovText);
    }

    @Test
    public void shouldMarkovChainGreaterThanOrder() {
        // given
         when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, ORDER, OUTPUTSIZE);

        // then
        Assert.assertNotNull(markovText);

        String[] markovSplit = markovText.split(" ");
        Assert.assertTrue(markovSplit.length > ORDER);
        Assert.assertTrue(markovSplit.length <= OUTPUTSIZE);
        Assert.assertTrue(checkFirstLinkMarkovChain(BASE_TEXT, markovText, ORDER));
    }

    @Test
    public void shouldMarkovChainLessOrEqualThanOutputSize() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, ORDER, OUTPUTSIZE);

        // then
        Assert.assertNotNull(markovText);

        String[] markovSplit = markovText.split(" ");
        Assert.assertTrue(markovSplit.length <= OUTPUTSIZE);
        Assert.assertTrue(checkFirstLinkMarkovChain(BASE_TEXT, markovText, ORDER));
    }

    @Test
    public void shouldMarkovChainAlgorithmSucceed() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, ORDER, OUTPUTSIZE);

        // then
        Assert.assertNotNull(markovText);

        String[] markovSplit = markovText.split(" ");
        Assert.assertTrue(markovSplit.length > ORDER);
        Assert.assertTrue(markovSplit.length <= OUTPUTSIZE);
        Assert.assertTrue(checkFirstLinkMarkovChain(BASE_TEXT, markovText, ORDER));
    }

    @Test( expected = IllegalArgumentException.class)
    public void shouldNotOrderLessThanOne() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, 0, OUTPUTSIZE);
    }


    @Test( expected = IllegalArgumentException.class)
    public void shouldNotOutputEqualTextSize() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, ORDER, BASE_TEXT.split(" ").length);
    }

    @Test( expected = IllegalArgumentException.class)
    public void shouldNotOutputGreaterThanTextSize() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, ORDER, BASE_TEXT.split(" ").length+1);
    }

    @Test( expected = IllegalArgumentException.class)
    public void shouldNotOutputLessThanOrder() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenReturn(BASE_TEXT.getBytes());

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, 7, 6);
    }

    @Test
    public void shouldMarkovChainTextFileNotFound() {
        // given
        when(fileStorageService.loadFileAsBytes(FILE_NAME)).thenThrow(MyFileNotFoundException.class);

        // when
        String markovText = markovChainService.markovChainText(FILE_NAME, ORDER, OUTPUTSIZE);

        // then
        Assert.assertNotNull(markovText);
        Assert.assertEquals("",markovText);
    }

}
