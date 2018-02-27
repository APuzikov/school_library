package ru.mera.lib.manager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.mera.lib.entity.Pupil;
import ru.mera.lib.entity.RecordCard;
import ru.mera.lib.service.PupilService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PupilManager {
    private Session session;
    private BufferedReader reader;
    private PupilService pupilService;

    public PupilManager(Session session, BufferedReader reader) {
        this.session = session;
        this.reader = reader;
        pupilService = new PupilService(session);

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
        String name;
        while (true) {
            System.out.println("Введите имя ученика:");
            name = reader.readLine();
            if (!name.equals("")) break;
            System.out.println("Неверный ввод!");
        }
        return name;
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



    List<Pupil> showPupilsList(){
        Query query = session.createQuery("from Pupil");
        try {
            List<Pupil> pupils = query.getResultList();

            pupils.forEach(pupil -> {
                System.out.println("Номер: " + pupil.getId() +
                        "        Имя: " + pupil.getName() +
                        "        Класс: " + pupil.getClassNumber() +
                        "        Буква класса: " + pupil.getClassName());
                System.out.println("------------------------------------------------------");
            });
            return pupils;

        } catch (Exception e) {
            return null;
        }

    }

    Pupil showOnePupil(int id){
        Pupil pupil;
        Query query = session.createQuery("from Pupil where id = " + id);
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

    private List<Pupil> findPupilByName(String name){
        List<Pupil> pupils;
        Query query = session.createQuery("from Pupil where name like '%" + name + "%'");
        try {
            pupils = query.getResultList();
            return pupils;
        } catch (Exception e){
            return null;
        }
    }

    private List<Pupil> findPupilByClassNumber(int classNumber){
        List<Pupil> pupils;
        Query query = session.createQuery("from Pupil where classNumber = " + classNumber);
        try {
            pupils = query.getResultList();
            return pupils;
        } catch (Exception e){
            return null;
        }
    }

    public void addNewPupil() throws IOException{
        Pupil pupil = new Pupil();
        pupil.setName(inputName());
        pupil.setClassNumber(inputClassNumber());
        pupil.setClassName(inputClassName());

        try {
            pupilService.savePupil(pupil);
            System.out.println("Ученик успешно добавлен!");
        }catch (Exception e){
            System.out.println("Ошибка: " + e.getMessage());
        }
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

            pupil.setId(id);
            try {
                pupilService.savePupil(pupil);
                System.out.println("Ученик успешно изменен!");
            }catch (Exception e){
                System.out.println("Ошибка: " + e.getMessage());
            }
        } else System.out.println("Ученик не найден!");
    }

    public void removePupil() throws IOException{
        showPupilsList();
        int id = inputId("удаления");
        Pupil pupil = showOnePupil(id);

        if (pupil != null){
            Query query = session.createQuery("from RecordCard where pupilId = " + pupil.getId() +
                    " and returnDate = null");
                List<RecordCard> recordCards = query.getResultList();

                if (recordCards.size() > 0) {
                    System.out.println("Ученик имеет несданные книги!");
                } else {
                    try {
                        pupilService.deletePupil(pupil);
                        System.out.println("Ученик успешно удален!");
                    } catch (Exception e){
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }
        } else System.out.println("Ученик не найден!");
    }

    public void showPupilsMenu() throws IOException {
        System.out.println("");
        System.out.println("1. Показать список всех учеников");
        System.out.println("2. Найти ученика по имени");
        System.out.println("3. Найти ученика по id");
        System.out.println("4. Найти ученика по номеру класса");
        System.out.println("");

        String choice = reader.readLine();
        switch (choice){
            case "1":{
                showPupilsList();
                break;
            }
            case "2":{
                System.out.println("");
                List<Pupil> pupils = findPupilByName(inputName());
                System.out.println("(или часть имени)");
                if (pupils != null && pupils.size() > 0) {
                    pupils.forEach(pupil -> showOnePupil(pupil.getId()));
                } else System.out.println("Ничего не найдено!");
                break;
            }
            case "3":{
                int pupilId = inputId("просмотра:");
                Pupil pupil = showOnePupil(pupilId);
                if (pupil == null) {
                    System.out.println("Ученик не найден!");
                }
                break;
            }
            case "4":{
                System.out.println("");
                List<Pupil> pupils = findPupilByClassNumber(inputClassNumber());
                if (pupils != null && pupils.size() > 0){
                    pupils.forEach(pupil -> showOnePupil(pupil.getId()));
                } else System.out.println("Ученики не найдены!");
                break;
            }
            default:{
                System.out.println("Неверный ввод!");
            }
        }
    }
}
