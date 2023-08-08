package com.globallogic.users.service;

import com.globallogic.users.config.JwtTokenUtil;
import com.globallogic.users.exception.EmailWrongFormatException;
import com.globallogic.users.exception.PasswordWrongFormatException;
import com.globallogic.users.exception.UserAlreadyExistsException;
import com.globallogic.users.exception.UserNotExistsException;
import com.globallogic.users.model.LoginResponse;
import com.globallogic.users.model.ResponseBody;
import com.globallogic.users.model.User;
import com.globallogic.users.model.UserDTO;
import com.globallogic.users.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserService {

    @Value("${email.regex}")
    private String emailRegex;

    @Value("${password.regex}")
    private String passwordRegex;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseBody signUp(UserDTO userDTO) throws Exception {
        log.info("Saving user with email: " + userDTO.getEmail());

        validateFields(userDTO);

        validateIfUserExists(userDTO);

        encodePassword(userDTO);

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhones(userDTO.getPhones());
        user.setPassword(userDTO.getPassword());
        user.setCreated(LocalDateTime.now());

        userRepository.save(user);
        log.info("User saved");

        ResponseBody rb = new ResponseBody();
        rb.setId(user.getId());
        rb.setEmail(user.getEmail());
        rb.setCreated(user.getCreated());
        rb.setIsActive(true);
        rb.setToken(jwtTokenUtil.generateToken(user));

        return rb;
    }

    private void encodePassword(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
    }

    private void validateIfUserExists(UserDTO userDTO) throws UserAlreadyExistsException {
        User dbUser = userRepository.findByEmail(userDTO.getEmail());
        if (dbUser != null) throw new UserAlreadyExistsException("User with email: " + userDTO.getEmail() + " already exists");
    }

    private void validateFields(UserDTO userDTO) throws Exception {

        if (userDTO.getEmail() == null || userDTO.getPassword() == null) throw new Exception("User and email cannot be null");

        if (!userDTO.getEmail().matches(emailRegex)) throw new EmailWrongFormatException("Email invalid format");

        // TODO Revisar la regex, no valida bien que haya hasta dos digitos
        if (!userDTO.getPassword().matches(passwordRegex)) throw new PasswordWrongFormatException("Password invalid format");
    }

    public Boolean validateToken(String requestTokenHeader) throws Exception {
        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token", e);
                throw e;
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired", e);
                throw e;
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByEmail(username);

            if (jwtTokenUtil.validateToken(jwtToken, user)) {
                return true;
            }
        }
        return false;
    }

    public LoginResponse createLoginResponse(String username) throws UserNotExistsException {
        User user = userRepository.findByEmail(username);

        if(user == null) throw new UserNotExistsException("User with email: " + username + " does not exist");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtTokenUtil.generateToken(user));
        loginResponse.setCreated(user.getCreated());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setId(user.getId());
        loginResponse.setName(user.getName());
        loginResponse.setIsActive(true);
        loginResponse.setPhones(user.getPhones());
        loginResponse.setLastLogin(LocalDateTime.now());
        loginResponse.setPassword(user.getPassword());

        return loginResponse;
    }
}
