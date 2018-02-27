package ru.mera.lib.service;

import org.hibernate.Session;
import org.springframework.util.Assert;
import ru.mera.lib.entity.RecordCard;

public class RecordCardService {
    private Session session;

    public RecordCardService(Session session) {
        this.session = session;
    }

    public void saveRecordCard(RecordCard recordCard) throws Exception {
        Assert.notNull(recordCard, "Учетная карта не может быть null!");
        Assert.hasText(recordCard.getReceiveDate(), "Отсутствует дата выдачи!");

        session.beginTransaction();
        session.save(recordCard);
        session.getTransaction().commit();
    }

    public void updateRecordCard(RecordCard recordCard) throws Exception {
        Assert.notNull(recordCard, "Учетная карта не может быть null!");
        Assert.hasText(recordCard.getReceiveDate(), "Отсутствует дата выдачи!");
        Assert.hasText(recordCard.getReturnDate(), "Отсутствует дата приема!");

        session.beginTransaction();
        session.save(recordCard);
        session.getTransaction().commit();
    }
}
