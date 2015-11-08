package com.clinic.sync.diagnostic;

public enum DiagnosticParams {

    id("Id", "id"),
    name("Name", "Название города"),
    alias("Alias", "строка Наименование города для ЧПУ"),
    cityId("CityId", "Число Уникальный идентификатор города"),
    subDiagnosticList("SubDiagnosticList", "Подуслуги"),


    ;

    public final String key;
    public final String title;

    DiagnosticParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
