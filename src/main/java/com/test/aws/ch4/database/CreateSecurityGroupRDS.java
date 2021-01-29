package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Scanner;

/**
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-ec2-security-groups.html
 */
public class CreateSecurityGroupRDS {

    public static void main(String...args){

        //Create ec2 instance
        Ec2Client ec2Client = Ec2Client
                .builder()
                .region(Region.AP_SOUTH_1)
                .build();


        String groupName = "rds-sg-dev-demo";
        //String groupDesc = "RDS Security Group for AWS Dev Study Guide";
        //System.out.println("Please enter the vpc id");
        //String vpcId = new Scanner(System.in).next();

        //List out available security groups
        DescribeSecurityGroupsRequest describeSecurityGroupsRequest =
                DescribeSecurityGroupsRequest
                        .builder()
                        //.groupNames(groupName)
                        //.groupIds(groupName)
                        .build();

        DescribeSecurityGroupsResponse describeSecurityGroupsResponse = ec2Client.describeSecurityGroups(describeSecurityGroupsRequest);

        describeSecurityGroupsResponse
                .securityGroups()
                .stream()
                .forEach(index->System.out.printf("Found Security Group with id %s, " +
                                "vpc id %s " +
                                "and description %s",
                        index.groupId(),
                        index.vpcId(),
                        index.description()));

        //Define IP Range
        /*IpRange ipRange = IpRange
                .builder()
                .cidrIp("0.0.0.0/0")
                .build();*/

        //Define IP Permissions
        /*IpPermission ipPermission = IpPermission
                .builder()
                .ipProtocol("tcp")
                .toPort(3306)
                .fromPort(3306)
                .ipRanges(ipRange)
                .build();*/


        //Authorize Security Group request from the ip address

        /*AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest
                = AuthorizeSecurityGroupIngressRequest
                .builder()
                .groupName(groupName)
                .ipPermissions(ipPermission)
                .build();*/


        /*CreateSecurityGroupRequest createSecurityGroupRequest =
                CreateSecurityGroupRequest
                        .builder()
                        .groupName(groupName)
                        .description(groupDesc)
                        .vpcId(vpcId)
                        .build();*/

        //CreateSecurityGroupResponse resp= ec2Client.createSecurityGroup(createSecurityGroupRequest);
    }
}
