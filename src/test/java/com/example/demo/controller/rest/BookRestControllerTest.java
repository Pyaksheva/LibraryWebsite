package com.example.demo.controller.rest;

import com.example.demo.dto.*;
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
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetBookByName() throws Exception {
        Long bookId = 1L;
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName("Война и мир");
        bookDto.setGenre("Роман");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/book")
                                .param("name", bookDto.getName())
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testGetBookByNameV2() throws Exception {
        Long bookId = 1L;
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName("Война и мир");
        bookDto.setGenre("Роман");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/book/v2")
                                .param("name", bookDto.getName())
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testGetBookByNameV3() throws Exception {
        Long bookId = 1L;
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName("Война и мир");
        bookDto.setGenre("Роман");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/book/v3")
                                .param("name", bookDto.getName())
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testCreateUpdateDeleteBook() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .name("Не навреди")
                .genreId(2L)
                .build();
        String requestBody = objectMapper.writeValueAsString(bookCreateDto);

        String genreName = "Роман";
        String bookJson = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/book/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookCreateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(genreName))
                .andReturn()
                .getResponse().getContentAsString(Charset.defaultCharset());
        BookDto bookDto = objectMapper.readValue(bookJson, BookDto.class);
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .id(bookDto.getId())
                .name("Призвание")
                .genreId(1L)
                .build();
        String requestBody1 = objectMapper.writeValueAsString(bookUpdateDto);

        String genreName1 = "Рассказ";
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/book/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody1)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookUpdateDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookUpdateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(genreName1));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/book/delete/{id}", bookDto.getId())
                )
                .andExpect(status().isOk());
    }
}























