package com.danielezihe.hibernate.entity;

import javax.persistence.*;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 20/09/2021
 */
@Entity
@Table(name = "borrow_ledger")
public class BorrowLedger {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(unique = true, name = "student_id", referencedColumnName = "id")
    private Student student_id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book_id;

    public BorrowLedger(int id, Student student_id, Book book_id) {
        this.id = id;
        this.student_id = student_id;
        this.book_id = book_id;
    }

    public BorrowLedger() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudentID() {
        return student_id;
    }

    public void setStudentID(Student student_id) {
        this.student_id = student_id;
    }

    public Book getBookID() {
        return book_id;
    }

    public void setBookID(Book book_id) {
        this.book_id = book_id;
    }
}
