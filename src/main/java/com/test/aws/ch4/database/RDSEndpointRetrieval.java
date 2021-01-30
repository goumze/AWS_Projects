package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;

import java.util.Scanner;

public class RDSEndpointRetrieval {

    public static void main(String...args){

        System.out.println("Set the region");
        String regionName = new Scanner(System.in).next();

        Region region = Region
                .regions()
                .stream()
                .filter(index->index.equals(Region.of(regionName.toLowerCase())))
                .findFirst()
                .get();

        //Describe request for Rds instance identifier
        DescribeDbInstancesRequest describeDbInstancesRequest = DescribeDbInstancesRequest
                .builder()
                .dbInstanceIdentifier("my-rds-db-1612031535471")
                .build();

        //Create Rds client instance
        RdsClient rdsClient = RdsClient
                .builder()
                .region(region)
                .build();

        //A more generic way of retrieving the db instances from the account
        /*rdsClient.describeDBInstances()
                .dbInstances()
                .stream()
                .forEach(index-> System.out.println(index.dbInstanceIdentifier()));*/

        //A more specific way of retrieving the db instances from the account
        if(rdsClient.describeDBInstances(describeDbInstancesRequest).hasDbInstances()){
            rdsClient.describeDBInstances(describeDbInstancesRequest)
                    .dbInstances()
                    .stream()
                    .forEach(System.out::println);
        }

    }
}
