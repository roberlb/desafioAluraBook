package com.alura.literatura.Principal;

import com.alura.literatura.model.Author;
import com.alura.literatura.model.Book;
import com.alura.literatura.model.Datos;
import com.alura.literatura.repository.BookRepository;
import com.alura.literatura.service.ConsumeAPI;
import com.alura.literatura.service.ConvertData;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

public class Principal {
    int option = 0;
    boolean iteration = true;
    Scanner scanner = new Scanner(System.in);
    BookRepository bookRepository;
    String[] values = {"Buscar libro por titulo",
            "Listar libros registrados",
            "Listar autores registrados",
            "Listar autores vivos en un determinado año",
            "Listar libros por idioma",
            "Salir"};
    ConsumeAPI consumeAPI = new ConsumeAPI();
    ConvertData convertData = new ConvertData();
    String URL_BASE = "https://gutendex.com/books?search=";

    public Principal(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void menu() {
        while (iteration) {
            int i = 1;

            System.out.println("Bienvenidos\nSelecciona un numero:");
            for (String value : values) {
                System.out.printf("%s.- %s\n", i++, value);
            }
            System.out.print("Opcion: ");
            try {
                option = scanner.nextInt();
            } catch (InputMismatchException e) {
                option = 0;
            }

            switch (option) {
                case 1:
                    searchByTitle();
                    break;
                case 2:
                    getBooks();
                    break;
                case 3:
                    getAuthors();
                    break;
                case 4:
                    getAuthorsByYear();
                    break;
                case 5:
                    getBooksByLanguage();
                    break;
                default:
                    iteration = false;
                    System.out.println("Gracias por usar mi aplicacion");
                    break;
            }
        }


    }

    public void searchByTitle() {
        System.out.print("Ingresa un titulo que quieres buscar: ");
        String title = scanner.next();
        String json = consumeAPI.getData(URL_BASE+title.replace(" ","%20"));
        var data = convertData.getData(json, Datos.class);
        data.results().forEach(System.out::println);
        Book book;
        List<Author> authors;
        for (int i = 0; i < data.results().size(); i++) {
            book = new Book(data.results().get(i));
            authors = data.results().get(i).authors().stream().map(Author::new).toList();
            book.setAuthors(authors);
            try {
                bookRepository.save(book);
            }catch (DataIntegrityViolationException e){
                e.getCause();
            }

        }
    }

    private void getBooks() {
        bookRepository.findAllBooks().forEach(b-> System.out.printf("Titulo: %s\n",b.getTitle()));
    }

    private void getAuthors() {
        bookRepository.findAllAuthors().forEach(b-> System.out.printf("Autores: %s\n",b.getName()));
    }

    private void getAuthorsByYear() {
        System.out.print("Ingresa el año: ");
        String year = scanner.next();
        if(!bookRepository.findByAuthorAliveInAGiven(year).isEmpty()){
            bookRepository.findByAuthorAliveInAGiven(year).stream().forEach(author -> System.out.println(author.getName()));
        }
    }

    private void getBooksByLanguage() {
        bookRepository.findLanguages().forEach(b-> System.out.printf("Idiomas: %s\n",b));
    }
}
