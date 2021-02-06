package com.test.aws.ch4.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;

import java.util.Scanner;

public class RDSInitializer {

    public static RdsClient rdsClient() {

        System.out.println("Set the region");
        String regionName = new Scanner(System.in).next();
        Region region = Region
                .regions()
                .stream()
                .filter(index -> index.equals(Region.of(regionName.toLowerCase())))
                .findFirst()
                .get();

        //Create Rds client instance
        RdsClient rdsClient = RdsClient
                .builder()
                .region(region)
                .build();

        return rdsClient;
    }

}
