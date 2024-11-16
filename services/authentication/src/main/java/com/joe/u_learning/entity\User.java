package com.joe.u_learning.entity;

import java.io.Serializable;
import lombok.Data;


/**
 * User
 * @version [1.0.0, 2024-09-25 11:36:19] 
 */ 
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 2441017385947648798L;

    private String username;

    private String password;

    private String email;

    private String address;

    private String phoneNumber;

}