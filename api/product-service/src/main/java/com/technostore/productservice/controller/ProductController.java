package com.technostore.productservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.technostore.productservice.service.CategoryService;
import com.technostore.productservice.dto.ProductDto;
import com.technostore.productservice.dto.SortType;
import com.technostore.productservice.service.ProductService;
import com.technostore.productservice.utils.AppError;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping(path = "/{id}")
    ResponseEntity<?> getProductById(@PathVariable Long id, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(productService.getProductById(id, request), HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "No product with id: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/products-by-ids")
    ResponseEntity<?> getProductsByIds(@RequestParam String ids, HttpServletRequest request) {
        List<Long> productsIds = listLongFromString(ids);
        List<ProductDto> products = new ArrayList<>();

        for (Long id : productsIds) {
            try {
                products.add(productService.getProductById(id, request));
            } catch (EntityNotFoundException exception) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "No product with id: " + id), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    ResponseEntity<?> search(@RequestParam int numberPage,
                             @RequestParam int sizePage,
                             @RequestParam(required = false) String word,
                             @RequestParam(required = false, defaultValue = "NOTHING") SortType sort,
                             @RequestParam(required = false, defaultValue = "0") Integer minRating,
                             @RequestParam(required = false, defaultValue = "10") Integer maxRating,
                             @RequestParam(required = false, defaultValue = "0") Integer minPrice,
                             @RequestParam(required = false, defaultValue = "2147483647") Integer maxPrice,
                             @RequestParam(required = false) String categories,
                             HttpServletRequest request) {
        List<Long> listCategories = listLongFromString(categories);

        return new ResponseEntity<>(productService.searchProducts(numberPage, sizePage, sort, word, minRating,
                maxRating, minPrice, maxPrice, listCategories, request), HttpStatus.OK);
    }

    @RequestMapping(path = "/popular-categories", method = RequestMethod.GET)
    ResponseEntity<?> getPopularCategories() {
        return new ResponseEntity<>(categoryService.getPopularCategories(), HttpStatus.OK);
    }

    @RequestMapping(path = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<?> getProductImage(@RequestParam Long id, HttpServletRequest request) {
        String photoLink;
        try {
            photoLink = productService.getPhotoLink(id);
        } catch (EntityNotFoundException exception) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Product with id " + id + " does not exist."), HttpStatus.NOT_FOUND);
        }
        if (photoLink == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        try {
            String path = new File("").getAbsolutePath();
            File file = new File(path + photoLink);
            InputStream input = new FileInputStream(file);
            return new ResponseEntity<>(IOUtils.toByteArray(input), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    private List<Long> listLongFromString(String str) {
        if (str == null) {
            return new ArrayList<>();
        }
        Scanner scanner = new Scanner(str);
        List<Long> resultList = new ArrayList<>();
        scanner.useDelimiter(",");
        while (scanner.hasNextLong()) {
            resultList.add(scanner.nextLong());
        }

        scanner.close();
        return resultList;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<AppError> handleException(HttpClientErrorException e) {
        if (e instanceof HttpClientErrorException.Forbidden) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
        }
        if (e instanceof HttpClientErrorException.Unauthorized) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        throw e;
    }
}
