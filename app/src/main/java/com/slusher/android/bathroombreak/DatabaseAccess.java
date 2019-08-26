package com.slusher.android.bathroombreak;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.ScanOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private static String TAG = "DynamoDB";

    private final String COGNITO_IDENTITY_POOL_ID = "us-west-2:98409cc6-f48c-4ce6-b531-ecb36e16b000";
    private final Regions COGNITO_IDENTITY_POOL_REGION = Regions.US_WEST_2;
    private final String DYNAMODB_TABLE = "Bathrooms";
    private Context context;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;

    private static volatile DatabaseAccess instance;

    private DatabaseAccess(Context context)
    {
        this.context = context;

        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                COGNITO_IDENTITY_POOL_ID, // Identity pool ID
                COGNITO_IDENTITY_POOL_REGION // Region
        );

        dbClient = new AmazonDynamoDBClient(credentialsProvider);

        dbClient.setRegion(Region.getRegion(COGNITO_IDENTITY_POOL_REGION));
        dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
    }

    public static synchronized DatabaseAccess getInstance(Context context)
    {
        if(instance == null){
            try {
                instance = new DatabaseAccess(context);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }

        return instance;
    }

    public List<Document> getAllBathrooms(){
        ScanOperationConfig scanConfig = new ScanOperationConfig();
        List<String> attributeList = new ArrayList<>();
        attributeList.add("id");
        attributeList.add("address");
        attributeList.add("reference");
        attributeList.add("name");
        attributeList.add("latitude");
        attributeList.add("longitude");
        scanConfig.withAttributesToGet(attributeList);
        Search searchResult = dbTable.scan(scanConfig);

        return searchResult.getAllResults();
    }

    public Boolean addBathroom(Bathroom bathroom){
        Log.i(TAG, "adding bathroom...");

        Document newBathroom = new Document();

        newBathroom.put("id", bathroom.getId());
        newBathroom.put("address", bathroom.getAddress());
        newBathroom.put("reference", bathroom.getReference());
        newBathroom.put("name", bathroom.getName());
        newBathroom.put("latitude", bathroom.getLocation().latitude);
        newBathroom.put("longitude", bathroom.getLocation().longitude);

        try {
            dbTable.putItem(newBathroom);
        }catch(Exception e){
            Log.e(TAG, "Error adding bathroom");
            return false;
        }

        return true;
    }
}
