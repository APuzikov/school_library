package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.util.Assert;
import ru.mera.lib.entity.Pupil;

public class PupilService {
    private Session session;

    public PupilService(Session session) {
        this.session = session;
    }

    public void savePupil(Pupil pupil) throws Exception{
        Assert.notNull(pupil, "Ученик не может быть null!");
        Assert.hasText(pupil.getName(), "Ученик без имени!");
        Assert.isTrue(pupil.getClassNumber() > 0 && pupil.getClassNumber() < 11,
                "Такого номера класса нет!");
        session.beginTransaction();
        session.save(pupil);
        session.getTransaction().commit();
    }

    public void deletePupil(Pupil pupil) throws Exception{
        Assert.notNull(pupil, "Ученик не может быть null!");

        session.beginTransaction();
        Query query1 = session.createQuery("delete RecordCard where pupilId = " + pupil.getId() +
                " and returnDate <> null");
        query1.executeUpdate();
        session.delete(pupil);
        session.getTransaction().commit();
    }
}
