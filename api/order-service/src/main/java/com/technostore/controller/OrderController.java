package com.technostore.controller;

import com.technostore.service.OrderService;
import com.technostore.service.client.UserRestTemplateClient;
import com.technostore.utils.AppError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

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
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can set product count"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }

        orderService.setProductCount(userId, productId, count);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}