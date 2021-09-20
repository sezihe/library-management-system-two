package com.danielezihe;

import com.danielezihe.hibernate.entity.Book;
import com.danielezihe.hibernate.entity.BorrowLedger;
import com.danielezihe.hibernate.entity.Student;
import com.danielezihe.hibernate.util.HibernateUtil;
import org.h2.engine.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 19/09/2021
 */
public class Library {
    Transaction transaction;
    LibraryManager libraryManager;

    public Library() {
        libraryManager = new LibraryManager();
        transaction = null;
    }

    public <T> T giveOutBook(String requestedBookId, String borrowerName) {
        // check if Borrower is a student
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "from Student S WHERE S.name = '" + borrowerName + "'";
            Student student = session.createQuery(hql, Student.class).getSingleResult();

            if(student != null && student.getName().equals(borrowerName)) {
                // check if student is with a borrowed book
                if(isUserCurrentlyWithABorrowedBook(student)) {
                    return (T) "ERROR: Please return previously borrowed book!";
                }

                // get book and give it out
                var response = libraryManager.getBook(requestedBookId);

                if(response instanceof Book) {
                    addTransactionToBorrowLedger(student, (Book) response);
                }

                return (T) response;
            }
        } catch (NoResultException e) {
            return (T) "ERROR: Not a Student";
        } catch (Exception e) {

        }
        return (T) "ERROR: Not a Student";
    }

    public String collectBackBook(String borrowerName) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "from Student S WHERE S.name = '" + borrowerName + "'";
            Student student = session.createQuery(hql, Student.class).getSingleResult();

            String borrowLedgerHql = "FROM BorrowLedger B WHERE B.student_id = :student_id";
            Query query = session.createQuery(borrowLedgerHql);
            query.setParameter("student_id", student);
            List results = query.list();
            BorrowLedger trans = (BorrowLedger) results.get(0);

            Book book = trans.getBookID();
            if(student != null && student.getName().equals(borrowerName)) {
                // check if student is with a borrowed book
                if(isUserCurrentlyWithABorrowedBook(student)) {
                    if(deleteBorrowTransactionFromBorrowersLedger(student) && libraryManager.modifyBookQuantity(book.getId(), "INCREASE"))
                        return "Book returned Successfully";
                } else {
                    return "ERROR: You don't have any borrowed book";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return "An error occurred. You're not a Student";
    }

    private boolean deleteBorrowTransactionFromBorrowersLedger(Student student_id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "DELETE FROM BorrowLedger B WHERE B.student_id = :student_id";
            Query query = session.createQuery(hql);
            query.setParameter("student_id", student_id);

            int rowsAffected = query.executeUpdate();
            if(rowsAffected > 0)
                return true;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return false;
    }

    private void addTransactionToBorrowLedger(Student student, Book book) {
        BorrowLedger trans = new BorrowLedger(0, student, book);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(trans);

            transaction.commit();

        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    public boolean isUserCurrentlyWithABorrowedBook(Student student) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "from BorrowLedger B WHERE B.student_id = " + student.getId();
            BorrowLedger borrowLedger = session.createQuery(hql, BorrowLedger.class).getSingleResult();

            if(borrowLedger != null) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return false;
    }
}
