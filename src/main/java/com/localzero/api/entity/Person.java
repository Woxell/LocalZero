package com.localzero.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Person {

    @Id
    private String email;
    private String name;
    private String password;
    private String pfpURL;

}
