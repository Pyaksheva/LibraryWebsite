package com.example.demo.service;

import com.example.demo.dto.AuthorCreateDto;
import com.example.demo.dto.AuthorDto;
import com.example.demo.dto.AuthorUpdateDto;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);
    AuthorDto getAuthorByNameV1(String name);
    AuthorDto getAuthorByNameV2(String name);
    AuthorDto getAuthorByNameV3(String name);
    AuthorDto createAuthor(AuthorCreateDto authorCreateDto);
    AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto);
    void deleteAuthor(Long id);
}
