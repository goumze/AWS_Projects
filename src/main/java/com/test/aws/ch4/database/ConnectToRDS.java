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
                .dbInstanceIdentifier("my-rds-db-1612150565532")
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

    public static void connectToDb(DBInstance dbInstance){

        String sg_name = "rds-sg-dev-demo-3";
        String rds_identifier = "my-rds-db-"+System.currentTimeMillis();
        String db_name = "mytestdb";

        String user_name = "masteruser";
        String user_password = "mymasterpassw0rd1!";
        String admin_email = "myemail@myemail.com";

        String JDBC_Driver = "org.mariadb.jdbc.Driver";
        String DB_URL = "jdbc:mariadb://"+dbInstance.endpoint().address()+":"+dbInstance.endpoint().port();

        Connection conn = null;
        Statement stmt = null;
        //String masterServer = "";
        //String slaveServer = "";
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_Driver);

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, user_name, user_password);
            System.out.println("Connected successfully to database...");

            //STEP 4: Execute a query
           // System.out.println("Creating table in a given database...");
           // stmt = conn.createStatement();

            /*String sql = "CREATE TABLE VENDORS "
                    + "(id INTEGER not NULL, "
                    + " name VARCHAR(255), "
                    + " CEO VARCHAR(255), "
                    + " VAT_Number INTEGER, "
                    + " PRIMARY KEY ( id ))";*/

            //stmt.executeUpdate(sql);
            //System.out.println("Created table in a given database...");
            // stmt.executeUpdate(sql);
            //  System.out.println("Deleted table in a given database...");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }
}
