package com.schema.registry.api;

import com.schema.registry.model.CreateSchemaRequestBody;
import com.schema.registry.model.RegistryRequest;
import com.schema.registry.service.GlueRegistryService;
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
    public ResponseEntity<?> createSchema(@RequestBody CreateSchemaRequestBody request) {
         try {
            CreateSchemaResponse response = glueRegistryService.createSchema(request);
            return new ResponseEntity<>(response.registryArn(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
