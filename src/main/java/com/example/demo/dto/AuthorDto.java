package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorDto {
    private Long id;
    private String name;
    private String surname;

    private List<BookDto> books;

    @JsonIgnore
    public String getBookNames() {
        if (books!=null) {
            return books.stream().map(book -> book.getName()).collect(Collectors.joining(" "));
        } else return " ";
    }
}
