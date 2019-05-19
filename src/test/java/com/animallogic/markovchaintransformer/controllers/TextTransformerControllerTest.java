package com.animallogic.markovchaintransformer.controllers;

import com.animallogic.markovchaintransformer.services.MarkovChainService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TextTransformerControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private MarkovChainService markovChainSerice;

    @Autowired
    private TextTransformerController textTransformerController;

    private String FILE_NAME = "sample.txt";

    private final String REQ_PARAM_FILENAME = "fileName";
    private final String REQ_PARAM_ORDER = "order";
    private final String REQ_PARAM_OUTPUTSIZE = "outputSize";

    @Before
    public void init() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Test
    public void shouldLoadContext() {
        assertThat(textTransformerController).isNotNull();
    }

    @Test
    public void shouldDoMarkovTraining() throws Exception {
        // given
        JSONObject jsonRequest = new JSONObject()
                                    .put(REQ_PARAM_ORDER, 2)
                                    .put(REQ_PARAM_OUTPUTSIZE, 100)
                                    .put(REQ_PARAM_FILENAME, FILE_NAME);

        JSONObject jsonResponse = new JSONObject()
                .put("message","response teste");

        when(markovChainSerice.markovChainText(
                    jsonRequest.getString(REQ_PARAM_FILENAME),
                    jsonRequest.getInt(REQ_PARAM_ORDER),
                    jsonRequest.getInt(REQ_PARAM_OUTPUTSIZE)
                ))
                .thenReturn(jsonResponse.toString());


        // when
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/markov")
                .content(jsonRequest.toString())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void shouldDoMarkovTrainingNull() throws Exception {
        // given
        JSONObject jsonRequest = new JSONObject()
                .put(REQ_PARAM_ORDER, 2)
                .put(REQ_PARAM_OUTPUTSIZE, 100)
                .put(REQ_PARAM_FILENAME, FILE_NAME);

        when(markovChainSerice.markovChainText(
                jsonRequest.getString(REQ_PARAM_FILENAME),
                jsonRequest.getInt(REQ_PARAM_ORDER),
                jsonRequest.getInt(REQ_PARAM_OUTPUTSIZE)
        ))
                .thenReturn(null);


        // when
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/markov")
                .content(jsonRequest.toString())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}
