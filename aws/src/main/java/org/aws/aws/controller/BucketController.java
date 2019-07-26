package org.aws.aws.controller;

import org.aws.aws.service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/storage/")
public class BucketController {

    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    //retourne la clé du fichier à uploader
    //public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return this.amazonClient.uploadFile(file);
        } catch (IOException ex) {
            Logger.getLogger(BucketController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @PostMapping("/downloadFile")
    @ResponseBody
    //fileName représente la clé du fichier à downloader
    public boolean downloadFile(@RequestParam("fileName") String fileName,
            @RequestParam("pathLocalFileName") String pathLocalFileName) {
        return this.amazonClient.downloadFile(fileName,pathLocalFileName);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestParam(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}