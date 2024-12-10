package com.alura.literatura.repository;

import com.alura.literatura.model.Author;
import com.alura.literatura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query(value = "SELECT b FROM Book b")
    List<Book> findAllBooks();
    @Query(value = "SELECT a FROM Book b JOIN b.authors a")
    List<Author> findAllAuthors();
    @Query(value = "SELECT b.languages FROM Book b GROUP BY b.languages")
    List<String> findLanguages();
    @Query(value = "SELECT a FROM Book b JOIN b.authors a WHERE a.death_year > :year")
    List<Author> findByAuthorAliveInAGiven(String year);
}
