package com.example.demo.service;

import static org.mockito.Mockito.*;

import java.util.*;

import com.example.demo.dto.AuthorCreateDto;
import com.example.demo.dto.AuthorDto;
import com.example.demo.dto.AuthorUpdateDto;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

@SpringBootTest
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void testGetAuthorById() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);


        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorById(id);

        verify(authorRepository).findById(id);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByIdNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));

        verify(authorRepository).findById(id);
    }

    @Test
    public void testGetAuthorByNameV1() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorByNameV1(name);

        verify(authorRepository).findAuthorByName(name);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByNameV1NotFound() {
        String name = "John";

        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByNameV1(name));

        verify(authorRepository).findAuthorByName(name);
    }

    @Test
    public void testGetAuthorByNameV2() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findAuthorByNameBySql(name)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorByNameV2(name);

        verify(authorRepository).findAuthorByNameBySql(name);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByNameV2NotFound() {
        String name = "John";

        when(authorRepository.findAuthorByNameBySql(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> authorService.getAuthorByNameV2(name));

        verify(authorRepository).findAuthorByNameBySql(name);
    }

    @Test
    public void testGetAuthorByNameV3() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findOne(any(Specification.class))).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorByNameV3(name);

        verify(authorRepository).findOne(any(Specification.class));
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByNameV3NotFound() {
        String name = "John";

        when(authorRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> authorService.getAuthorByNameV3(name));

        verify(authorRepository).findOne(any(Specification.class));
    }

    @Test
    public void testCreateAuthor() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        AuthorCreateDto authorCreateDto = new AuthorCreateDto(name, surname);
        Author author = new Author(null, name, surname, null);
        Author newAuthor = new Author(id, name, surname, null);

        when(authorRepository.save(author)).thenReturn(newAuthor);

        AuthorDto authorDto = authorService.createAuthor(authorCreateDto);

        verify(authorRepository).save(author);
        Assertions.assertTrue(newAuthor.getId() != null);
        Assertions.assertEquals(authorCreateDto.getName(), authorDto.getName());
        Assertions.assertEquals(authorCreateDto.getSurname(), authorDto.getSurname());
    }

    @Test
    public void testCreateAuthorFail() {
        String name = "John";
        String surname = "Doe";
        AuthorCreateDto authorCreateDto = new AuthorCreateDto(name, surname);

        when(authorRepository.save(any())).thenThrow(IllegalStateException.class);
        Assertions.assertThrows(IllegalStateException.class, () -> authorService.createAuthor(authorCreateDto));

        verify(authorRepository).save(any());

    }

    @Test
    public void testUpdateAuthor() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(id, name, surname);
        Author author = new Author(id, name, surname, null);
        Author newAuthor = new Author(id, name, surname, null);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(newAuthor)).thenReturn(newAuthor);

        AuthorDto authorDto = authorService.updateAuthor(authorUpdateDto);

        verify(authorRepository).findById(id);
        verify(authorRepository).save(newAuthor);
        Assertions.assertEquals(authorUpdateDto.getId(), authorDto.getId());
        Assertions.assertEquals(authorUpdateDto.getName(), authorDto.getName());
        Assertions.assertEquals(authorUpdateDto.getSurname(), authorDto.getSurname());
    }

    @Test
    public void testUpdateAuthorFail() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(id, name, surname);
        Author author = new Author(id, name, surname, null);
        Author newAuthor = new Author(id, name, surname, null);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(newAuthor)).thenThrow(IllegalStateException.class);

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.updateAuthor(authorUpdateDto));

        verify(authorRepository).findById(id);
        verify(authorRepository).save(newAuthor);
    }

    @Test
    public void testDeleteAuthor() {
        Long id = 1L;

        authorRepository.deleteById(id);

        verify(authorRepository).deleteById(id);
    }

    @Test
    public void testDeleteAuthorFail() {
        Long id = 1L;

        doThrow(IllegalStateException.class).when(authorRepository).deleteById(id);

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.deleteAuthor(id));

        verify(authorRepository).deleteById(id);
    }

    @Test
    public void testGetAllAuthors() {
        List<Author> authors = List.of(new Author(1L, "John", "Doe", null), new Author(2L, "Александр", "Пушкин", null));
        List<AuthorDto> authorDtos = List.of(new AuthorDto(1L, "John", "Doe", null), new AuthorDto(2L, "Александр", "Пушкин", null));

        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorDto> authorDtos1 = authorService.getAllAuthors();

        verify(authorRepository).findAll();
        Assertions.assertEquals(authorDtos, authorDtos1);
    }

    @Test
    public void testGetAllAuthorsFail() {

        when(authorRepository.findAll()).thenThrow(IllegalStateException.class);

        Assertions.assertThrows(IllegalStateException.class, () -> authorService.getAllAuthors());

        verify(authorRepository).findAll();
    }
}














