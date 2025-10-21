package com.farmplace.digitalmarket.Model;

import com.farmplace.digitalmarket.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userInfo")
public class User {

    @Id
    private Long userId;
    private Roles role;

}
