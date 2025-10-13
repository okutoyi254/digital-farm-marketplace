package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {

    private LocalDateTime timeStamp;
    private String message;
    private boolean status;
    private T data;
}
