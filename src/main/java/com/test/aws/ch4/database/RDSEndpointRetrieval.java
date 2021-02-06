package com.test.aws.ch4.database;

import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;

import java.util.List;

import static com.test.aws.ch4.database.RDSInitializer.rdsClient;

public class RDSEndpointRetrieval {

    public static void main(String...args){

        //Describe request for Rds instance identifier
        DescribeDbInstancesRequest describeDbInstancesRequest = DescribeDbInstancesRequest
                .builder()
                //.dbInstanceIdentifier("my-rds-db-1612449667562")
                .build();

        //A more specific way of retrieving the db instances from the account
        getdbInstance(rdsClient(),describeDbInstancesRequest)
                .stream()
                .forEach(System.out::println);

    }

    public static List<DBInstance> getdbInstance(RdsClient rdsClient, DescribeDbInstancesRequest describeDbInstancesRequest) {
            //A more specific way of retrieving the db instances from the account
            return rdsClient.describeDBInstances(describeDbInstancesRequest).dbInstances();
    }
}
