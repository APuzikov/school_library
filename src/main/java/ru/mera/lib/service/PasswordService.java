package ru.mera.lib.service;

import org.hibernate.Session;
import org.springframework.util.Assert;
import ru.mera.lib.entity.Password;

public class PasswordService {

    private Session session;

    public PasswordService(Session session) {
        this.session = session;
    }

    public void savePassword(Password password) throws Exception{
        Assert.notNull(password, "Пароль не может быть null!");
        Assert.isTrue(password.getHashPassword() != 0, "Пароль не введен!");
        session.beginTransaction();
        session.save(password);
        session.getTransaction().commit();
    }
}
