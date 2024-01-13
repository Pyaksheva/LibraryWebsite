package com.example.demo.service;

import com.example.demo.dto.BookCreateDto;
import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookUpdateDto;

public interface BookService {
    BookDto getByNameV1(String name);

    BookDto getByNameV2(String name);

    BookDto getByNameV3(String name);
    BookDto createBook(BookCreateDto bookCreateDto);
    BookDto updateBook(BookUpdateDto bookUpdateDto);
    void deleteBook(Long id);
}
