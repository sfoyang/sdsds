package org.aws.aws.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    /*
     * @Value("${amazonProperties.endpointUrl}") private String endpointUrl;
     * 
     * @Value("${amazonProperties.bucketName}") private String bucketName;
     * 
     * @Value("${amazonProperties.accessKey}") private String accessKey;
     * 
     * @Value("${amazonProperties.secretKey}") private String secretKey;
     */
    //private String endpointUrl="https://ponga1.s3.amazonaws.com";
    private String endpointUrl = "https://s3.us-east-1.amazonaws.com";
    private String bucketName = "ponga1";
    private String accessKey =  "AKIAIGTRKY2SJBRME6VA";                      //"AKIARF3D5ANGAQFC7XU5";
    private String secretKey =  "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"; //"cmnaW2diKJSXJaElJPRmK4wa4MnH5h/GxwM2vN7x";

    @PostConstruct    
    private void initializeAmazon() {

        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1")
                .build(); 
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) throws AmazonServiceException {
        /*s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
         .withCannedAcl(CannedAccessControlList.PublicRead));*/

        try {
            s3client.putObject(new PutObjectRequest(bucketName, "willyk/faces/"+fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            //System.exit(1);
            throw new AmazonServiceException(e.getErrorMessage());
        }
        System.out.println("Done!");

    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {

        try {
            String fileUrl = "";
            
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            //fileUrl =  bucketName + "/" + fileName;
            //fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            //fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            fileUrl = fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
            
            return fileUrl;
        } catch (IOException ex) {
            Logger.getLogger(AmazonClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        }
    }

    /*
     private S3Object downloadFileFroms3bucket(String fileName) {
     s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
     .withCannedAcl(CannedAccessControlList.PublicRead));*/
    /*S3Object object=null;
     try {
     System.out.println("Done!");
     object = s3client.getObject(bucketName, fileName);
    	 
     } catch (AmazonServiceException e) {
     System.err.println(e.getErrorMessage());
     System.exit(1);
     }
     return object;
     }	*/

    /*public boolean downloadFile(String fileName) {

     S3Object object=null;
     try {

     object=downloadFileFroms3bucket(fileName);
    
     } catch (Exception e) {
     e.printStackTrace();
     }
     return object;
     }*/
    public boolean downloadFile(String fileName, String pathLocalFileName) {

        boolean success = false;
        try {
            System.out.println("Done!");
            //This is where the downloaded file will be saved
            File localFile = new File(pathLocalFileName);

            //This returns an ObjectMetadata file but you don't have to use this if you don't want 
            s3client.getObject(new GetObjectRequest(bucketName, fileName), localFile);

            //Now your file will have your image saved 
            success = localFile.exists() && localFile.canRead();

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        return success;
    }

    /**
     * 
     * @param fileUrl example willyk/faces/1560266394579-UNICEF.png
     * @return 
     */
    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl; 
        
        System.out.println(bucketName+"  >>>>>> "+fileName);
        
        s3client.deleteObject(bucketName,fileName );
        return "Successfully deleted";
    }
    
//////////    /**
//////////     * 
//////////     * @param fileUrl example willyk/faces/1560266394579-UNICEF.png
//////////     * @return 
//////////     */
//////////    public String deleteFileFromS3Bucket(String fileUrl) {
//////////        String fileName = fileUrl; //.substring(fileUrl.lastIndexOf("/") + 1);
//////////        
//////////        System.out.println(bucketName+"  >>>>>> "+fileName);
//////////        
//////////        s3client.deleteObject(bucketName,fileName /*new DeleteObjectRequest(bucketName + "/", fileName)*/);
//////////        return "Successfully deleted";
//////////    }
}
