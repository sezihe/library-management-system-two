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
public class LibraryManagerTest {
    private LibraryManager libraryManager;
    private Main mainClass;

    @BeforeEach
    void setUp() {
        libraryManager = new LibraryManager();
    }

    @Test
    @DisplayName("Checks if Library Manager returns the correct requested book")
    void checksIfLibraryManagerReturnsTheCorrectRequestedBook() {
        String requestedBookId = "SN011";
        String requestedBookTitle = "Clean Code";

        Book book = library.giveOutBook(requestedBookId, "Daniel");

        Assertions.assertEquals(requestedBookTitle, book.getTitle());
    }


    // UTILITIES
    private void populateStudentsDb() {
        mainClass.createStudent(3, "Ezihe", 23);
        mainClass.createStudent(4, "Daniel", 25);
    }

    private void populateBooksDb() {
        libraryManager.createBook("SN011", "Clean code", "Robert C. Martin", 3);
    }
}
