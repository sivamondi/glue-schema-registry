package com.schema.registry.service;

import com.schema.registry.model.CreateSchemaRequestBody;
import com.schema.registry.model.RegistryRequest;
import com.schema.registry.model.RegistryResponse;
import com.schema.registry.util.SchemaDefinitionValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.glue.GlueClient;
import software.amazon.awssdk.services.glue.model.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient;
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.*;


import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GlueRegistryService {
    private final GlueClient glueClient;

    private ResourceGroupsTaggingApiClient taggingClient;

    private AwsBasicCredentials awsCredentials;

    public GlueRegistryService(@Value("${aws.access.key.id}") String accessKeyId,
                               @Value("${aws.secret.access.key}") String secretAccessKey,
                               @Value("${aws.region:us-east-1}") String region) {

        // Create AWS credentials
        awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        taggingClient = ResourceGroupsTaggingApiClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region != null ? region : "us-east-1"))
                .build();

        // Build GlueClient with explicit credentials
        this.glueClient = GlueClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region != null ? region : "us-east-1"))
                .build();
    }

    public CreateRegistryResponse createRegistry(RegistryRequest registryRequest) {
        try {
            CreateRegistryRequest request = CreateRegistryRequest.builder()
                    .registryName(registryRequest.getRegistryName())
                    .description(registryRequest.getDescription())
                    .tags(registryRequest.getTags() != null ? registryRequest.getTags() : Map.of())
                    .build();
            return glueClient.createRegistry(request);
        } catch (GlueException e) {
            if (e.awsErrorDetails().errorCode().equals("AccessDeniedException")) {
                throw new RuntimeException("Access denied. Please check your AWS credentials and permissions.", e);
            } else if (e.awsErrorDetails().errorCode().equals("InvalidRequestException")) {
                throw new RuntimeException("Invalid request: " + e.getMessage(), e);
            } else {
                throw new RuntimeException("Failed to create registry: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error creating registry: " + e.getMessage(), e);
        }
    }

    public RegistryResponse findRegistryArnByTag(String tagName, String tagValue) {

        GetResourcesRequest request = GetResourcesRequest.builder()
                .tagFilters(TagFilter.builder()
                        .key(tagName)
                        .values(tagValue)
                        .build())
                .resourceTypeFilters("glue:registry")
                .build();

        GetResourcesResponse response = taggingClient.getResources(request);

        Optional<ResourceTagMapping> matchedRegistry = response.resourceTagMappingList().stream()
                .findFirst();

        if (matchedRegistry.isPresent()) {
            ResourceTagMapping tagMapping = matchedRegistry.get();
            String registryArn = tagMapping.resourceARN();

            // Get registry details
            GetRegistryRequest getRegistryRequest = GetRegistryRequest.builder()
                    .registryId(RegistryId.builder().registryArn(registryArn).build())
                    .build();

            GetRegistryResponse registryResponse = glueClient.getRegistry(getRegistryRequest);

            // Map tags from list to Map<String, String>
            Map<String, String> tagMap = tagMapping.tags().stream()
                    .collect(Collectors.toMap(Tag::key, Tag::value));

            RegistryResponse responseModel = new RegistryResponse();
            responseModel.setRegistryARN(registryResponse.registryArn());
            responseModel.setRegistryName(registryResponse.registryName());
            responseModel.setDescription(registryResponse.description());
            responseModel.setTags(tagMap);

            return responseModel;
        }

        return null;
    }

    public CreateSchemaResponse createSchema(CreateSchemaRequestBody requestBody) {
        try {
            RegistryId registryId = null;

            if (requestBody.getRegistryArn() != null && !requestBody.getRegistryArn().isBlank()) {
                registryId = RegistryId.builder()
                        .registryArn(requestBody.getRegistryArn())
                        .build();
            } else if (requestBody.getRegistryName() != null && !requestBody.getRegistryName().isBlank()) {
                registryId = RegistryId.builder()
                        .registryName(requestBody.getRegistryName())
                        .build();
            } else {
                // optional: use default registry or throw error
                throw new IllegalArgumentException("Either registryArn or registryName must be provided.");
            }


            if(SchemaDefinitionValidator.isValidSchema(requestBody)) {

                CreateSchemaRequest.Builder builder = CreateSchemaRequest.builder()
                        .schemaName(requestBody.getSchemaName())
                        .dataFormat(requestBody.getDataFormat())
                        .schemaDefinition(requestBody.getSchemaDefinition());

                if (registryId != null) builder.registryId(registryId);
                if (requestBody.getCompatibility() != null) builder.compatibility(requestBody.getCompatibility());
                if (requestBody.getDescription() != null) builder.description(requestBody.getDescription());
                if (requestBody.getTags() != null) builder.tags(requestBody.getTags());

                CreateSchemaResponse response =  glueClient.createSchema(builder.build());
                System.out.println("Schema created with ARN: " + response);
                return response;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

