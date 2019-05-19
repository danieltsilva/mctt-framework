package com.animallogic.markovchaintransformer.services;

import com.animallogic.markovchaintransformer.controllers.FileControllerTest;
import com.animallogic.markovchaintransformer.services.exceptions.MyFileNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FileStorageServiceTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    UrlResource UrlResource;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

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
        assertThat(fileStorageService).isNotNull();
    }

    @Test
    public void shouldStoreUploadedFile() throws Exception {
        // given
        File file = new File(uploadDir + FILE_NAME);
        file.delete(); //delete if file exits

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "Sample file for tests".getBytes());

        // when
        String fileName = fileStorageService.storeFile(mockMultipartFile);

        // then
        Assert.assertEquals(FILE_NAME,fileName);
        Assert.assertTrue(file.exists());
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotStoreNullFile() {
        // given
        File file = new File(uploadDir + FILE_NAME);
        file.delete(); //delete if file exits

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "Sample file for tests".getBytes());

        // when
        String fileName = fileStorageService.storeFile(null);
    }

    @Test
    public void shouldLoadFileAsResource() {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",FILE_NAME,
                "application/json", "Sample file for tests".getBytes());
        fileStorageService.storeFile(mockMultipartFile);

        // when
        Resource resource = fileStorageService.loadFileAsResource(FILE_NAME);

        // then
        Assert.assertTrue(resource.exists());
    }

    @Test( expected = NullPointerException.class)
    public void shouldNotLoadNullFileAsResource() {
        // given
        File file = new File(uploadDir + FILE_NAME);
        file.delete(); //delete if file exits

        // when
        Resource resource = fileStorageService.loadFileAsResource(null);
    }

    @Test( expected = MyFileNotFoundException.class)
    public void shouldNotFindLoadFileAsResource() {
        // given
        File file = new File(uploadDir + FILE_NAME);
        file.delete(); //delete if file exits

        // when
        Resource resource = fileStorageService.loadFileAsResource(FILE_NAME);
    }

}