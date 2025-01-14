package com.example.literalura.service;

import com.example.literalura.entity.AuthorEntity;
import com.example.literalura.entity.BookEntity;
import com.example.literalura.model.Author;
import com.example.literalura.model.Book;
import com.example.literalura.model.BooksResponse;
import com.example.literalura.repository.AuthorRepository;
import com.example.literalura.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final RestTemplate restTemplate;

    @Autowired
    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String BASE_URL = "https://gutendex.com/books";

    public BooksResponse getBooks(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = BASE_URL + "?search=" + encodedQuery;
            System.out.println("URL construida: " + url);
            String jsonResponse = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(BooksResponse.class);
            return objectMapper.readValue(jsonResponse, BooksResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Book> getBooksBySpecificAuthor(String authorName) {
        BooksResponse booksResponse = getBooks(authorName);
        return booksResponse.getResults().stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getName().equalsIgnoreCase(authorName)))
                .collect(Collectors.toList());
    }
    @Autowired
    private BookRepository bookRepository;

    public BookEntity searchAndSaveBookByTitle(String title) {
        BooksResponse response = getBooks(title);
        if (response != null && !response.getResults().isEmpty()) {
            Book book = response.getResults().get(0);
            String language = book.getSubjects() != null && !book.getSubjects().isEmpty()
                    ? book.getSubjects().get(0)
                    : "Unknown";
            BookEntity bookEntity = new BookEntity(
                    book.getTitle(),
                    book.getAuthors().isEmpty() ? "Unknown" : book.getAuthors().get(0).getName(),
                    language,
                    book.getDownload_count()
            );
            return bookRepository.save(bookEntity);
        }
        return null;
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }
    @Autowired
    private AuthorRepository authorRepository;

    public BookEntity searchAndSaveBook(String title) {
        BooksResponse response = getBooks(title);
        if (response != null && !response.getResults().isEmpty()) {
            Book book = response.getResults().get(0);

            // Guardar autor
            Author firstAuthor = book.getAuthors().isEmpty() ? null : book.getAuthors().get(0);
            AuthorEntity authorEntity = null;
            if (firstAuthor != null) {
                authorEntity = new AuthorEntity(
                        firstAuthor.getName(),
                        firstAuthor.getBirthYear(),
                        firstAuthor.getDeathYear()
                );
                authorRepository.save(authorEntity);
            }

            // Guardar libro
            BookEntity bookEntity = new BookEntity(
                    book.getTitle(),
                    firstAuthor != null ? firstAuthor.getName() : "Unknown",
                    book.getSubjects() != null && !book.getSubjects().isEmpty()
                            ? book.getSubjects().get(0)
                            : "Unknown",
                    book.getDownload_count()
            );
            return bookRepository.save(bookEntity);
        }
        return null;
    }

    public List<AuthorEntity> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<AuthorEntity> getAuthorsAliveInYear(Integer year) {
        return authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
    }


}


