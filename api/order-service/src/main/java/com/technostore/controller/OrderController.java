package com.technostore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import com.technostore.service.OrderService;
import com.technostore.service.client.UserRestTemplateClient;
import com.technostore.utils.AppError;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserRestTemplateClient userRestTemplateClient;

    @RequestMapping(path = "/set-product-count", method = RequestMethod.POST)
    public ResponseEntity<?> setProductCount(@RequestParam Long productId,
                                             @RequestParam int count,
                                             HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        orderService.setProductCount(userId, productId, count);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/complete-order/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> completeOrder(@PathVariable Long id,
                                           HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            orderService.completeOrder(userId, id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/get-current-order", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentOrder(HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(orderService.getCurrentOrder(userId, request), HttpStatus.OK);
    }

    @RequestMapping(path = "/get-completed-orders", method = RequestMethod.GET)
    public ResponseEntity<?> getCompletedOrdersIds(HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(Map.of("ids", orderService.getCompletedOrdersIds(userId)), HttpStatus.OK);
    }

    @RequestMapping(path = "/get-completed-order/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCompletedOrder(@PathVariable Long id, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(orderService.getCompletedOrder(id, userId, request), HttpStatus.OK);
    }

    @RequestMapping(path = "/get-popular-products", method = RequestMethod.GET)
    public ResponseEntity<?> getMostPopularProductsIds() {
        return new ResponseEntity<>(orderService.getPopularProductsIds(), HttpStatus.OK);
    }

    @RequestMapping(path = "/get-in-cart-count-by-product-ids", method = RequestMethod.GET)
    public ResponseEntity<?> getInCartCountByProductsIds(@RequestParam String ids, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Long> productsIds = listLongFromString(ids);

        return new ResponseEntity<>(orderService.getInCartCountByProductIds(productsIds, userId), HttpStatus.OK);
    }

    private List<Long> listLongFromString(String str) {
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
