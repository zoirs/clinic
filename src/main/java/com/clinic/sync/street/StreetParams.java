package com.clinic.sync.street;

enum StreetParams {

    id("Id", "id"),
    name("Title", "Название"),
    alias("RewriteName", "строка Наименование станции для ЧПУ"),
    cityId("CityId", "Число Уникальный идентификатор города"),


    ;

    public final String key;
    public final String title;

    StreetParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
