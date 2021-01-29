package com.test.aws.ch3.s3;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest;
import software.amazon.awssdk.services.s3.model.GetBucketLocationResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

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
                /**More specific settings when choosing for credentials provider
                 * https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
                 */
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.AP_SOUTH_1)
                .build();

        /**
         * This method doesBucketExistV2("") is not present in latest v2 sdk.
         * if(!s3Client.doesBucketExistV2(bucketName))
         */
        if (s3Client.listBuckets().buckets().stream().anyMatch(index -> index.name().equals(bucketName))) {
            throw S3Exception.create("Bucket Exists",null);
        }else{
            System.out.println("This is a new bucket");

            CreateBucketRequest bucketRequest = CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            //Here bucket is created
            /**
             * In SDK v1, CreateBucketRequest used could be instantiated using new but now it's final in v2.
             *s3Client.createBucket(new CreateBucketRequest(bucketName));
             */

            s3Client.createBucket(bucketRequest);

            //Verifying bucket location
            GetBucketLocationRequest getBucketLocationRequest = GetBucketLocationRequest.builder().bucket(bucketName).build();
            GetBucketLocationResponse s3BucketLocation = s3Client.getBucketLocation(getBucketLocationRequest);
            System.out.println("Bucket location is: "+s3BucketLocation.toString());
        }
    }

}