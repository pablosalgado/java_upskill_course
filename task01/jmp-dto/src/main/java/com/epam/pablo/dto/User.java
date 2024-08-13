package com.epam.pablo.dto;

import java.time.LocalDate;

public class User {
    private String name;
    private String surname;
    private LocalDate birthday;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String name, String surname, LocalDate birthday) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for surname
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // Getter and setter for birthday
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    // Override toString method for easy printing
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User User = (User) o;
        return name.equals(User.name) &&
                surname.equals(User.surname) &&
                birthday.equals(User.birthday);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, surname, birthday);
    }
}