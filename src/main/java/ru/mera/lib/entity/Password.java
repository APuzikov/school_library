package ru.mera.lib.entity;

import javax.persistence.*;

@Entity
@Table(name = "Passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "hashPassword", nullable = false)
    private int hashPassword;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(int hashPassword) {
        this.hashPassword = hashPassword;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
