package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.Scanner;

import static com.test.aws.ch4.database.Ec2DescribeSecurityGroups.describeSecurityGroupsResponse;
import static com.test.aws.ch4.database.RDSInitializer.rdsClient;

/**
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-ec2-security-groups.html
 */
public class RDSCreateSecurityGroup {

    public static CreateSecurityGroupResponse createSecurityGroupResponse(String vpcId,String securityGroupName, String securityGroupDescription){

        DescribeSecurityGroupsResponse describeSecurityGroupsResponse = describeSecurityGroupsResponse(securityGroupName);

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
        IpRange ipRange = IpRange
                .builder()
                .cidrIp("0.0.0.0/0")
                .build();

        //Define IP Permissions
        IpPermission ipPermission = IpPermission
                .builder()
                .ipProtocol("tcp")
                .toPort(3306)
                .fromPort(3306)
                .ipRanges(ipRange)
                .build();


        //Authorize Security Group request from the ip address

        AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest
                = AuthorizeSecurityGroupIngressRequest
                .builder()
                .groupName(securityGroupName)
                .ipPermissions(ipPermission)
                .build();


        CreateSecurityGroupRequest createSecurityGroupRequest =
                CreateSecurityGroupRequest
                        .builder()
                        .groupName(securityGroupName)
                        .description(securityGroupDescription)
                        .vpcId(vpcId)
                        .build();

        Ec2Client ec2Client = Ec2Client
                .builder()
                .region(Region.of(rdsClient().describeSourceRegions().sourceRegions().stream().findFirst().get().regionName()))
                .build();

        CreateSecurityGroupResponse createSecurityGroupResponse= ec2Client.createSecurityGroup(createSecurityGroupRequest);

        System.out.println("Response after security group creation");
        System.out.println(createSecurityGroupResponse);
        return createSecurityGroupResponse;
    }

    public static void main(String...args){
        //System.out.println("ThreadLocalRandom.current().nextInt(1,11): "+ThreadLocalRandom.current().nextInt(1,Integer.MAX_VALUE));
        //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
        PrimitiveIterator.OfInt randomIterator = new Random().ints(1,Integer.MAX_VALUE).iterator();
        //System.out.println("randomIterator: "+randomIterator.nextInt());
        String groupName = "rds-sg-dev-demo-"+ randomIterator.nextInt();
        String groupDesc = "RDS Security Group for AWS Dev Study Guide";
        System.out.println("Please enter the vpc id");
        String vpcId = new Scanner(System.in).next();
        createSecurityGroupResponse(vpcId,groupName, groupDesc);
    }
}
