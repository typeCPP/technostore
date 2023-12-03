package com.technostore.reviewservice.controller;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.technostore.reviewservice.service.client.ProductRestTemplateClient;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;
import com.technostore.reviewservice.service.ReviewService;
import com.technostore.reviewservice.utils.AppError;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can write review"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
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
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can write review"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(reviewService.getReviewByUserIdAndProductId(userId, id), HttpStatus.OK);
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
}
