package com.danielezihe.unitTests;

import com.danielezihe.Library;
import com.danielezihe.LibraryManager;
import com.danielezihe.Main;
import com.danielezihe.hibernate.entity.Book;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
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

    public static final Logger logger = LogManager.getLogger(LibraryManagerTest.class);

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        library = new Library();
        libraryManager = new LibraryManager();
        populateStudentsDb();
        populateBooksDb();
    }

    @Test
    @DisplayName("Checks if a library returns a requested book")
    void checksIfALibraryReturnsARequestedBook() {
        String requestedBookId = "SN011";
        String requestedBookTitle = "Clean Code";

        Book book = library.giveOutBook(requestedBookId, "Daniel");
        logger.info(book);

        Assertions.assertEquals(requestedBookTitle, book.getTitle());
    }

    @Test
    @DisplayName("Checks if A Library returns 'Not a Student' if requesting user is not a student.")
    void checksIfALibraryReturnsNotAStudentIfRequestingUserIsNotAStudent() {
        String invalidStudentName = "Philips";
        var response = library.giveOutBook("SN011", invalidStudentName);
        logger.info(response);

        Assertions.assertEquals("ERROR: Not a Student", response);
    }

    @Test
    @DisplayName("Checks if A Library Returns 'Please return previously borrowed book' if a user attempts borrowing another book without returning previous one.")
    void shouldReturnPendingBookWhenAUserAttemptsReBorrowingWithoutReturning() {
        // Simulate borrowing a book
        library.giveOutBook("SN011", "Daniel");
        
        // try borrowing another book again
        var response = library.giveOutBook("SN011", "Daniel");

        Assertions.assertEquals("ERROR: Please return previously borrowed book!", response);
    }

    @Test
    @DisplayName("Checks if A Library Collects Back A Book if Student returns One.")
    void checksIfALibraryCollectsBackABookIfStudentReturnsOne() {
        String requestedBookId = "SN011";

        Book book = library.giveOutBook(requestedBookId, "Daniel");
        logger.info(book);

        // return a book
        var response = library.collectBackBook("Daniel");

        Assertions.assertEquals("Book returned Successfully", response);
    }

    private void populateStudentsDb() {
        Main.createStudent("Ezihe", 23);
        Main.createStudent("Daniel", 25);
    }

    private void populateBooksDb() {
        libraryManager.createBook("SN011", "Clean Code", "Robert C. Martin", 3);
        libraryManager.createBook("SN189", "Rework", "Jason Fried", 2);
    }
}
