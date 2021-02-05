package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputDescription;

import java.util.List;
import java.util.Scanner;

//https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-tables.html
public class DynamoDbDescribeTable {

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

        describeDynamoDbTable(dynamoDbClient,"JavaUsers");

    }

    public static DescribeTableResponse describeDynamoDbTable(DynamoDbClient dynamoDbClient, String tableName) {

        DescribeTableRequest describeTableRequest = DescribeTableRequest
                .builder()
                .tableName(tableName)
                .build();

        DescribeTableResponse tableResponse = dynamoDbClient.describeTable(describeTableRequest);

        if (tableResponse != null) {
            System.out.format("Table name  : %s\n", tableResponse.table().tableName());
            System.out.format("Table ARN   : %s\n", tableResponse.table().tableArn());
            System.out.format("Status      : %s\n", tableResponse.table().tableStatus());
            System.out.format("Item count  : %d\n", tableResponse.table().itemCount().longValue());
            System.out.format("Size (bytes): %d\n", tableResponse.table().tableSizeBytes().longValue());
        }

        ProvisionedThroughputDescription throughputInfo = tableResponse.table().provisionedThroughput();
        System.out.println("Throughput");
        System.out.format("  Read Capacity : %d\n",throughputInfo.readCapacityUnits().longValue());
        System.out.format("  Write Capacity: %d\n",throughputInfo.writeCapacityUnits().longValue());

        List<AttributeDefinition> attributes = tableResponse.table().attributeDefinitions();
        System.out.println("Attributes");

        for (AttributeDefinition a : attributes) {
            System.out.format("  %s (%s)\n",a.attributeName(), a.attributeType());
        }

        return tableResponse;
    }
}
