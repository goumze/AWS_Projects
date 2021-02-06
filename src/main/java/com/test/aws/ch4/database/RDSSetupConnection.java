package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static com.test.aws.ch4.database.RDSEndpointRetrieval.getdbInstance;
import static com.test.aws.ch4.database.RDSInitializer.rdsClient;

/**
 * Based on
 * https://aws.amazon.com/blogs/database/using-the-mariadb-jdbc-driver-with-amazon-aurora-with-mysql-compatibility/
 * https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.Connecting.Java.html
 * https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/java-rds.html
 * https://www.youtube.com/watch?v=HfukksuHU6Q&t=425s
 */

public class RDSSetupConnection {

    public static Connection connectToDb(DBInstance dbInstance,String userName,String password) throws ClassNotFoundException, SQLException {
        String hostname = dbInstance.endpoint().address();
        String port = String.valueOf(dbInstance.endpoint().port());
        String jdbcUrl ="";
        switch (dbInstance.engine()){
            case "oracle-se1":
                jdbcUrl  = "jdbc:oracle:thin:@" + hostname + ":" + port + ":"+dbInstance.dbName().toLowerCase();
                break;
            default:
                jdbcUrl ="";
        }
        //String jdbcUrl = "jdbc:oracle:thin:@" + hostname + ":" + port + ":"+dbInstance.dbName().toLowerCase();
        Connection connection = DriverManager.getConnection(jdbcUrl,userName,password);
        System.out.println("Connected successfully to database...");
        return connection;
    }

    public static void main(String...args) throws SQLException, ClassNotFoundException {

        //Describe request for Rds instance identifier
        DescribeDbInstancesRequest describeDbInstancesRequest = DescribeDbInstancesRequest
                .builder()
                .build();

        List<DBInstance> dbInstanceList = getdbInstance(rdsClient(),describeDbInstancesRequest);
        dbInstanceList.stream().forEach(System.out::println);

        connectToDb(dbInstanceList.get(0),"admin","mymasterpassw0rd1!");
    }
}
