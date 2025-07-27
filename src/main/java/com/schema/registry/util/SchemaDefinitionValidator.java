package com.schema.registry.util;

import com.schema.registry.model.CreateSchemaRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;

import java.io.IOException;

public class SchemaDefinitionValidator {

    private static final ObjectMapper mapper = new ObjectMapper();


    public static boolean isValidSchema(CreateSchemaRequestBody value) {
        if (value == null || value.getSchemaDefinition() == null || value.getDataFormat() == null) {
            return true; // Handled by @NotBlank elsewhere
        }

        String def = value.getSchemaDefinition();
        String format = value.getDataFormat().toUpperCase();

        try {
            switch (format) {
                case "JSON":
                    mapper.readTree(def); // Validate valid JSON
                    return true;
                case "AVRO":
                    new Schema.Parser().parse(def); // Validate AVRO schema
                    return true;
                case "PROTOBUF":
                    return def.contains("syntax =") && def.contains("message "); // Very basic check
                default:
                    return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
}
