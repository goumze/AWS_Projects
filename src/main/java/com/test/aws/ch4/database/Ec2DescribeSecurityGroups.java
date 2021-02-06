package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;

import static com.test.aws.ch4.database.RDSInitializer.rdsClient;

public class Ec2DescribeSecurityGroups {
    public static DescribeSecurityGroupsResponse describeSecurityGroupsResponse(){

        Ec2Client ec2Client = Ec2Client
                .builder()
                .region(Region.of(rdsClient().describeSourceRegions().sourceRegions().stream().findFirst().get().regionName()))
                .build();

        //List out available security groups
        DescribeSecurityGroupsRequest describeSecurityGroupsRequest =
                DescribeSecurityGroupsRequest
                        .builder()
                        .build();

        DescribeSecurityGroupsResponse describeSecurityGroupsResponse = ec2Client.describeSecurityGroups(describeSecurityGroupsRequest);
        return describeSecurityGroupsResponse;
    }

    public static DescribeSecurityGroupsResponse describeSecurityGroupsResponse(String securityGroupName){

        Ec2Client ec2Client = Ec2Client
                .builder()
                .region(Region.of(rdsClient().describeSourceRegions().sourceRegions().stream().findFirst().get().regionName()))
                .build();

        //List out available security groups
        DescribeSecurityGroupsRequest describeSecurityGroupsRequest =
                DescribeSecurityGroupsRequest
                        .builder()
                        .groupNames(securityGroupName)
                        .build();

        DescribeSecurityGroupsResponse describeSecurityGroupsResponse = ec2Client.describeSecurityGroups(describeSecurityGroupsRequest);
        return describeSecurityGroupsResponse;
    }

    public static DescribeSecurityGroupsResponse describeSecurityGroupsResponse(String securityGroupName, String groupId){

        Ec2Client ec2Client = Ec2Client
                .builder()
                .region(Region.of(rdsClient().describeSourceRegions().sourceRegions().stream().findFirst().get().regionName()))
                .build();

        //List out available security groups
        DescribeSecurityGroupsRequest describeSecurityGroupsRequest =
                DescribeSecurityGroupsRequest
                        .builder()
                        .groupNames(securityGroupName)
                        .groupIds(groupId)
                        .build();

        DescribeSecurityGroupsResponse describeSecurityGroupsResponse = ec2Client.describeSecurityGroups(describeSecurityGroupsRequest);
        return describeSecurityGroupsResponse;
    }
}
