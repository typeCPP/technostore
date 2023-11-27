package com.technostore.reviewservice.controller;

import com.technostore.reviewservice.service.ReviewService;
import com.technostore.reviewservice.utils.AppError;
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
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;
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
}
