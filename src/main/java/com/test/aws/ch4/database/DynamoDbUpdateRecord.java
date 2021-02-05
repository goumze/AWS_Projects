package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.test.aws.ch4.database.DynamoDbDescribeTable.describeDynamoDbTable;
import static com.test.aws.ch4.database.DynamoDbListAllTables.listTablesResponse;

public class DynamoDbUpdateRecord {

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

        //Update record
        updateTableItem(dynamoDbClient,describeTableResponse.table().tableName(),first_attribute,"1612500574100");
    }

    public static void updateTableItem(DynamoDbClient dynamoDbClient, String tableName, String key, String keyVal){

        HashMap<String,AttributeValue> itemKey = new HashMap<>();
        itemKey.put(key, AttributeValue.builder().s(keyVal).build());

        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();

        // Update the column specified by name with updatedVal
        updatedValues.put(key, AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s("Hello Test").build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest updateItemRequest = UpdateItemRequest
                .builder()
                .tableName(tableName)
                .key(itemKey)
                .attributeUpdates(updatedValues)
                .build();

        dynamoDbClient.updateItem(updateItemRequest);
    }
}
