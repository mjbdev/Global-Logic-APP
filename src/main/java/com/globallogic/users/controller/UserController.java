package com.globallogic.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.users.config.JwtTokenUtil;
import com.globallogic.users.exception.EmailWrongFormatException;
import com.globallogic.users.model.LoginResponse;
import com.globallogic.users.model.ResponseBody;
import com.globallogic.users.model.UserDTO;
import com.globallogic.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBody> login(@RequestBody String body) throws Exception {
        log.info("*** Begin sign-up ***");
        log.info("Request Body: " + body);

        ObjectMapper om = new ObjectMapper();

        UserDTO userDTO = null;
        ResponseBody responseBody = null;
        try {
            userDTO = om.readValue(body, UserDTO.class);

            responseBody = userService.signUp(userDTO);
        } catch (JsonProcessingException e) {
            log.error("Error processing json ", e);
            throw e;
        } catch (EmailWrongFormatException emailWrongFormatException) {
            log.error("Error email format ", emailWrongFormatException);
            throw emailWrongFormatException;
        }

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestParam String username, @RequestParam String password, @RequestHeader String authorization) throws Exception {
        log.info("*** Begin login ***");

        Boolean isValid = userService.validateToken(authorization);
        LoginResponse loginResponse = null;
        if (isValid) {
            loginResponse = userService.createLoginResponse(username);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            throw new Exception("Invalid Token");
        }
    }
}
