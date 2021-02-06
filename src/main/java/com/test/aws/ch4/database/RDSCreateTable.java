package com.test.aws.ch4.database;

import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.test.aws.ch4.database.RDSEndpointRetrieval.getdbInstance;
import static com.test.aws.ch4.database.RDSInitializer.rdsClient;
import static com.test.aws.ch4.database.RDSSetupConnection.connectToDb;

public class RDSCreateTable {

    public static void createTable(String query) throws SQLException, ClassNotFoundException {
        //Describe request for Rds instance identifier
        DescribeDbInstancesRequest describeDbInstancesRequest = DescribeDbInstancesRequest
                .builder()
                .build();

        List<DBInstance> dbInstanceList = getdbInstance(rdsClient(), describeDbInstancesRequest);
        dbInstanceList.stream().forEach(System.out::println);

        try (Connection connection = connectToDb(dbInstanceList.get(0), "admin", "mymasterpassw0rd1!")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
            System.out.println("Table Created......");
         }
    }

    public static void main(String...args){
       //String query = "CREATE TABLE Users (user_id INT NOT NULL AUTO_INCREMENT,"+"user_fname VARCHAR(100) NOT NULL, user_lname VARCHAR(150) NOT NULL, user_email VARCHAR(175) NOT NULL, PRIMARY KEY (user_id))";
        String query = "CREATE TABLE DISPATCHES("
                + "ProductName VARCHAR (20) NOT NULL, "
                + "CustomerName VARCHAR (20) NOT NULL, "
                + "DispatchDate date, "
                + "DeliveryTime timestamp, "
                + "Price INT, "
                + "Location varchar(20))";
        try {
            createTable(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
