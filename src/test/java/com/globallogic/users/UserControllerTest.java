package com.globallogic.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.users.model.ResponseBody;
import com.globallogic.users.model.User;
import com.globallogic.users.model.UserDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenCredentials_whenFind_thenStatus200() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("maxi");
        userDTO.setEmail("hola@gmail.com");
        userDTO.setPassword("a2asfGfdfdf4");

        String json = objectMapper.writeValueAsString(userDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/sign-up").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(json)).andReturn();

        Integer status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);

        var u = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseBody.class);

        assert u.getEmail().equals("hola@gmail.com");

    }
}
