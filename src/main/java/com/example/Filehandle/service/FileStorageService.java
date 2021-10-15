package com.example.Filehandle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    Path fileStoragePath;

    private String fileStorageLocation;

    public FileStorageService(@Value("${file.storage.location:temp}") String fileStorageLocation) {

        this.fileStorageLocation=fileStorageLocation;

        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        try {

            Files.createDirectories(fileStoragePath);

        } catch (IOException e) {

            throw new RuntimeException("Issue in creating file directory ");

        }
    }

    /*
    * The upload function
    * */

    public String storeFile(MultipartFile multipartFile) {

        String fileName= StringUtils.cleanPath( multipartFile.getOriginalFilename());

        Path filepath=Paths.get(fileStoragePath+"\\"+fileName);

        try {

            Files.copy(multipartFile.getInputStream(),filepath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {

            throw new RuntimeException("Issue in  storing  the file ");

        }


        return fileName;
    }

    /*
    * The Download function ----
    * */

    public Resource downloadFile(String fileName) {

       Path path= Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);

        Resource resource;
        try {
             resource=new UrlResource(path.toUri());

        } catch (MalformedURLException e) {

            throw new RuntimeException("Issue in  reading  the file ",e);

        }

        // chek is the resource is readable or not
        if(resource.exists() && resource.isReadable()){

            return resource;

        }else{

            throw new RuntimeException("The file does not exist or not readable ");

        }


    }
}
