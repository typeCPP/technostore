package com.technostore.productservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.technostore.productservice.service.CategoryService;
import com.technostore.productservice.dto.SortType;
import com.technostore.productservice.service.ProductService;
import com.technostore.productservice.service.client.UserRestTemplateClient;
import com.technostore.productservice.utils.AppError;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserRestTemplateClient userRestTemplateClient;

    @GetMapping(path = "/{id}")
    ResponseEntity<?> getProductById(@PathVariable Long id, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "No product with id: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/search")
    ResponseEntity<?> search(@RequestParam int numberPage,
                             @RequestParam int sizePage,
                             @RequestParam(required = false) String word,
                             @RequestParam(required = false) SortType sort,
                             @RequestParam(required = false, defaultValue = "0") Integer minRating,
                             @RequestParam(required = false, defaultValue = "10") Integer maxRating,
                             @RequestParam(required = false) Integer minPrice,
                             @RequestParam(required = false) Integer maxPrice,
                             @RequestParam(required = false) String categories,
                             HttpServletRequest request) {
        Long userId = (long) -1;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException | HttpClientErrorException.Forbidden ignored) {
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }

        Scanner scanner = new Scanner("");
        List<Long> listCategories = new ArrayList<>();
        if (categories != null) {
            scanner = new Scanner(categories);
            scanner.useDelimiter(",");
            while (scanner.hasNextLong()) {
                listCategories.add(scanner.nextLong());
            }
        }
        scanner.close();

        try {
            return new ResponseEntity<>(productService.searchProducts(numberPage, sizePage, sort, word, minRating,
                    maxRating, minPrice, maxPrice, listCategories, userId), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "There are no such results."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/popular-categories", method = RequestMethod.GET)
    ResponseEntity<?> getProductById() {
        return new ResponseEntity<>(categoryService.getPopularCategories(), HttpStatus.OK);
    }
}
