package com.example.demo.service;

import com.example.demo.dto.AuthorDto;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);
    AuthorDto getAuthorByNameV1(String name);
    AuthorDto getAuthorByNameV2(String name);
    AuthorDto getAuthorByNameV3(String name);
}
