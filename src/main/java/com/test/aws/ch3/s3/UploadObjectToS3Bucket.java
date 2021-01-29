package com.test.aws.ch3.s3;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class UploadObjectToS3Bucket {
    public static void main(String...args){

        String testStringDataKey = "TestString_"+System.currentTimeMillis();
        String testString = "Test_"+testStringDataKey.split("_")[1];
        //String fileName = "*** Path to file to upload ***";

        S3Client s3Client = S3Client
                .builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.AP_SOUTH_1)
                .build();

        String bucketName = s3Client.listBuckets().buckets().stream().findFirst().get().name();

        System.out.println("Bucket Name is: "+bucketName);

        PutObjectRequest putObjectRequest = PutObjectRequest
                .builder()
                .bucket(bucketName)
                .key(testStringDataKey)
                .build();

        // Put Object
        /**
         * https://stackabuse.com/aws-s3-with-java-uploading-files-creating-and-deleting-s3-buckets/
         */
        s3Client.putObject(putObjectRequest, RequestBody.fromString(testString));
    }
}
