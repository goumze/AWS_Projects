package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import static com.test.aws.ch4.database.RDSEndpointRetrieval.getdbInstance;

public class ConnectToRDS {

    public static void main(String...args) throws SQLException, ClassNotFoundException {
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
                .dbInstanceIdentifier("my-rds-db-1612449667562")
                .build();

        //Create Rds client instance
        RdsClient rdsClient = RdsClient
                .builder()
                .region(region)
                .build();

        List<DBInstance> dbInstanceList = getdbInstance(rdsClient,describeDbInstancesRequest);
        dbInstanceList.stream().forEach(System.out::println);

        connectToDb(dbInstanceList.get(0));
    }

    /**
     * Based on
     * https://aws.amazon.com/blogs/database/using-the-mariadb-jdbc-driver-with-amazon-aurora-with-mysql-compatibility/
     * https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.Connecting.Java.html
     * https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/java-rds.html
     */
    public static void connectToDb(DBInstance dbInstance) throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        String dbName = "mytestdb";
        String userName = "masteruser";
        String password = "mymasterpassw0rd1!";
        String hostname = dbInstance.endpoint().address();
        String port = String.valueOf(dbInstance.endpoint().port());

        String jdbcUrl = "jdbc:mariadb://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
        Connection con = DriverManager.getConnection(jdbcUrl);
        System.out.println("Connected successfully to database...");

    }
}
