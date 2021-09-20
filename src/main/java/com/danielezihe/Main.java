package com.danielezihe;

import com.danielezihe.hibernate.entity.Book;
import com.danielezihe.hibernate.entity.Student;
import com.danielezihe.hibernate.util.HibernateUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 19/09/2021
 */
public class Main {
    private static final int OPTION_1 = 1;
    private static final int OPTION_2 = 2;
    private static final int OPTION_3 = 3;
    private static final int OPTION_4 = 4;
    private static final int OPTION_5 = 5;

    public static final Logger logger = LogManager.getLogger(Main.class);

    static Scanner scanner = new Scanner(System.in);
    static Transaction transaction = null;

    static LibraryManager libraryManager;
    static Library library;
    public static void main(String[] args) {
        library = new Library();

        // Log4j
        PropertyConfigurator.configure("./src/main/log4j.properties");

        println("Welcome!");
        boolean isRunning = true;
        while (isRunning) {
            printOutOptions();
            int optionSelected = scanner.nextInt();
            scanner.nextLine();
            switch (optionSelected) {
                case OPTION_1 -> handleCreateStudentOption();
                case OPTION_2 -> handleCreateNewBookOption();
                case OPTION_3 -> handleBorrowBookOption();
                case OPTION_4 -> handleReturnBookOption();
                case OPTION_5 -> {
                    isRunning = false;
                }
                default -> println("Please select a valid option");
            }
        }
    }

    public static void createStudent(String name, int age) {
        Student student = new Student(0, name, age);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(student);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    static void handleCreateStudentOption() {
        println("Enter Student Name and Age in a single line seperated by a comma. Eg: John,22");
        String[] stuDetails = scanner.nextLine().split(",");

        createStudent(stuDetails[0], Integer.parseInt(stuDetails[1]));

        println("Student Created!\n");
    }

    static void handleCreateNewBookOption() {
        libraryManager = new LibraryManager();

        println("Enter book details in a single line seperated by commas. Eg: SN102,Clean Code,Robert C. Martin,3");
        String[] bookDetails = scanner.nextLine().split(",");

        libraryManager.createBook(bookDetails[0], bookDetails[1], bookDetails[2], Integer.parseInt(bookDetails[3]));

        println("Book created successfully!\n");
    }

    static void handleBorrowBookOption() {
        println("Enter book Id and Borrowers name seperated by a comma. Eg. SN102,John");
        String[] borrowRequest = scanner.nextLine().split(",");

        var response = library.giveOutBook(borrowRequest[0], borrowRequest[1]);

        if(response instanceof Book) {
            println("You have successfully borrowed Book with title '" + ((Book) response).getTitle() + "'\n");
        } else {
            println(response + "\n");
        }
    }

    static void handleReturnBookOption() {
        println("Enter Borrowers name. Eg. John");
        String borrowerName = scanner.nextLine();

        String response = library.collectBackBook(borrowerName);

        println(response + "\n");
    }


    static void printOutOptions() {
        String prompt = "Kindly choose from the following options\n" +
                "1. Create a new Student\n" +
                "2. Create a new Book\n" +
                "3. Borrow a Book\n" +
                "4. Return Borrowed Book\n" +
                "5. Quit";
        println(prompt);
    }

    private static <T> void println(T value) {
        System.out.println(value);
    }
}
