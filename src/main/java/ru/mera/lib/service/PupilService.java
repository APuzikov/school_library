package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.mera.lib.entity.Pupil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PupilService {
    private Session session;
    private BufferedReader reader;

    public PupilService(Session session, BufferedReader reader) {
        this.session = session;
        this.reader = reader;
    }

    private boolean checkCountInput(String input){
        Pattern regex = Pattern.compile("\\d+");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    private boolean checkClassNameInput(String input){
        Pattern regex = Pattern.compile("[A-Za-zА-Яа-я]?");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    private boolean checkClassNumberInput(String input){
        Pattern regex = Pattern.compile("\\d{1,2}");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    private String inputName() throws IOException{
        System.out.println("Введите имя ученика:");
        return reader.readLine();
    }

    private String inputClassName() throws IOException{
        String className;
        while (true){
            System.out.println("Введите букву класса:");
            className = reader.readLine();
            if (checkClassNameInput(className)){
                break;
            } else System.out.println("Неверный ввод");
        }
        return className;
    }

    private int inputClassNumber() throws IOException{
        int classNumber;
        while (true){
            System.out.println("Введите класс:");
            String input = reader.readLine();
            if (checkClassNumberInput(input)){
                classNumber = Integer.parseInt(input);
                break;
            } else System.out.println("Неверный ввод");
        }
        return classNumber;
    }


    int inputId(String message) throws IOException{
        int id;
        while (true) {
            System.out.println("Выберите номер ученика для " + message);
            String input = reader.readLine();
            if (checkCountInput(input)){
                id = Integer.parseInt(input);
                break;
            } else System.out.println("Неверный ввод!");
        }
        return id;
    }



    public void showPupilsList(){
        Query query = session.createQuery("from Pupil");
        List<Pupil> pupils = query.getResultList();

        pupils.forEach(pupil ->
                System.out.println("Номер: " + pupil.getId()+
                "        Имя: " + pupil.getName() +
                "        Класс: " + pupil.getClassNumber() +
                "        Буква класса: " + pupil.getClassName()));
    }

    Pupil showOnePupil(int id){
        Pupil pupil;
        Query query = session.createQuery("from Pupil where id = " + id);
//        query.setParameter(0, id);
        try {
            pupil = (Pupil) query.getSingleResult();
        } catch (Exception e){
            return null;
        }
        System.out.println("Номер: " + pupil.getId() +
                "        Имя: " + pupil.getName() +
                "        Класс: " + pupil.getClassNumber() +
                "        Буква класса: " + pupil.getClassName());
        return pupil;
    }

    public void addNewPupil() throws IOException{
        Pupil pupil = new Pupil();

        session.beginTransaction();
        pupil.setName(inputName());
        pupil.setClassNumber(inputClassNumber());
        pupil.setClassName(inputClassName());
        session.save(pupil);
        session.getTransaction().commit();
        System.out.println("Ученик успешно добавлен!");

    }

    public void updatePupil() throws IOException{
        showPupilsList();
        int id = inputId("изменения:");
        Pupil pupil = showOnePupil(id);

        if (pupil != null){
            System.out.println("Выберите, что вы хотите изменить в ученике:");
            System.out.println("1. Имя");
            System.out.println("2. Класс");
            System.out.println("3. Букву класса");
            String input = reader.readLine();
            switch (input){
                case "1":{
                    pupil.setName(inputName());
                    break;
                }
                case "2":{
                    pupil.setClassNumber(inputClassNumber());
                }
                case "3":{
                    pupil.setClassName(inputClassName());
                    break;
                }
                default:{
                    System.out.println("Неверный ввод!");
                }
            }

            session.beginTransaction();
            pupil.setId(id);
            session.save(pupil);
            session.getTransaction().commit();
            System.out.println("Ученик успешно изменен!");
        } else System.out.println("Ученик не найден!");
    }

    public void deletePupil() throws IOException{
        showPupilsList();
        int id = inputId("удаления");
        Pupil pupil = showOnePupil(id);
        if (pupil != null){
            session.getTransaction();
            session.delete(pupil);
            session.getTransaction().commit();
            System.out.println("Ученик успешно удален!");
        } else System.out.println("Ученик не найден!");
    }
}
