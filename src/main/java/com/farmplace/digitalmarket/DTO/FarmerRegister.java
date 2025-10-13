package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FarmerRegister {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String location;
    private String description;
}
