package com.globallogic.users.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ResponseBody {

    private String id;
    private String email;
    private LocalDateTime created;
    private String token;
    private Boolean isActive;


}
