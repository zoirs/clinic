package com.clinic.sync;

public enum CityParams {
    id("Id", "id"),
    name("Name", "Название города"),
    alias("Alias", "строка Наименование города для ЧПУ"),
    Phone("Phone", "телефон"),
    Latitude("Latitude", "широта"),
    Longitude("Longitude", "долгота");

    public final String key;
    public final String title;

    CityParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
