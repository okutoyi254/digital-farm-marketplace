package com.farmplace.digitalmarket.config;

import com.farmplace.digitalmarket.DTO.CustomerRegister;
import com.farmplace.digitalmarket.DTO.FarmerRegister;
import com.farmplace.digitalmarket.Model.Customer;
import com.farmplace.digitalmarket.Model.Farmer;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper=new ModelMapper();
        modelMapper.addMappings(new PropertyMap<FarmerRegister, Farmer>() {
            @Override
            //Mappings for farmer and register
            protected void configure() {
                using(ctx -> {
                    FarmerRegister src = (FarmerRegister) ctx.getSource();
                    String firstName = src.getFirstName() != null ? src.getFirstName() : "";
                    String lastName = src.getLastName() != null ? src.getLastName() : "";
                    return (firstName + " " + lastName).trim();
                })
                        .map(source, destination.getFarmerName());
            };
               });

        modelMapper.addMappings(new PropertyMap<CustomerRegister, Customer>() {
            @Override
            protected void configure() {
              using(ctx->{
                  CustomerRegister src=(CustomerRegister) ctx.getSource();
                  String firstName=src.getFirstName() !=null ?src.getFirstName():"";
                  String lastName = src.getLastName() !=null? src.getLastName():"";
                  return (firstName+" "+lastName).trim();
              }).map(source,destination.getCustomerName());
            };
        });
        return modelMapper;
    }
}
