package com.example.literalura.service;

import com.example.literalura.entity.BookEntity;
import com.example.literalura.model.Author;
import com.example.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Lista todos los autores de los libros guardados en la base de datos.
     * @return Lista de autores únicos.
     */
    public List<Author> getAllAuthors() {
        List<BookEntity> books = bookRepository.findAll();
        List<Author> authors = new ArrayList<>();
        for (BookEntity book : books) {
            Author author = new Author();
            author.setName(book.getAuthor());
            author.setBirthYear(null); // Datos no disponibles en este ejemplo
            author.setDeathYear(null); // Datos no disponibles en este ejemplo
            authors.add(author);
        }
        return authors.stream()
                .distinct() // Remueve duplicados
                .collect(Collectors.toList());
    }

    /**
     * Lista los autores que estaban vivos en un año específico.
     * @param year Año para verificar si estaban vivos.
     * @return Lista de autores vivos en ese año.
     */
    public List<Author> getAuthorsAliveInYear(int year) {
        List<Author> authors = getAllAuthors();
        return authors.stream()
                .filter(author -> isAuthorAliveInYear(author, year))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un autor estaba vivo en un año específico.
     * @param author Autor a verificar.
     * @param year Año a verificar.
     * @return Verdadero si estaba vivo en ese año; falso de lo contrario.
     */
    private boolean isAuthorAliveInYear(Author author, int year) {
        Integer birthYear = author.getBirthYear();
        Integer deathYear = author.getDeathYear();

        if (birthYear == null) {
            return false; // Si no se conoce el año de nacimiento, no se puede verificar
        }

        if (deathYear == null) {
            return year >= birthYear; // Si no se conoce el año de fallecimiento, asumimos que sigue vivo
        }

        return year >= birthYear && year <= deathYear;
    }
}
