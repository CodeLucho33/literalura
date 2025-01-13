package com.example.literalura;

import com.example.literalura.model.BooksResponse;
import com.example.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		exhibitMenu();
	}

	private void exhibitMenu() {
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;

		while (!exit) {
			System.out.println("===== Menú de Opciones =====");
			System.out.println("1. Buscar libros por título");
			System.out.println("2. Buscar libros por autor");
			System.out.println("3. Listar libros de un autor específico");
			System.out.println("4. Salir");
			System.out.print("Por favor, elige una opción: ");

			int option = scanner.nextInt();
			scanner.nextLine(); // Consume el salto de línea restante

			switch (option) {
				case 1:
					System.out.print("Ingrese el título del libro: ");
					String title = scanner.nextLine();
					BooksResponse responseByTitle = bookService.getBooks(title);
					System.out.println("Resultados: " + responseByTitle);
					break;

				case 2:
					System.out.print("Ingrese el nombre del autor: ");
					String author = scanner.nextLine();
					BooksResponse responseByAuthor = bookService.getBooks(author);
					System.out.println("Resultados: " + responseByAuthor);
					break;

				case 3:
					System.out.print("Ingrese el nombre del autor: ");
					String specificAuthor = scanner.nextLine();
					System.out.println("Resultados: " + bookService.getBooksBySpecificAuthor(specificAuthor));
					break;

				case 4:
					System.out.println("¡Gracias por usar Literalura! Hasta pronto. 👋");
					exit = true;
					break;

				default:
					System.out.println("Opción no válida. Por favor, intenta nuevamente.");
			}

			System.out.println();
		}

		scanner.close();
	}
}
