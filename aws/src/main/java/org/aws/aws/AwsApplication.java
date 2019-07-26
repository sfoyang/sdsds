package org.aws.aws;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@SpringBootApplication
public class AwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsApplication.class, args);
		  
			/*AWSCredentials credentials = new BasicAWSCredentials(
					  "", 
					  ""
					);
			AmazonS3 s3client = AmazonS3ClientBuilder
					  .standard()
					  .withCredentials(new AWSStaticCredentialsProvider(credentials))
					  .withRegion("us-east-1")
					  .build();
			String bucketName = "ponga1";
			String file_path = "/home/sfoyang/test.xt";
			
			
			String key_name = Paths.get(file_path).getFileName().toString();
	
	        System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucketName);
	        
	        try {
	        	s3client.putObject(bucketName, key_name, new File(file_path));
	        } catch (AmazonServiceException e) {
	            System.err.println(e.getErrorMessage());
	            System.exit(1);
	        }
	        System.out.println("Done!");*/
		}
	
	

	
	}


