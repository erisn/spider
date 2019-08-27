package com.acuity.rdso.sbe_user.services;

import java.io.ByteArrayInputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class AmazonS3Service {
	
	  @Value("${wikiscrapper.aws.accesskey}")
	  private String accessKey;
	  
	  @Value("${wikiscrapper.aws.secretkey}")
	  private String secretKey;
	  
	  private AmazonS3 s3Client;
	
	  /**
	   * Post construct to initialize the s3 client
	   */
	@PostConstruct
	public void init(){
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_EAST_2)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.build();
	}
	
	/**
	 * Method to store the scrapped data in s3
	 * @param scrappedData
	 * @param bucketName
	 * @param key
	 */
	public void store(String scrappedData, String bucketName, String key) {
		try {
			ByteArrayInputStream input = new ByteArrayInputStream(scrappedData.getBytes());
			s3Client.putObject(bucketName, key, input, new ObjectMetadata());
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
