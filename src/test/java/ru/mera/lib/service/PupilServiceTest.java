package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.mera.lib.entity.Pupil;

import java.util.List;

import static org.junit.Assert.*;

public class PupilServiceTest {

    private SessionFactory factory;
    private Session session;
    private PupilService pupilService;

    @Before
    public void setUp() throws Exception {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
        pupilService = new PupilService(session);
    }

    @After
    public void tearDown() throws Exception {
        session.close();
        factory.close();
    }

    @Test
    public void savePupil() throws Exception {
        Pupil pupil = new Pupil();
        pupil.setName("sdfsfsfdgsdfg");
        pupil.setClassNumber(4);
        pupil.setClassName("S");
        pupilService.savePupil(pupil);

        Query query = session.createQuery("from Pupil where id = " + pupil.getId());
        Pupil pupil1 = (Pupil)query.getSingleResult();

        assertEquals(pupil.getId(), pupil1.getId());
        assertEquals(pupil.getName(), pupil1.getName());
        assertEquals(pupil.getClassNumber(), pupil1.getClassNumber());
        assertEquals(pupil.getClassName(), pupil1.getClassName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void savePupilNull() throws Exception {
        Pupil pupil = null;
        pupilService.savePupil(pupil);
    }

    @Test(expected = IllegalArgumentException.class)
    public void savePupilNoName() throws Exception {
        Pupil pupil = new Pupil();
        pupilService.savePupil(pupil);
    }

    @Test(expected = IllegalArgumentException.class)
    public void savePupilNoClassNumber() throws Exception {
        Pupil pupil = new Pupil();
        pupil.setName("sdfsdfdsdf");
        pupilService.savePupil(pupil);
    }

    @Test
    public void deletePupil() throws Exception {
        Pupil pupil = new Pupil();
        pupil.setName("Unknown2");
        pupil.setClassNumber(5);
        pupil.setClassName("S");
        pupilService.savePupil(pupil);
        pupilService.deletePupil(pupil);

        Query query = session.createQuery("from Pupil");
        List<Pupil> pupils = query.getResultList();

        pupils.forEach(p -> assertNotEquals(p, pupil));

    }

}