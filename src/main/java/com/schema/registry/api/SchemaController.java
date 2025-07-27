package com.schema.registry.api;

import com.schema.registry.model.CreateSchemaRequestBody;
import com.schema.registry.model.CreateSchemaResponseBody;
import com.schema.registry.model.RegistryRequest;
import com.schema.registry.service.GlueRegistryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.glue.model.CreateRegistryResponse;
import software.amazon.awssdk.services.glue.model.CreateSchemaResponse;


@RestController
@RequestMapping("/api/v1/schemas")
@Tag(name = "Schemas", description = "Schema Management APIs")
public class SchemaController {

    private final GlueRegistryService glueRegistryService;

    @Autowired
    public SchemaController(GlueRegistryService glueRegistryService) {
        this.glueRegistryService = glueRegistryService;
    }

    @PostMapping()
    @Operation(
            summary = "Create a schema",
            description = "Creates a new schema in AWS Glue Schema Registry."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Schema created successfully",
            content = @Content(schema = @Schema(implementation = CreateSchemaResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload",
            content = @Content
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
    )
    public ResponseEntity<CreateSchemaResponseBody> createSchema(@RequestBody CreateSchemaRequestBody request) {
         try {
            CreateSchemaResponse response = glueRegistryService.createSchema(request);
            CreateSchemaResponseBody createSchemaResponseBody = new CreateSchemaResponseBody();
            createSchemaResponseBody.setSchemaARN(response.schemaArn());
            createSchemaResponseBody.setSchemaName(response.schemaName());
            createSchemaResponseBody.setRegistryArn(response.registryArn());
            createSchemaResponseBody.setRegistryName(response.registryName());
            createSchemaResponseBody.setSchemaVersionId(response.schemaVersionId());
            createSchemaResponseBody.setStatus(response.schemaStatusAsString());
            createSchemaResponseBody.setDescription(response.description());
            createSchemaResponseBody.setCompatibility(request.getCompatibility());
            createSchemaResponseBody.setDataFormat(request.getDataFormat());
            createSchemaResponseBody.setTags(request.getTags());
            createSchemaResponseBody.setSchemaDefinition(request.getSchemaDefinition());
            createSchemaResponseBody.setSchemaVersionId(response.schemaVersionId());

             return new ResponseEntity<>(createSchemaResponseBody, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
