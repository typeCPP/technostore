package com.technostore.reviewservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import com.technostore.reviewservice.service.client.ProductRestTemplateClient;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;
import com.technostore.reviewservice.service.ReviewService;
import com.technostore.reviewservice.utils.AppError;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ProductRestTemplateClient productRestTemplateClient;

    @Autowired
    UserRestTemplateClient userRestTemplateClient;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getReviewById(@PathVariable Long id, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(reviewService.getReviewById(id, request), HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "No review with id: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/all-by-product-id/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getAllReviewsByProductId(@PathVariable Long id, HttpServletRequest request) {
        return new ResponseEntity<>(reviewService.getAllReviewsByProductId(id, request), HttpStatus.OK);
    }

    @RequestMapping(path = "/newReview", method = RequestMethod.POST)
    public ResponseEntity<?> setReview(@RequestParam(required = false) String text,
                                       @RequestParam int rate,
                                       @RequestParam long productId,
                                       HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        reviewService.setReview(userId, productId, rate, text);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/product-rating/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getReviewById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(reviewService.getProductRatingById(id), HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "No product with id: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/by-product-id/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getReviewByProductId(@PathVariable long id, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            return new ResponseEntity<>(reviewService.getReviewByUserIdAndProductId(userId, id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/statistics-by-product-ids", method = RequestMethod.GET)
    public ResponseEntity<?> getReviewStatisticsByProductIds(@RequestParam String ids) {
        List<Long> productsIds = listLongFromString(ids);
        return new ResponseEntity<>(reviewService.getReviewStatisticDto(productsIds), HttpStatus.OK);
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
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            e.getMessage()), HttpStatus.FORBIDDEN);
        }
        if (e instanceof HttpClientErrorException.Unauthorized) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        throw e;
    }
}
