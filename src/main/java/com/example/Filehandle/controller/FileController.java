package com.example.Filehandle.controller;

import com.example.Filehandle.dto.FileUploadResponse;
import com.example.Filehandle.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FileController {

    private FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {

        this.fileStorageService = fileStorageService;

    }

    @PostMapping("/single/upload")
 public FileUploadResponse singleFileupload(@RequestParam("file")MultipartFile multipartFile){

      String  fileName=  fileStorageService.storeFile(multipartFile);

      String url= ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/download/")
              .path(fileName)
              .toUriString();

      String contentType=multipartFile.getContentType();

      FileUploadResponse response=new FileUploadResponse(fileName ,contentType, url);

      return response;


    }


    // download file

    @GetMapping("/download/{fileName}")
     ResponseEntity<Resource>   downloadFile(@PathVariable String fileName){

        Resource  resource = fileStorageService.downloadFile(fileName);

       MediaType contentType= MediaType.IMAGE_JPEG;  // -- image ---

        return ResponseEntity.ok()
               .contentType(contentType)
//               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName"+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName"+resource.getFilename())
               .body(resource);

    }
}
