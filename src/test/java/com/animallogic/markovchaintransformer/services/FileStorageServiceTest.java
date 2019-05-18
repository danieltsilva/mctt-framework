package com.animallogic.markovchaintransformer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest
public class FileStorageServiceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileStorageService fileStorageService;

//    @Test
//    public void shouldListAllFiles() throws Exception {
//        given(this.fileStorageService.loadAll())
//                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));
//
//        this.mvc.perform(get("/")).andExpect(status().isOk())
//                .andExpect(model().attribute("files",
//                        Matchers.contains("http://localhost/files/first.txt",
//                                "http://localhost/files/second.txt")));
//    }

//    @Test
//    public void shouldSaveUploadedFile() throws Exception {
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
//                "text/plain", "Spring Framework".getBytes());
//        this.mvc.perform(fileUpload("/").file(multipartFile))
//                .andExpect(status().isFound())
//                .andExpect(header().string("Location", "/"));
//
//        then(this.fileStorageService).should().storeFile(multipartFile);
//    }

//    @SuppressWarnings("unchecked")
//    @Test
//    public void should404WhenMissingFile() throws Exception {
//        given(this.fileStorageService.loadFileAsResource("test.txt"))
//                .willThrow(StorageFileNotFoundException.class);
//
//        this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
//    }

}