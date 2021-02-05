package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.test.aws.ch4.database.DynamoDbDescribeTable.describeDynamoDbTable;
import static com.test.aws.ch4.database.DynamoDbListAllTables.listTablesResponse;

public class DynamoDbInsertRecord {

    public static void main(String...args){

        System.out.println("Set the region");
        String regionName = new Scanner(System.in).next();

        Region region = Region
                .regions()
                .stream()
                .filter(index->index.equals(Region.of(regionName.toLowerCase())))
                .findFirst()
                .get();

        //Get the client
        DynamoDbClient dynamoDbClient = DynamoDbClient
                .builder()
                .region(region)
                .build();

        //List the tables present
        List<String> listTableNamesResponseList = listTablesResponse(dynamoDbClient);
        listTableNamesResponseList.stream().forEach(System.out::println);

        DescribeTableResponse describeTableResponse = describeDynamoDbTable(dynamoDbClient, listTableNamesResponseList.stream().filter(index->index.equals("JavaUsers")).findFirst().get());
        String first_attribute = describeTableResponse.table().attributeDefinitions().stream().findFirst().get().attributeName();

        putItemInTable(dynamoDbClient,describeTableResponse.table().tableName(),first_attribute,String.valueOf(System.currentTimeMillis()));
    }

    public static void putItemInTable(DynamoDbClient dynamoDbClient,String tableName,String key,String keyVal){

        // Add all content to the table
        HashMap<String, AttributeValue> itemValues = new HashMap<String,AttributeValue>();
        itemValues.put(key, AttributeValue.builder().s(keyVal).build());

        //Create put item request for the table
        PutItemRequest request = PutItemRequest
                .builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            dynamoDbClient.putItem(request);
            System.out.println(tableName +" was successfully updated");

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }
}
