package com.epam.pablo.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class User {
    private String name;
    private String surname;
    private LocalDate birthday;

    public User() {
    }

    public User(String name, String surname, LocalDate birthday) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var User = (User) o;
        return name.equals(User.name) &&
                surname.equals(User.surname) &&
                birthday.equals(User.birthday);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, surname, birthday);
    }

    public long getAge() {
        return ChronoUnit.YEARS.between(birthday, LocalDate.now());
    }
}