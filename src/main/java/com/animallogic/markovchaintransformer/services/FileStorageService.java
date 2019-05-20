package com.animallogic.markovchaintransformer.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * This service consists of methods to manage files into the filesystem.
 *
 * <p> In this first version, all the files are uploaded in file system.
 *
 * @author danieltsilva
 *
 */
public interface FileStorageService {

    /**
     * Store a file into the filesystem. The filesystem path can be configured on application.properties, file.upload-dir parameter.
     *
     * @param   file
     *          The {@code MultipartFile} representing the file to be store.
     *
     * @return  Return the file name if succeed. If not will return an empty string.
     *
     */
    public String storeFile(MultipartFile file);

    /**
     * Load a file from the filesystem as a {@code Resource}. The filesystem path can be configured on application.properties, file.upload-dir parameter.
     *
     * @param   fileName
     *          The {@code String} filename.
     *
     * @return  Return the file as a {@code Resource}.
     *
     */
    public Resource loadFileAsResource(String fileName);

    /**
     * Load a file from the filesystem as {@code byte[]}. The filesystem path can be configured on application.properties, file.upload-dir parameter.
     *
     * @param   fileName
     *          The {@code String} filename.
     *
     * @return  Return the file as an array of bytes.
     *
     */
    public byte[] loadFileAsBytes(String fileName);

}
