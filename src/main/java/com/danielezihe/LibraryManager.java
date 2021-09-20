package com.danielezihe;

import com.danielezihe.hibernate.entity.Book;
import com.danielezihe.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 19/09/2021
 */
public class LibraryManager {
    Transaction transaction;

    public LibraryManager() {
        transaction = null;
    }


    public <T> T getBook(String requestedBookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "from Book B WHERE B.id = '" + requestedBookId + "'";
            Book book = session.createQuery(hql, Book.class).getSingleResult();

            if(book != null && book.getQuantity() > 0) {
                if(modifyBookQuantity(requestedBookId, "DECREASE")) {
                    return (T) book;
                }
            }
        } catch (NoResultException e) {
            return (T) "Book does not exist";
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return (T) "Book is out of stock";
    }

    public boolean modifyBookQuantity(String requestedBookId, String action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Book book = session.get(Book.class, requestedBookId);

            switch(action) {
                case "DECREASE" -> book.setQuantity(book.getQuantity()-1);
                case "INCREASE" -> book.setQuantity(book.getQuantity()+1);
            }

            session.saveOrUpdate(book);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    public void createBook(String id, String title, String author, int quantity) {
        Book book = new Book(id, title, author, quantity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(book);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
