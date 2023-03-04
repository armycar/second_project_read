package com.readers.be3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class OneCommentTest {
  
//   @Autowired MockMvc mockMvc;

//   @Test
//   void sns로그인테스트(){
//     mockMvc.perform(post("/api/onecomment/add")
//                       .contentType(MediaType.APPLICATION_JSON)
//                       .content(null)
//                       ).andDo(print())
//                       .andExpect(status().isOk());
//   }
// }
