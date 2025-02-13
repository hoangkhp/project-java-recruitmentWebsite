package vn.hoidanit.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.jobhunter.domain.response.file.ResponseUploadFileDTO;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;
    @Value("${hoidanit.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload Single File")
    public ResponseEntity<ResponseUploadFileDTO> upload(
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam("folder") String folder) 
        throws URISyntaxException, IOException, StorageException{
        //validate
            if(file == null || file.isEmpty()){
                throw new StorageException("File is empty, please upload a file!!!");
            }

            String fileName = file.getOriginalFilename();
            List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
            boolean isValid = allowedExtensions.stream().anyMatch(item-> fileName.toLowerCase().endsWith(item));


            if(!isValid){
                throw new StorageException("Invalid Extension" + allowedExtensions.toString());
            }
        //create a directory if not exist
        this.fileService.createUploadFolder(baseURI + folder);
        //Store file
        String uploadFile = this.fileService.store(file, folder);
        ResponseUploadFileDTO res = new ResponseUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.ok().body(res);
    }
}
