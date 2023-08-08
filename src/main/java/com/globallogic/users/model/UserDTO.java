package com.globallogic.users.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {

    private String name;
    private String email;
    private String password;
    private List<Phone> phones = new ArrayList<>();
}
