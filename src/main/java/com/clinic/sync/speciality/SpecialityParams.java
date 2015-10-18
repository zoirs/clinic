package com.clinic.sync.speciality;

enum SpecialityParams {
    id("Id", "id"),
    name("Name", "Название"),
    alias("Alias", "строка Наименование станции для ЧПУ"),
    NameGenitive("NameGenitive", "Наименование в родительном падеже"),
    NamePlural("NamePlural", "Наименование во множественном числе"),
    NamePluralGenitive("NamePluralGenitive", "Наименование в родительном падеже во множественном числе"),

//  входной
    cityID("city", "Уникальный числовой идентификатор города"),
    ;

    public final String key;
    public final String title;

    SpecialityParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
