package com.example.demo.controller.rest;

import com.example.demo.dto.AuthorCreateDto;
import com.example.demo.dto.AuthorDto;
import com.example.demo.dto.AuthorUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAuthorById() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV1() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/author")
                                .param("name", authorDto.getName())
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV2() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/author/v2")
                                .param("name", authorDto.getName())
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV3() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/author/v3")
                                .param("name", authorDto.getName())
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testCreateUpdateDeleteAuthor() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        AuthorCreateDto authorCreateDto = AuthorCreateDto.builder()
                .name("Федор")
                .surname("Углов")
                .build();
        String requestBody = objectMapper.writeValueAsString(authorCreateDto);


        String authorJson = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/author/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorCreateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorCreateDto.getSurname()))
                .andReturn()
                .getResponse().getContentAsString(Charset.defaultCharset());
        AuthorDto authorDto = objectMapper.readValue(authorJson, AuthorDto.class);
        AuthorUpdateDto authorUpdateDto = AuthorUpdateDto.builder()
                .id(authorDto.getId())
                .name("Федор")
                .surname("Тютчев")
                .build();
        String requestBody1 = objectMapper.writeValueAsString(authorUpdateDto);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/author/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorUpdateDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorUpdateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorUpdateDto.getSurname()));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/author/delete/{id}", authorDto.getId())
                )
                .andExpect(status().isOk());
    }

}






















