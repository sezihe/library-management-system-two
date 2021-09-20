package com.danielezihe.unitTests;


import com.danielezihe.LibraryManager;
import com.danielezihe.hibernate.entity.Book;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.jupiter.api.*;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 19/09/2021
 */
public class LibraryManagerTest {
    private LibraryManager libraryManager;

    public static final Logger logger = LogManager.getLogger(LibraryManagerTest.class);

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        libraryManager = new LibraryManager();
        populateBooksDb();
    }

    @Test
    @DisplayName("Checks if Library Manager returns the correct requested book")
    void checksIfLibraryManagerReturnsTheCorrectRequestedBook() {
        String requestedBookId = "SN011";
        String requestedBookTitle = "Clean Code";

        Book book = libraryManager.getBook(requestedBookId);
        logger.info(book);

        Assertions.assertEquals(requestedBookTitle, book.getTitle());
    }

    @Test
    @DisplayName("Checks if Library Manager returns 'Book does not exist' when user trys requesting an Invalid Book")
    void shouldReturnBookDoesntExistWhenRequestingInvalidBook() {
        String requestedBookId = "SN500";

        var response = libraryManager.getBook(requestedBookId);

        Assertions.assertEquals("Book does not exist", response);
    }

    // UTILITIES
    private void populateBooksDb() {
        libraryManager.createBook("SN011", "Clean Code", "Robert C. Martin", 3);
    }
}
