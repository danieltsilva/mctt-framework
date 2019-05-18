package com.animallogic.markovchaintransformer.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FileControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private FileController controller = new FileController();

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Before
    public void init() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void uploadFile() throws Exception {
        // given
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        String fileName = "test.txt";
        File file = new File(uploadDir + "/" + fileName);
        file.delete(); //delete if file exits

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",fileName,
                "application/json", "test simple data for upload OTO".getBytes());

        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/file/upload")
                .file(mockMultipartFile);

        // then
        this.mockMvc.perform(builder).andExpect(ok)
                .andDo(MockMvcResultHandlers.print());

        Assert.assertTrue(file.exists());
    }

    @Test
    public void downloadFile() throws Exception {
        // given
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher contentType = MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN);

        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/file/download/test.txt");

        // then
        this.mockMvc.perform(builder)
                .andExpect(ok)
                .andExpect(contentType)
                .andDo(MockMvcResultHandlers.print());

    }

}
