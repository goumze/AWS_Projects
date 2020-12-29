package com.test.aws.java;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class CreateS3Bucket {
    public static void main(String... args) {
        String clientRegion = "ap-south-1";

        //Since bucket name should be unique, hence the code below:
        String bucketName = "bucket." + System.currentTimeMillis();

        /**
         * Version with SDK 1.x versions
         * AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
         .withCredentials(new ProfileCredentialsProvider())
         .withRegion(clientRegion)
         .build();
         **//*

        /**
            Version with sdk 2.x versions
         **/

        S3Client s3Client = S3Client
                .builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.AF_SOUTH_1)
                .build();

        if (!s3Client.listBuckets().buckets().stream().anyMatch(index -> index.name().equals(bucketName))) {
        }
    }
}