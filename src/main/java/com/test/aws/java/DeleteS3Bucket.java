package com.test.aws.java;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;

public class DeleteS3Bucket {

    public static void main(String...args){

        /**
         * Delete non version bucket
         */

        S3Client s3Client = S3Client
                .builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.AP_SOUTH_1)
                .build();

        String bucketName = s3Client.listBuckets().buckets().stream().findFirst().get().name();

        ListObjectsRequest listObjectsRequest = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();

        String objectKey = s3Client.listObjects(listObjectsRequest).contents().stream().findFirst().get().key();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        System.out.println("Requested Object Key to delete: "+deleteObjectRequest.key());
        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);

        System.out.println("Response after delete object operation");
        System.out.println(deleteObjectResponse.toString());

        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest
                .builder()
                .bucket(bucketName)
                .build();

        System.out.println("Requested Bucket to delete: "+deleteBucketRequest.bucket());
        DeleteBucketResponse deleteBucketResponse = s3Client.deleteBucket(deleteBucketRequest);

        System.out.println("Response after delete bucket operation");
        System.out.println(deleteBucketResponse.toString());
    }
}
