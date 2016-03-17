package com.clinic.sync.doctor;

public enum DoctorParams {
    id("Id", "id"),
    name("Name", "Название"),
    alias("Alias", "строка Наименование станции для ЧПУ"),
    Rating("Rating", "Рейтинг ДокДок врача"),
    InternalRating("InternalRating", "Внутренний рейтинг ДокДок врача"),
    Price("Price", "Стоимость первичного приёма"),
    SpecialPrice("SpecialPrice", "Специальная стоимость приёма"),
    Sex("Sex", "Пол"),
    ReviewCount("OpinionCount", "Количество отзывов"),
    TextAbout("TextAbout", "Информация о враче"),
    ExperienceYear("ExperienceYear", "Стаж специалиста"),
    Departure("Departure", "Выезд домой"),
    Category("Category", "Категория врача (e.g. \"1-я категория\")"),
    Degree("Degree", "Ученая степень"),
    Rank("Rank", ""),
    Extra("Extra", "Признак того, что данный врач из добивки (null / geo / best)"),
    Clinics("Clinics", "Клиники"),
    Specialities("Specialities", "Специальности"),
    Description("Description", "Описание"),
    TextAssociation("TextAssociation", "Описание"),
    TextCourse("TextCourse", "Описание"),
    Img("Img", "фото"),
    Metro("Stations", "Метро"),

    // входные:
    startFrom("start", "Начиная с какого порядкого номера вернуть список врачей"),
    countList("count", "Сколько врачей должно быть в списке (не более 500)"),
    cityID("city", "Уникальный числовой идентификатор города");

    ;


    public final String key;
    public final String title;

    DoctorParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
