package me.simpleboard.server.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import me.simpleboard.server.payload.SignedUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

  private AmazonS3 s3client;

  @Value("${aws.bucketName}")
  private String bucketName;
  @Value("${aws.accessKey}")
  private String accessKey;
  @Value("${aws.secretKey}")
  private String secretKey;

  @PostConstruct
  private void initializeAWS() {
    AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

    this.s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build();
  }

  public SignedUrlResponse getSignedUrl(String userId, String oldKey) {
    Date expiration = new Date();
    expiration.setTime(expiration.getTime() + 1000 * 60 * 3);
    String key = oldKey != null ? oldKey : userId + "/" + UUID.randomUUID();
    String url = "";

    try {
      GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
              .withMethod(HttpMethod.PUT)
              .withContentType("image/jpeg")
              .withExpiration(expiration);
      url = s3client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    } catch (AmazonServiceException e) {
      e.printStackTrace();
    }

    return new SignedUrlResponse(url, key);
  }

  public void deleteFile(String key) {
    try {
      DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
      s3client.deleteObject(deleteObjectRequest);
    } catch (AmazonServiceException e) {
      e.printStackTrace();
    }
  }
}
