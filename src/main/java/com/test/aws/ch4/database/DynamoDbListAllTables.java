package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

import java.util.List;
import java.util.Scanner;

//https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-tables.html
public class DynamoDbListAllTables {

    public static void main(String...args){

        System.out.println("Set the region");
        String regionName = new Scanner(System.in).next();

        Region region = Region
                .regions()
                .stream()
                .filter(index->index.equals(Region.of(regionName.toLowerCase())))
                .findFirst()
                .get();

        DynamoDbClient dynamoDbClient = DynamoDbClient
                .builder()
                .region(region)
                .build();

        /*ListTablesRequest listTableRequest = ListTablesRequest
                .builder()
                .exclusiveStartTableName("JavaUsers")
                .build();*/

        ListTablesResponse listTablesResponse = dynamoDbClient.listTables();

        listTablesResponse
                .tableNames()
                .stream()
                .forEach(System.out::println);
    }

    public static List<String> listTablesResponse(DynamoDbClient dynamoDbClient){
        return dynamoDbClient.listTables().tableNames();
    }
}
