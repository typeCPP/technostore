package com.technostore.productservice.controller;

import com.technostore.productservice.service.ProductService;
import com.technostore.productservice.utils.AppError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getProductById(@PathVariable Long id, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "No product with id: " + id), HttpStatus.NOT_FOUND);
        }
    }
}
