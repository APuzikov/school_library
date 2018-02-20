package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.mera.lib.entity.Book;
import ru.mera.lib.entity.Pupil;
import ru.mera.lib.entity.RecordCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordCardService {
    private Session session;
    private BufferedReader reader;
    private BookService bookService;
    private PupilService pupilService;

    public RecordCardService(Session session, BufferedReader reader) {
        this.session = session;
        this.reader = reader;
        bookService = new BookService(session, reader);
        pupilService = new PupilService(session, reader);
    }

    private boolean recordCardExists(int pupilId, int bookId){
        Query query = session.createQuery("from RecordCard where pupilId=" + pupilId +
                " and bookId=" + bookId + " and returnDate = null");

        try {
            RecordCard recordCard = (RecordCard) query.getSingleResult();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private Book findBookById(int bookId){
        Query query = session.createQuery("from Book where id=" + bookId);
        try {
            return (Book)query.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }

    private Pupil findPupilById(int pupilId){
        Query query = session.createQuery("from Pupil where id=" + pupilId);
        try {
            return (Pupil) query.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }

    private List<RecordCard> findRecordCardsByPupilId(int pupilId){
        List<RecordCard> recordCards;
        Query query = session.createQuery("from RecordCard where pupilId=" + pupilId +
                                             " and returnDate=null");
        try {
            recordCards = query.getResultList();
            if (recordCards.size() > 0) return recordCards;
            return null;
        } catch (Exception e){
            return null;
        }
    }

    private RecordCard findOneRecordCard(int pupilId, int bookId){
        Query query = session.createQuery("from RecordCard where pupilId=" + pupilId +
                                             " and bookId=" + bookId +
                                             " and returnDate=null");
        try {
            return (RecordCard)query.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }

    public void giveBook() throws IOException{
        pupilService.showPupilsList();
        int pupilId = pupilService.inputId("выдачи книги:");
        Pupil pupil = findPupilById(pupilId);

        bookService.showBooksList();
        int bookId = bookService.inputId("выдачи:");
        Book book = findBookById(bookId);

        if (!recordCardExists(pupilId, bookId)){
            if(pupil != null) {
                if (book !=null) {
                    if (book.getCount() > 0) {
                        session.beginTransaction();
                        RecordCard recordCard = new RecordCard();
                        SimpleDateFormat dateFormat = new SimpleDateFormat();

                        recordCard.setBookId(bookId);
                        recordCard.setPupilId(pupilId);
                        recordCard.setReceiveDate(dateFormat.format(new Date()));
                        session.save(recordCard);

                        book.setCount(book.getCount() - 1);
                        session.save(book);
                        session.getTransaction().commit();

                        System.out.println("Книга: ");
                        bookService.showOneBook(bookId);
                        System.out.println("Успешно выдана ученику:");
                        pupilService.showOnePupil(pupilId);

                    } else System.out.println("Книги нет в наличии!");
                } else System.out.println("Книга не найдена!");
            } else System.out.println("Ученик не найден!");
        } else System.out.println("Книга уже выдана ученику!");
    }

    public void returnBook() throws IOException {
        pupilService.showPupilsList();
        int pupilId = pupilService.inputId("возврата книги:");
        Pupil pupil = findPupilById(pupilId);
        List<RecordCard> recordCards = findRecordCardsByPupilId(pupilId);
        int bookId;
        RecordCard recordCard;
        if (pupil != null) {
            if (recordCards != null) {
                recordCards.forEach(recCard -> bookService.showOneBook(recCard.getBookId()));
                bookId = bookService.inputId("возврата");
                recordCard = findOneRecordCard(pupilId, bookId);
                if (recordCard != null){
                    session.beginTransaction();
                    SimpleDateFormat dateFormat = new SimpleDateFormat();
                    recordCard.setReturnDate(dateFormat.format( new Date()));
                    session.save(recordCard);

                    Book book = findBookById(bookId);
                    book.setCount(book.getCount() + 1);
                    session.save(book);
                    session.getTransaction().commit();

                    System.out.println("Ученик:");
                    pupilService.showOnePupil(pupilId);
                    System.out.println("Успешно взвратил книгу: ");
                    bookService.showOneBook(bookId);

                } else System.out.println("Такая книга ученику не выдавалась!");
            } else System.out.println("Этому ученику книги не выдавались!");
        } else System.out.println("Ученик не найден!");
    }

    public void listPupilsWithBook() {
        Query query = session.createQuery("from RecordCard where returnDate=null");
        List<RecordCard> recordCards = query.getResultList();

        recordCards.forEach(recordCard -> {
            System.out.println("Ученик:");
            pupilService.showOnePupil(recordCard.getPupilId());
            System.out.println(recordCard.getReceiveDate() + "   получил книгу:" );
            bookService.showOneBook(recordCard.getBookId());
            System.out.println("----------------------------------------------------------------------------------------");
        });
    }

    public void listReceivedBooks() {
        Query query = session.createQuery("from RecordCard where returnDate=null");
        List<RecordCard> recordCards = query.getResultList();

        recordCards.forEach(recordCard -> {
            System.out.println("Книга");
            bookService.showOneBook(recordCard.getBookId());
            System.out.println("Выдана ученику:");
            pupilService.showOnePupil(recordCard.getPupilId());
            System.out.println("-------------------------------------------------------------------------------------");
        });
    }

    public void recordCardsHistory(){
        Query query = session.createQuery("from RecordCard where returnDate<>null");
        List<RecordCard> recordCards = query.getResultList();

        recordCards.forEach(recordCard -> {
            System.out.println("Книга");
            bookService.showOneBook(recordCard.getBookId());
            System.out.println("Была выдана: " + recordCard.getReceiveDate() + "   и возвращена: " + recordCard.getReturnDate());
            System.out.println("Ученик:");
            pupilService.showOnePupil(recordCard.getPupilId());
            System.out.println("-------------------------------------------------------------------------------------");
        });
    }
}
