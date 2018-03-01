package ru.mera.lib.manager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.mera.lib.entity.Password;
import ru.mera.lib.service.PasswordService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class PasswordManager {

    private Session session;
    private BufferedReader reader;
    private PasswordService passwordService;

    public PasswordManager(Session session, BufferedReader reader) {
        this.session = session;
        this.reader = reader;
        passwordService = new PasswordService(session);
    }

    private Password getPassword(String password){   //возвращает пароль, если он есть в БД, иначе null
        Query query = session.createQuery("from Password");
        List<Password> passwords = query.getResultList();
        for (Password p : passwords){
            if (p.getHashPassword() == password.hashCode()) return p;
        }
        return null;
    }

    public void addPassword() throws IOException{   //добавление пароля в БД
        System.out.println("");
        System.out.println("Введите пароль для добавления:");
        String passwd = reader.readLine();
        if (getPassword(passwd) == null){
            Password password = new Password();
            password.setHashPassword(passwd.hashCode());
            password.setEnable(true);
            try {
                passwordService.savePassword(password);
                System.out.println("");
                System.out.println("Пароль успешно добавлен!");
            } catch (Exception e){
                System.out.println("Ошибка: " + e.getMessage());
            }
        } else System.out.println("Пароль уже существует!");
    }

    public void enablePassword() throws IOException{
        System.out.println("");
        System.out.println("Введите пароль для его активации:");
        String passwd = reader.readLine();
        Password password = getPassword(passwd);
        if (password != null){
            if (!password.isEnable()){
                password.setEnable(true);
                try {
                    passwordService.savePassword(password);
                    System.out.println("");
                    System.out.println("Пароль успешно активирован!");
                } catch (Exception e){
                    System.out.println("Ошибка: " + e.getMessage());
                }
            } else System.out.println("Пароль уже активен!");
        } else System.out.println("Пароль не найден!");
    }

    public void disablePassword() throws IOException{
        System.out.println("");
        System.out.println("Введите пароль для отключения:");
        String passwd = reader.readLine();
        Password password = getPassword(passwd);
        if (password !=null){
            if (password.isEnable()){
                password.setEnable(false);
                try {
                    passwordService.savePassword(password);
                    System.out.println("");
                    System.out.println("Пароль успешно отключен!");
                } catch (Exception e){
                    System.out.println("Ошибка: " + e.getMessage());
                }
            } else System.out.println("Пароль уже отключен!");
        }else System.out.println("Пароль не найден!");
    }
}
