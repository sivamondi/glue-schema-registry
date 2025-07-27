package com.schema.registry.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/v1/hello")
@Tag(name = "Hello", description = "Hello management APIs")
public class HelloController  {


    static final String MESSAGE = "Welcome to AWS Glue Schema Registry Service!";

    @Autowired
    public HelloController() {
        super();
    }


    @GetMapping()
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<>(MESSAGE,HttpStatus.OK);
    }

}
