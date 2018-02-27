package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.mera.lib.entity.RecordCard;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class RecordCardServiceTest {

    private SessionFactory factory;
    private Session session;
    private RecordCardService recordCardService;


    @Before
    public void setUp() throws Exception {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
        recordCardService = new RecordCardService(session);
    }

    @After
    public void tearDown() throws Exception {
    session.close();
    factory.close();
    }

    @Test
    public void saveRecordCard() throws Exception {
        RecordCard recordCard = new RecordCard();
        recordCard.setPupilId(5);
        recordCard.setBookId(6);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        recordCard.setReceiveDate(dateFormat.format(new Date()));
        recordCardService.saveRecordCard(recordCard);

        Query query = session.createQuery("from RecordCard where id = " +  recordCard.getId());
        RecordCard recordCard1 = (RecordCard) query.getSingleResult();

        assertEquals(recordCard, recordCard1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveRecordCardNull() throws Exception{
        RecordCard recordCard = null;

        recordCardService.saveRecordCard(recordCard);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveRecordCardNoReceiveDate() throws Exception{
        RecordCard recordCard = new RecordCard();
        recordCard.setPupilId(5);
        recordCard.setBookId(6);
        recordCardService.saveRecordCard(recordCard);
    }

    @Test
    public void updateRecordCard() throws Exception {
        RecordCard recordCard = new RecordCard();
        recordCard.setPupilId(5);
        recordCard.setBookId(6);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        recordCard.setReceiveDate(dateFormat.format(new Date()));
        recordCard.setReturnDate(dateFormat.format(new Date()));
        recordCardService.saveRecordCard(recordCard);

        Query query = session.createQuery("from RecordCard where id = " +  recordCard.getId());
        RecordCard recordCard1 = (RecordCard) query.getSingleResult();

        assertEquals(recordCard, recordCard1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRecordCardNull() throws Exception{
        RecordCard recordCard = null;

        recordCardService.updateRecordCard(recordCard);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRecordCardNoReceiveDate() throws Exception{
        RecordCard recordCard = new RecordCard();
        recordCard.setPupilId(5);
        recordCard.setBookId(6);
        recordCardService.updateRecordCard(recordCard);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRecordCardNoReturnDate() throws Exception{
        RecordCard recordCard = new RecordCard();
        recordCard.setPupilId(5);
        recordCard.setBookId(6);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        recordCard.setReceiveDate(dateFormat.format(new Date()));
        recordCardService.updateRecordCard(recordCard);
    }
}