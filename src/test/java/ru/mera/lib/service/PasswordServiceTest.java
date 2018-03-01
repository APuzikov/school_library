package ru.mera.lib.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.mera.lib.entity.Password;

import javax.persistence.Table;
import java.util.List;

import static org.junit.Assert.*;

public class PasswordServiceTest {

    private SessionFactory factory;
    private Session session;
    private PasswordService passwordService;
    @Before
    public void setUp() throws Exception {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
        passwordService = new PasswordService(session);
    }

    @After
    public void tearDown() throws Exception {
        session.close();
        factory.close();
    }

    @Test
    public void savePassword() throws Exception {

        Password password = new Password();
        password.setHashPassword("qqqq".hashCode());
        password.setEnable(true);

        passwordService.savePassword(password);

        Query query = session.createQuery("from Password where id = " + password.getId());
        Password password1 = (Password) query.getSingleResult();

        assertEquals("qqqq".hashCode(), password1.getHashPassword());
        assertEquals(password.isEnable(), password1.isEnable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void savePasswordNull() throws Exception{
        Password password = null;
        passwordService.savePassword(password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void savePasswordNullHash() throws Exception{
        Password password = new Password();
        password.setHashPassword("".hashCode());
        passwordService.savePassword(password);
    }
}