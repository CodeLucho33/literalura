package com.example.literalura.controller;

import com.example.literalura.model.Book;
import com.example.literalura.model.BooksResponse;
import com.example.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books/title")
    public BooksResponse getBooksByTitle(@RequestParam String title) {
        return bookService.getBooks(title);
    }

    @GetMapping("/books/author")
    public BooksResponse getBooksByAuthor(@RequestParam String author) {
        return bookService.getBooks(author);
    }

    @GetMapping("/books/by-author")
    public List<Book> getBooksBySpecificAuthor(@RequestParam String author) {
        return bookService.getBooksBySpecificAuthor(author);
    }

}

