package com.farmplace.digitalmarket.controllers;

import com.farmplace.digitalmarket.DTO.ApiResponse;
import com.farmplace.digitalmarket.DTO.OrderResponse;
import com.farmplace.digitalmarket.service.services.BusinessServiceImpl;
import com.farmplace.digitalmarket.utils.LoggedInCustomer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/business")
public class BusinessController {

    private final BusinessServiceImpl businessService;

    public BusinessController(BusinessServiceImpl businessService) {
        this.businessService = businessService;
    }

    @PostMapping("place/order")
    public ResponseEntity<ApiResponse<OrderResponse>>placeOrder(double payment){

        String username= LoggedInCustomer.getUsername();
        OrderResponse orderResponse= businessService.placeOrder(username,payment);

        ApiResponse<OrderResponse> response=new ApiResponse<>(LocalDateTime.now(),"Order placed successfully"
                ,true,orderResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
