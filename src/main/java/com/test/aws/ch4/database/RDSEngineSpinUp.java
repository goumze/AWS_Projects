package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import static com.test.aws.ch4.database.Ec2DescribeSecurityGroups.describeSecurityGroupsResponse;
import static com.test.aws.ch4.database.RDSInitializer.rdsClient;

public class RDSEngineSpinUp {



    public static void spinUpRDS(CreateDbInstanceRequest createDbInstanceRequest){

        //Initiating request
        CreateDbInstanceResponse createDbInstanceResponse = rdsClient().createDBInstance(createDbInstanceRequest);

        System.out.println("Creating "+createDbInstanceResponse.dbInstance().engine()+" Db RDS instance "+createDbInstanceResponse.dbInstance().dbName());

        //Waiting on instance creation
        rdsClient().waiter().waitUntilDBInstanceAvailable(DescribeDbInstancesRequest
                .builder()
                .dbInstanceIdentifier(createDbInstanceResponse.dbInstance().dbInstanceIdentifier())
                .build());
        //Success on db instance creation
        System.out.println("Maria Db Instance Created");

    }

    public static void main(String...args) throws IOException {

        String rds_identifier = "my-rds-db-"+System.currentTimeMillis();
        String db_name = "mytestdb";

        String user_name = "masteruser";
        String user_password = "mymasterpassw0rd1!";
        String admin_email = "myemail@myemail.com";

        //Checking for available security group ids under security group name
        //You can create multiple ids under one security group name
        describeSecurityGroupsResponse()
                .securityGroups()
                .stream()
                .forEach(index->System.out.println("Security Group id: "+index.groupId()));

        String sg_grp_id = describeSecurityGroupsResponse()
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
        System.out.println("Please select Db Engine");
        String dbEngine = new Scanner(System.in).next();

        CreateDbInstanceRequest createDbInstanceRequest = CreateDbInstanceRequest
                .builder()
                .dbName(db_name)
                .dbInstanceIdentifier(rds_identifier)
                .dbInstanceClass("db.t2.micro")
                .engine(dbEngine)
                .masterUsername(user_name)
                .masterUserPassword(user_password)
                .vpcSecurityGroupIds(sg_grp_id)
                .allocatedStorage(20)
                .tags(rdsTags)
                .publiclyAccessible(Boolean.TRUE)
                .build();

        spinUpRDS(createDbInstanceRequest);
    }
}
