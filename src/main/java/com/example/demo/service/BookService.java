package com.example.demo.service;

import com.example.demo.dto.BookDto;

public interface BookService {
    BookDto getByNameV1(String name);

    BookDto getByNameV2(String name);

    BookDto getByNameV3(String name);
}
