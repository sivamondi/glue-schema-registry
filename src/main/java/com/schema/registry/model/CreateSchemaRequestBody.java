package com.schema.registry.model;

import jakarta.validation.constraints.*;
import java.util.Map;


public class CreateSchemaRequestBody {

    // Optional RegistryId
    private String registryArn;
    private String registryName;

    @NotBlank
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[a-zA-Z0-9_\\-\\$#]+$", message = "SchemaName can only contain letters, numbers, hyphen, underscore, dollar sign, or hash mark.")
    private String schemaName;

    @NotBlank
    @Pattern(regexp = "AVRO|JSON|PROTOBUF", message = "DataFormat must be AVRO, JSON, or PROTOBUF")
    private String dataFormat;

    @Pattern(regexp = "NONE|DISABLED|BACKWARD|BACKWARD_ALL|FORWARD|FORWARD_ALL|FULL|FULL_ALL", message = "Invalid compatibility mode")
    private String compatibility;

    @Size(max = 2048)
    private String description;

    @Size(max = 50)
    private Map<@Size(min = 1, max = 128) String, @Size(max = 256) String> tags;

    @NotBlank
    @Size(min = 1, max = 170000)
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

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
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

    public String getSchemaDefinition() {
        return schemaDefinition;
    }

    public void setSchemaDefinition(String schemaDefinition) {
        this.schemaDefinition = schemaDefinition;
    }
}
