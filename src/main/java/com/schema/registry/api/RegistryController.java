package com.schema.registry.api;

import com.schema.registry.model.RegistryRequest;
import com.schema.registry.model.RegistryResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.schema.registry.service.GlueRegistryService;
import software.amazon.awssdk.services.glue.model.CreateRegistryResponse;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/registries")
@Tag(name = "Registry", description = "Registry Management APIs")
public class RegistryController {

    private final GlueRegistryService glueRegistryService;

    @Autowired
    public RegistryController(GlueRegistryService glueRegistryService) {
        this.glueRegistryService = glueRegistryService;
    }

    @PostMapping()
    public ResponseEntity<?> createRegistry(@RequestBody RegistryRequest request) {
         try {
            CreateRegistryResponse response = glueRegistryService.createRegistry(request);
            return new ResponseEntity<>(response.registryArn(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<?> getRegistry(@RequestParam String tagName, @RequestParam String tagValue) {
        try {
            RegistryResponse registryResponse = glueRegistryService.findRegistryArnByTag(tagName, tagValue);
            if (registryResponse != null) {
                return new ResponseEntity<>(registryResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Registry not found for the given tag", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving registry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
