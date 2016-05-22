package com.clinic.sync.appointment;

public enum AppointmentParams {
    name("name", "Имя пациента"),
    phone("phone", "Номер телефона"),
    doctor("doctor", "ID врача"),
    clinic("clinic", "ID клиники"),
    comment("comment", "Комментарий пациента"),
    speciality("speciality", "ID специальности");

    public final String nameParam;
    public final String description;

    AppointmentParams(String nameParam, String description) {
        this.nameParam = nameParam;
        this.description = description;
    }
}
