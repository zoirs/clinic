package com.clinic.sync.metro;

public enum MetroParams {
    id("Id", "id"),
    name("Name", "Название"),
    alias("Alias", "строка Наименование станции для ЧПУ"),
    lineName("LineName", "строка Имя линии станций метро"),
    lineColor("LineColor", "строка Цвет линии станций метро"),
    cityId("CityId", "Число Уникальный идентификатор города"),
    districtIds("DistrictIds", "массив Идентификаторы районов");

    public final String key;
    public final String title;

    MetroParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
