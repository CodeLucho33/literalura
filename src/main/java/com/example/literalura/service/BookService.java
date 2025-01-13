package com.example.literalura.service;

import com.example.literalura.model.Book;
import com.example.literalura.model.BooksResponse;
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

}


