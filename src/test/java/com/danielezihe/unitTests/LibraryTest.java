package com.danielezihe.unitTests;

import com.danielezihe.hibernate.entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 19/09/2021
 */
public class LibraryTest {
    private Library library;
    private LibraryManager libraryManager;
    private Main mainClass;


    @BeforeEach
    void setUp() {
        library = new Library();
        libraryManager = new LibraryManager();
        mainClass = new Main();
        populateStudentsDb();
        populateBooksDb();
    }

    @Test
    @DisplayName("Checks if a library returns a requested book")
    void checksIfALibraryReturnsARequestedBook() {
        String requestedBookId = "SN011";
        String requestedBookTitle = "Clean Code";

        Book book = library.giveOutBook(requestedBookId, "Daniel");

        Assertions.assertEquals(requestedBookTitle, book.getTitle());
    }

    @Test
    @DisplayName("Checks if A Library returns 'Not a Student' if requesting user is not a student.")
    void checksIfALibraryReturnsNotAStudentIfRequestingUserIsNotAStudent() {
        String invalidStudentName = "Philips";
        var response = library.giveOutBook("SN011", invalidStudentName);

        Assertions.assertEquals("Not a Student", response);
    }

    @Test
    @DisplayName("Checks if A Library Returns 'Pending Book' if a user attempts borrowing another book without returning previous one.")
    void shouldReturnPendingBookWhenAUserAttemptsReBorrowingWithoutReturning() {
        // Simulate borrowing a book
        library.giveOutBook("SN011", "Daniel");

        // try borrowing another book again
        var response = library.giveOutBook("SN011", "Daniel");

        Assertions.assertEquals("Not a Student", response);
    }

    private void populateStudentsDb() {
        mainClass.createStudent(3, "Ezihe", 23);
        mainClass.createStudent(4, "Daniel", 25);
    }

    private void populateBooksDb() {
        libraryManager.createBook("SN011", "Clean code", "Robert C. Martin", 3);
    }
}
