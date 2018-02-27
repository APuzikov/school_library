package ru.mera.lib.service;

import org.hibernate.Session;
import org.springframework.util.Assert;
import ru.mera.lib.entity.Book;

public class BookService {

    private Session session;

    public BookService(Session session) {
        this.session = session;
    }

    public void saveBook(Book book) throws Exception {
        Assert.notNull(book, "Книга не может быть null!");
        Assert.hasText(book.getTitle(), "Нет названия книги!");
        Assert.hasText(book.getAuthor(), "Нет автора книги!");
        Assert.isTrue(book.getPublishYear() > 0, "Год издания не может быть отрицательным!");
        Assert.isTrue(book.getCount() >= 0, "Количество книг не может быть отрицательным!");
        session.beginTransaction();
        session.save(book);
        session.getTransaction().commit();
    }

    public void deleteBook(Book book) throws Exception {
        Assert.notNull(book, "Книга не может быть null!");
        session.beginTransaction();
        session.delete(book);
        session.getTransaction().commit();
    }
}
