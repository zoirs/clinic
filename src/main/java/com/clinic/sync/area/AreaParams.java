package com.clinic.sync.area;

public enum AreaParams {

    id("Id", "id"),
    name("Name", "Название города"),
    alias("Alias", "строка Наименование города для ЧПУ"),
    cityId("CityId", "Число Уникальный идентификатор города"),


    ;

    public final String key;
    public final String title;

    AreaParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
