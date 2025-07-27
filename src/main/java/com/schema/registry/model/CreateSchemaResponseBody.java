package com.schema.registry.model;


import java.util.Map;


public class CreateSchemaResponseBody {

    // Optional RegistryId
    private String registryArn;
    private String registryName;

     private String schemaName;

     private String schemaARN;

     private String schemaVersionId;

     private String status;

     private String description;

     private Map<String, String> tags;
     private String compatibility;
     private String dataFormat;
    private String schemaDefinition;




    public String getRegistryArn() {
        return registryArn;
    }

    public void setRegistryArn(String registryArn) {
        this.registryArn = registryArn;
    }

    public String getRegistryName() {
        return registryName;
    }

    public void setRegistryName(String registryName) {
        this.registryName = registryName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaARN() {
        return schemaARN;
    }

    public void setSchemaARN(String schemaARN) {
        this.schemaARN = schemaARN;
    }

    public String getSchemaVersionId() {
        return schemaVersionId;
    }

    public void setSchemaVersionId(String schemaVersionId) {
        this.schemaVersionId = schemaVersionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getSchemaDefinition() {
        return schemaDefinition;
    }

    public void setSchemaDefinition(String schemaDefinition) {
        this.schemaDefinition = schemaDefinition;
    }
}
