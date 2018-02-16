package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.mera.lib.entity.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookService {

    private Session session;
    private BufferedReader reader;

    public BookService(Session session, BufferedReader reader) {
        this.reader = reader;
        this.session = session;
    }

    private boolean checkPublishYearInput(String input){
        Pattern regex = Pattern.compile("\\d{4}");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    private boolean checkCountInput(String input){
        Pattern regex = Pattern.compile("\\d+");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    private boolean checkClassNumberInput(String input){
        Pattern regex = Pattern.compile("\\d{1,2}");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    Book showOneBook(int id){
        Book book;
            Query query = session.createQuery("from Book where id = " + id);
//            query.setParameter(0, id);
            try {
                book = (Book) query.getSingleResult();
            } catch (Exception e){
                return null;
            }
        System.out.println("Номер: " + book.getId() +
                "        Название: " + book.getTitle() +
                "        Автор: " + book.getAuthor() +
                "        Класс: " + book.getClassNumber() +
                "        Год издания: " + book.getPublishYear() +
                "        Количество: " + book.getCount());
        return book;
    }

    public void showBooksList(){

        Query query = session.createQuery("from Book");
        List<Book> books = query.getResultList();

        books.forEach(book ->
            System.out.println("Номер: " + book.getId() +
            "        Название: " + book.getTitle() +
            "        Автор: " + book.getAuthor() +
            "        Класс: " + book.getClassNumber() +
            "        Год издания: " + book.getPublishYear() +
            "        Количество: " + book.getCount()));

    }

    int inputId(String message) throws IOException{
        int id;
        while (true) {
            System.out.println("Выберите номер книги для " + message);
            String input = reader.readLine();
            if (checkCountInput(input)){
                id = Integer.parseInt(input);
                break;
            } else System.out.println("Неверный ввод!");
        }
        return id;
    }

    private String inputTitle() throws IOException{
        System.out.println("Введите название:");
        return reader.readLine();
    }

    private String inputAuthor() throws IOException{
        System.out.println("Введите автора:");
        return reader.readLine();
    }

    private int inputClassNumber() throws IOException{
        int classNumber;
        while (true) {
            System.out.println("Введите класс:");
            String input = reader.readLine();
            if (checkClassNumberInput(input)) {
                classNumber = Integer.parseInt(input);
                break;
            }
            System.out.println("Неверный ввод!");
        }
        return classNumber;
    }

    private int inputPublishYear() throws IOException{
        int publishYear;
        while (true) {
            System.out.println("Введите год издания:");
            String input = reader.readLine();
            if (checkPublishYearInput(input)) {
                publishYear = Integer.parseInt(input);
                break;
            }
            System.out.println("Неверный ввод!");
        }
        return publishYear;
    }

    private int inputCount() throws IOException{
        int count;
        while (true){
            System.out.println("Введите количство книг:");
            String input = reader.readLine();
            if (checkCountInput(input)){
                count = Integer.parseInt(input);
                break;
            }
            System.out.println("Неверный ввод!");
        }
        return count;
    }

    public void addNewBook() throws IOException{

        Book book = new Book();
        book.setTitle(inputTitle());
        book.setAuthor(inputAuthor());
        book.setClassNumber(inputClassNumber());
        book.setPublishYear(inputPublishYear());
        book.setCount(inputCount());

        session.beginTransaction();
        session.save(book);
        session.getTransaction().commit();

        System.out.println("Книга успешно сохранена в БД!");
    }

    public void updateBook() throws IOException{

        showBooksList();
        int id = inputId("изменения:");

        Book book = showOneBook(id);

        if (book != null) {
            showUpdateMenu();
            String choice = reader.readLine();
            switch (choice){
                case "1":{
                    book.setTitle(inputTitle());
                    break;
                }
                case "2":{
                    book.setAuthor(inputAuthor());
                    break;
                }
                case "3":{
                    book.setClassNumber(inputClassNumber());
                    break;
                }
                case "4":{
                    book.setPublishYear(inputPublishYear());
                    break;
                }
                case "5":{
                    book.setCount(inputCount());
                    break;
                }
                default:{
                    System.out.println("Неверный ввод!");
                }

            }

            session.beginTransaction();
            book.setId(id);
            session.save(book);
            session.getTransaction().commit();
            System.out.println("Книга успешно изменена!");
        } else System.out.println("Книга не найдена!");
    }

    public void deleteBook() throws IOException{
        showBooksList();
        int id = inputId("удаления:");

        Book book = showOneBook(id);

        if (book != null){
            session.beginTransaction();
            session.delete(book);
            session.getTransaction().commit();
            System.out.println("Книга успешно удалена!");
        } else System.out.println("Книга не найдена!");
    }

    private void showUpdateMenu(){
        System.out.println("Вберите, что вы хотите изменить в книге:");
        System.out.println("1. Название");
        System.out.println("2. Автор");
        System.out.println("3. Класс");
        System.out.println("4. Год издания");
        System.out.println("5. Количество");

    }
}
