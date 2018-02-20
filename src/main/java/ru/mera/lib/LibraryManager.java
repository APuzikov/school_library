package ru.mera.lib;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.mera.lib.service.BookService;
import ru.mera.lib.service.PupilService;
import ru.mera.lib.service.RecordCardService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LibraryManager {

    private SessionFactory factory;
    private Session session;
    private BufferedReader reader;

    private  LibraryManager(BufferedReader reader){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
        this.reader = reader;
    }

    private boolean checkPassword() throws IOException{
        System.out.println("Введите пароль:");
        String password = reader.readLine();
        return password.equals("1234");
    }

    private void bookServiceMenu() throws IOException{
        System.out.println("");
        System.out.println("1. Показать все книги");
        System.out.println("2. Добавить новую книгу");
        System.out.println("3. Изменить книгу");
        System.out.println("4. Удалить книгу");
        System.out.println("0. Возврат в предыдущее меню");
        System.out.println("");

        BookService bookService = new BookService(session, reader);

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                bookService.showBooksList();
                break;
            }
            case "2":{
                bookService.addNewBook();
                break;
            }
            case "3":{
                bookService.updateBook();
                break;
            }
            case "4":{
                bookService.deleteBook();
                break;
            }
            case "0":{
                mainMenu();
            }
            default:{
                System.out.println("Неверный ввод!");
                bookServiceMenu();
            }
        }

        bookServiceMenu();

    }

    private void pupilServiceMenu() throws IOException{
        System.out.println("");
        System.out.println("1. Показать учеников");
        System.out.println("2. Добавить ученика");
        System.out.println("3. Изменить ученика");
        System.out.println("4. Удалить ученика (Предупреждение: будет удалена история выдачи книг этому ученику!)");
        System.out.println("0. Возврат в предыдущее меню");
        System.out.println("");

        PupilService pupilService = new PupilService(session, reader);

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                pupilService.showPupilsMenu();
                break;
            }
            case "2":{
                pupilService.addNewPupil();
                break;
            }
            case "3":{
                pupilService.updatePupil();
                break;
            }
            case "4":{
                pupilService.deletePupil();
                break;
            }
            case "0":{
                mainMenu();
                break;
            }
            default:{
                System.out.println("Неверный ввод!");
                pupilServiceMenu();
            }
        }
        pupilServiceMenu();
    }

    private void recordCardServiceMenu() throws IOException{
        System.out.println("");
        System.out.println("1. Выдать книгу");
        System.out.println("2. Принять книгу");
        System.out.println("3. Ученики, получившие книги");
        System.out.println("4. Книги, которые не возвращены");
        System.out.println("5. История выдач");
        System.out.println("0. Возврат в предыдущее меню");
        System.out.println("");

        RecordCardService recordCardService = new RecordCardService(session, reader);

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                recordCardService.giveBook();
                break;
            }
            case "2":{
                recordCardService.returnBook();
                break;
            }
            case "3":{
                recordCardService.listPupilsWithBook();
                break;
            }
            case "4":{
                recordCardService.listReceivedBooks();
                break;
            }
            case "5":{
                recordCardService.recordCardsHistory();
                break;
            }
            case "0":{
                mainMenu();
                break;
            }
            default:{
                System.out.println("Неверный ввод!");
                recordCardServiceMenu();
            }
        }
        recordCardServiceMenu();
    }

    private void mainMenu() throws IOException{
        System.out.println("");
        System.out.println("Меню:");
        System.out.println("1. Книги");
        System.out.println("2. Ученики");
        System.out.println("3. Учетные карточки");
        System.out.println("0. Завершить работу");
        System.out.println("");
        System.out.println("Для выбора действия, введите номер пункта меню:");
        System.out.println("");

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                bookServiceMenu();
                break;
            }
            case "2":{
               pupilServiceMenu();
                break;
            }
            case "3":{
                recordCardServiceMenu();
                break;
            }
            case "0":{
                System.out.println("Спасибо за внимание, до новых встреч!");
                System.exit(0);
                break;
            }
            default:{
                System.out.println("Неверный ввод!");
            }
        }

    }

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LibraryManager libraryManager = new LibraryManager(reader);

        try {
            if(libraryManager.checkPassword()){
            libraryManager.mainMenu();
            reader.close();
            } else System.out.println("Не верный пароль!");

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            libraryManager.session.close();
            libraryManager.factory.close();
        }
    }
}
