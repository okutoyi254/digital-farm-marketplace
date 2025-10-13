package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerRegister {

    private  String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String location;
    private String passwordHash;

}
