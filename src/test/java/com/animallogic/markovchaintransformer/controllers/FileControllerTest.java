package com.animallogic.markovchaintransformer.controllers;

import com.animallogic.markovchaintransformer.services.FileStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FileControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private FileController controller;

    private String testFilesDir;
    private String FILE_NAME = "test.txt";

    @Before
    public void init() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();

        testFilesDir = FileControllerTest.class.getClassLoader().getResource("./static/").getPath();
    }

    @Test
    public void shouldLoadContext() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void shouldUploadFileSucceed() throws Exception {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "test simple data for upload".getBytes());
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn(FILE_NAME);


        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/file/upload")
                        .file(mockMultipartFile);

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldUploadFileCrossOriginSucceed() throws Exception {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "test simple data for upload".getBytes());
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn(FILE_NAME);


        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/file/upload")
                        .file(mockMultipartFile)
                        .header("Origin", "http://localhost:3000");

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldUploadFileCrossOriginFailed() throws Exception {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "test simple data for upload".getBytes());
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn(FILE_NAME);


        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/file/upload")
                        .file(mockMultipartFile)
                        .header("Origin", "http://stackoverflow.com/");

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldUploadFileReturnDownloadUri() throws Exception {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "test simple data for upload".getBytes());
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn(FILE_NAME);


        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/file/upload")
                        .file(mockMultipartFile);

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("http://localhost/file/download/"+FILE_NAME)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldUploadFileReturnJsonUTF8() throws Exception {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "test simple data for upload".getBytes());
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn(FILE_NAME);


        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/file/upload")
                        .file(mockMultipartFile);

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldDownloadFileSucceed() throws Exception {
        // given
        when(fileStorageService.loadFileAsResource(FILE_NAME)).thenReturn(new FileSystemResource(testFilesDir+FILE_NAME));

        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/file/download/"+FILE_NAME);

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void shouldDownloadFileCrossOriginSucceed() throws Exception {
        // given
        when(fileStorageService.loadFileAsResource(FILE_NAME)).thenReturn(new FileSystemResource(testFilesDir+FILE_NAME));

        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/file/download/"+FILE_NAME)
                        .header("Origin", "http://localhost:3000");

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void shouldDownloadFileCrossOriginFailed() throws Exception {
        // given
        when(fileStorageService.loadFileAsResource(FILE_NAME)).thenReturn(new FileSystemResource(testFilesDir+FILE_NAME));

        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/file/download/"+FILE_NAME)
                        .header("Origin", "http://www.google.com/");

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void shouldDownloadPlainTextFile() throws Exception {
        // given
        when(fileStorageService.loadFileAsResource(FILE_NAME)).thenReturn(new FileSystemResource(testFilesDir+FILE_NAME));

        // when
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/file/download/"+FILE_NAME);

        // then
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andDo(MockMvcResultHandlers.print());

    }

}
