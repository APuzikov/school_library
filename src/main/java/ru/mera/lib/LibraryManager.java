package ru.mera.lib;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.mera.lib.entity.Password;
import ru.mera.lib.manager.BookManager;
import ru.mera.lib.manager.PupilManager;
import ru.mera.lib.manager.RecordCardManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
        Query query = session.createQuery("from Password where enable = true");
        List<Password> passwords = query.getResultList();

        for (Password p : passwords){
            if (password.hashCode() == p.getHashPassword()) return true;
        }
        return false;
    }

    private void bookServiceMenu() throws IOException{
        System.out.println("");
        System.out.println("1. Показать все книги");
        System.out.println("2. Добавить новую книгу");
        System.out.println("3. Изменить книгу");
        System.out.println("4. Удалить книгу");
        System.out.println("0. Возврат в предыдущее меню");
        System.out.println("");

        BookManager bookManager = new BookManager(session, reader);

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                bookManager.showBooksList();
                break;
            }
            case "2":{
                bookManager.addNewBook();
                break;
            }
            case "3":{
                bookManager.updateBook();
                break;
            }
            case "4":{
                bookManager.removeBook();
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

        PupilManager pupilManager = new PupilManager(session, reader);

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                pupilManager.showPupilsMenu();
                break;
            }
            case "2":{
                pupilManager.addNewPupil();
                break;
            }
            case "3":{
                pupilManager.updatePupil();
                break;
            }
            case "4":{
                pupilManager.removePupil();
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

        RecordCardManager recordCardManager = new RecordCardManager(session, reader);

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                recordCardManager.giveBook();
                break;
            }
            case "2":{
                recordCardManager.returnBook();
                break;
            }
            case "3":{
                recordCardManager.listPupilsWithBook();
                break;
            }
            case "4":{
                recordCardManager.listReceivedBooks();
                break;
            }
            case "5":{
                recordCardManager.recordCardsHistory();
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
                mainMenu();
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
