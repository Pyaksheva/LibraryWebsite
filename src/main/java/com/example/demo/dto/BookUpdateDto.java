package com.example.demo.dto;

import com.example.demo.entity.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookUpdateDto {
    @NotNull
    private Long id;
    @NotBlank(message = "Необходимо указать название")
    private String name;
    @NotNull
    private Long genreId;
}
