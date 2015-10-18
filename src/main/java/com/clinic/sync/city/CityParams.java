package com.clinic.sync.city;

enum CityParams {
//    выходные
    id("Id", "id"),
    name("Name", "Название города"),
    alias("Alias", "строка Наименование города для ЧПУ"),
    Phone("Phone", "телефон"),
    Latitude("Latitude", "широта"),
    Longitude("Longitude", "долгота"),

//    входные
    cityID("city", "Уникальный числовой идентификатор города");

    public final String key;
    public final String title;

    CityParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
