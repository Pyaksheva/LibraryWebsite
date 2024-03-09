package com.example.demo.service;

import com.example.demo.dto.BookCreateDto;
import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookUpdateDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.Genre;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto getByNameV1(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book with name: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new IllegalStateException("Книга с таким названием " + name + " не найдена");
        }
    }
    @Override
    public BookDto getByNameV2(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book with name: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new IllegalStateException("Книга с таким названием " + name + " не найдена");
        }
    }
    @Override
    public BookDto getByNameV3(String name) {
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), name);
            }
        });
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findOne(specification);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book with name: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new IllegalStateException("Книга с таким названием " + name + " не найдена");
        }
    }
    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        log.info("Try to find genre with id {}", bookCreateDto.getGenreId());
        Optional<Genre> genre = genreRepository.findById(bookCreateDto.getGenreId());
        if (genre.isEmpty()) {
            log.info("Id {} жанра не найден", bookCreateDto.getGenreId());
            throw new IllegalStateException("Жанр с таким id " + bookCreateDto.getGenreId() + " не найден");
        }
        log.info("Try to create book {}", bookCreateDto);
        Book book = convertDtoToEntity(bookCreateDto);
        book.setGenre(genre.get());
        book = bookRepository.save(book);
        BookDto bookDto = convertEntityToDto(book);
        log.info("Создана книга с id {}", book.getId());
        return bookDto;
    }
    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        log.info("Try to find book with id {}", bookUpdateDto.getId());
        Optional<Book> bookOptional = bookRepository.findById(bookUpdateDto.getId());
        if (bookOptional.isEmpty()) {
            log.info("Id {} книги не найден", bookUpdateDto.getId());
            throw new IllegalStateException("Книга с таким id "+ bookUpdateDto.getId() + " не найдена");
        }
        log.info("Try to update book {}", bookUpdateDto);
        Book book = bookOptional.get();
        book.setName(bookUpdateDto.getName());
        if (!book.getGenre().getId().equals(bookUpdateDto.getGenreId())) {
            Genre genre = genreRepository.findById(bookUpdateDto.getGenreId()).orElseThrow();
            book.setGenre(genre);
        };
        book = bookRepository.save(book);
        BookDto bookDto = convertEntityToDto(book);
        log.info("Книга с id {} обновлена", book.getId());
        return bookDto;
    }
    @Override
    public void deleteBook(Long id) {
        log.info("Try to delete book with id {}", id);
        bookRepository.deleteById(id);
        log.info("Книга с id {} удалена", id);
    }
    @Override
    public List<BookDto> getAllBooks() {
        log.info("Try to find all books");
        List<Book> books = bookRepository.findAll();
        log.info("Количество всех найденных книг {}", books.size());
        return books.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        return Book.builder()
                .name(bookCreateDto.getName())
                .build();
    }

    private BookDto convertEntityToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .name(book.getName())
                .build();
    }
}
