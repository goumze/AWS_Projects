package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.CreateDbInstanceRequest;
import software.amazon.awssdk.services.rds.model.CreateDbInstanceResponse;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;
import software.amazon.awssdk.services.rds.model.Tag;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class SpinUpMariaDb {

    public static void main(String...args) throws IOException {

        String sg_name = "rds-sg-dev-demo-3";
        String rds_identifier = "my-rds-db-2";
        String db_name = "mytestdb";

        String user_name = "masteruser";
        String user_password = "mymasterpassw0rd1!";
        String admin_email = "myemail@myemail.com";
        String sg_id_number = "";
        String rds_endpoint = "";

        //List out available security groups
        DescribeSecurityGroupsRequest describeSecurityGroupsRequest =
                DescribeSecurityGroupsRequest
                        .builder()
                        .groupNames(sg_name)
                        .build();

        System.out.println("Set the region");
        String regionName = new Scanner(System.in).next();

        Region region = Region.regions().stream().filter(index->index.equals(Region.of(regionName.toLowerCase()))).findFirst().get();

        System.out.println("Region set is: "+region.id());

        //Set Ec2Client
        Ec2Client ec2Client =
                Ec2Client.builder()
                        .region(region)
                        .build();

        DescribeSecurityGroupsResponse describeSecurityGroupsResponse = ec2Client.describeSecurityGroups(describeSecurityGroupsRequest);

        //Checking for available security group ids under security group name
        //You can create multiple ids under one security group name

        describeSecurityGroupsResponse
                .securityGroups()
                .stream()
                .forEach(index->System.out.println("Security Group id: "+index.groupId()));

        String sg_grp_id = describeSecurityGroupsResponse
                .securityGroups()
                .stream()
                .findFirst()
                .get()
                .groupId();


        //Create Rds Client instance
        RdsClient rdsClient = RdsClient
                .builder()
                .region(Region.AP_SOUTH_1)
                .build();

        //Setting tags for RDS instance (Optional)
        Tag rdsTags = Tag.builder()
                .key("POC-Email")
                .value(admin_email)
                .key("Purpose")
                .value("AWS Developer Study Guide Demo")
                .build();

        //Prepping the request
        CreateDbInstanceRequest createDbInstanceRequest = CreateDbInstanceRequest
                .builder()
                .dbName(db_name)
                .dbInstanceIdentifier(rds_identifier)
                .dbInstanceClass("db.t2.micro")
                .engine("mariadb")
                .masterUsername(user_name)
                .masterUserPassword(user_password)
                .vpcSecurityGroupIds(sg_grp_id)
                .allocatedStorage(20)
                .tags(rdsTags)
                .build();

        //Initiating request
        CreateDbInstanceResponse createDbInstanceResponse = rdsClient.createDBInstance(createDbInstanceRequest);

        System.out.println("Creating Maria Db RDS instance "+createDbInstanceResponse.dbInstance().dbName());

        //Waiting on instance creation
        rdsClient.waiter().waitUntilDBInstanceAvailable(DescribeDbInstancesRequest
                .builder()
                .dbInstanceIdentifier(rds_identifier)
                .build());

        //Success on db instance creation
        System.out.println("Maria Db Instance Created");

    }
}
