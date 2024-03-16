package com.example.demo.service;

import com.example.demo.dto.BookCreateDto;
import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookUpdateDto;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Genre;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testGetBookByNameV1() {
        Long id = 1L;
        String name = "Золушка";
        Genre genre = new Genre(1L, "Рассказ", null);
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findBookByName(name)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameV1(name);

        verify(bookRepository).findBookByName(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByNameV1Fail() {
        String name = "Золушка";

        when(bookRepository.findBookByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> bookService.getByNameV1(name));

        verify(bookRepository).findBookByName(name);
    }

    @Test
    public void testGetBookByNameV2() {
        Long id = 1L;
        String name = "Золушка";
        Genre genre = new Genre(1L, "Рассказ", null);
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameV2(name);

        verify(bookRepository).findBookByNameBySql(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByNameV2Fail() {
        String name = "Золушка";

        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> bookService.getByNameV2(name));

        verify(bookRepository).findBookByNameBySql(name);
    }

    @Test
    public void testGetBookByNameV3() {
        Long id = 1L;
        String name = "Золушка";
        Genre genre = new Genre(1L, "Рассказ", null);
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findOne(any(Specification.class))).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameV3(name);

        verify(bookRepository).findOne(any(Specification.class));
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByNameV3Fail() {
        String name = "Золушка";

        when(bookRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> bookService.getByNameV3(name));

        verify(bookRepository).findOne(any(Specification.class));
    }

    @Test
    public void testCreateBook() {
        Long id = 1L;
        String name = "Золушка";
        Genre genre = new Genre(1L, "Рассказ", null);
        Long genreId = 1L;
        Set<Author> authors = new HashSet<>();
        BookCreateDto bookCreateDto = new BookCreateDto(name, genreId);
        Book book = new Book(null, name, genre, null);
        Book newBook = new Book(id, name, genre, null);

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(bookRepository.save(book)).thenReturn(newBook);

        BookDto bookDto = bookService.createBook(bookCreateDto);

        verify(genreRepository).findById(genreId);
        verify(bookRepository).save(book);
        Assertions.assertTrue(newBook.getId() != null);
        Assertions.assertEquals(bookCreateDto.getName(), bookDto.getName());
        Assertions.assertEquals(bookCreateDto.getGenreId(), genre.getId());
        Assertions.assertEquals(bookDto.getGenre(), genre.getName());
    }

    @Test
    public void testCreateBookFail() {
        String name = "Золушка";
        Long genreId = 1L;
        Genre genre = new Genre(1L, "Рассказ", null);
        BookCreateDto bookCreateDto = new BookCreateDto(name, genreId);

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(bookRepository.save(any())).thenThrow(IllegalStateException.class);
        Assertions.assertThrows(IllegalStateException.class, () -> bookService.createBook(bookCreateDto));

        verify(genreRepository).findById(genreId);
        verify(bookRepository).save(any());
    }

    @Test
    public void testUpdateBook() {
        Long id = 1L;
        String name = "Золушка";
        Genre genre = new Genre(1L, "Рассказ", null);
        Long genreId = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto(id, name, genreId);
        Book book = new Book(id, name, genre, null);
        Book newBook = new Book(id, name, genre, null);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(newBook)).thenReturn(newBook);

        BookDto bookDto = bookService.updateBook(bookUpdateDto);

        verify(bookRepository).findById(id);
        verify(bookRepository).save(newBook);
        Assertions.assertEquals(bookUpdateDto.getId(), bookDto.getId());
        Assertions.assertEquals(bookUpdateDto.getName(), bookDto.getName());
        Assertions.assertEquals(bookUpdateDto.getGenreId(), genre.getId());
        Assertions.assertEquals(bookDto.getGenre(), genre.getName());
    }

    @Test
    public void testUpdateBookFail() {
        Long id = 1L;
        String name = "Золушка";
        Genre genre = new Genre(1L, "Рассказ", null);
        Long genreId = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto(id, name, genreId);
        Book book = new Book(id, name, genre, null);
        Book newBook = new Book(id, name, genre, null);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(newBook)).thenThrow(IllegalStateException.class);

        Assertions.assertThrows(IllegalStateException.class, () -> bookService.updateBook(bookUpdateDto));

        verify(bookRepository).findById(id);
        verify(bookRepository).save(newBook);
    }

    @Test
    public void testDeleteBook() {
        Long id = 1L;

        bookRepository.deleteById(id);

        verify(bookRepository).deleteById(id);
    }

    @Test
    public void testDeleteBookFail() {
        Long id = 1L;

        doThrow(IllegalStateException.class).when(bookRepository).deleteById(id);

        Assertions.assertThrows(IllegalStateException.class, () -> bookService.deleteBook(id));

        verify(bookRepository).deleteById(id);
    }

    @Test
    public void testGetAllBooks() {
        Genre genre1 = new Genre(1l, "Рассказ", null);
        List<Book> books = List.of(new Book(1L, "Золушка", genre1, null), new Book(2L, "Идиот", genre1, null));
        List<BookDto> bookDtos = List.of(new BookDto(1L, "Золушка", "Рассказ", null), new BookDto(2L, "Идиот", "Рассказ", null));

        when(bookRepository.findAll()).thenReturn(books);

        List<BookDto> bookDtos1 = bookService.getAllBooks();

        verify(bookRepository).findAll();
        Assertions.assertEquals(bookDtos, bookDtos1);
    }

    @Test
    public void testGetAllBooksFail() {

        when(bookRepository.findAll()).thenThrow(IllegalStateException.class);

        Assertions.assertThrows(IllegalStateException.class, () -> bookService.getAllBooks());

        verify(bookRepository).findAll();
    }
}
