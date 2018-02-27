package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.mera.lib.entity.Book;

import java.util.List;

import static org.junit.Assert.*;

public class BookServiceTest {

    private SessionFactory factory;
    private Session session;
    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
        bookService = new BookService(session);
    }

    @After
    public void tearDown() throws Exception {
        session.close();
        factory.close();
    }

    @Test
    public void saveBook() throws Exception {
        Book book = new Book();
        book.setTitle("фываываыав");
        book.setAuthor("safsfd");
        book.setPublishYear(1234);
        book.setCount(3);
        bookService.saveBook(book);

        Query query = session.createQuery("from Book where id = " + book.getId());
        Book book1 = (Book)query.getSingleResult();

        assertEquals(book.getTitle(), book1.getTitle());
        assertEquals(book.getAuthor(), book1.getAuthor());
        assertEquals(book.getId(), book1.getId());
        assertEquals(book.getPublishYear(), book1.getPublishYear());
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveBookNull() throws Exception {
        Book book = null;
        bookService.saveBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveBookNoTitle() throws Exception {
        Book book = new Book();
        bookService.saveBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveBookNoAuthor() throws Exception {
        Book book = new Book();
        book.setTitle("фываываыав");
        bookService.saveBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveBookNegativePublishYear() throws Exception {
        Book book = new Book();
        book.setTitle("фываываыав");
        book.setAuthor("safsfd");
        book.setPublishYear(-1234);
        bookService.saveBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveBookNegativeCount() throws Exception {
        Book book = new Book();
        book.setTitle("фываываыав");
        book.setAuthor("safsfd");
        book.setPublishYear(1234);
        book.setCount(-3);
        bookService.saveBook(book);
    }

    @Test
    public void deleteBook() throws Exception {
        Book book = new Book();

        book.setTitle("фываываыав");
        book.setAuthor("safsfd");
        book.setPublishYear(1234);
        book.setCount(3);
        bookService.saveBook(book);
        bookService.deleteBook(book);

        Query query = session.createQuery("from Book");
        List<Book> books = query.getResultList();

        books.forEach(b -> assertNotEquals(b, book));
    }
}