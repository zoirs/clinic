package com.clinic.sync.clinic;

public enum ClinicParams {
    id("Id", "id"),
    name("Name", "Название"),
    alias("Alias", "строка Наименование станции для ЧПУ"),
    RewriteName("RewriteName", "Краткое название клиники"),
    URL("URL", "Адрес url"),
    Longitude("Longitude", "Долгота"),
    Latitude("Latitude", "Latitude"),
    City("City", "Идентификатор города"),
    Street("Street", "Название улицы"),
    StreetId("StreetId", "Идентификатор улицы"),
    House("House", "Дом"),
    Description("Description", "Описание"),
    ShortDescription("ShortDescription", "Краткое описание"),
    IsDiagnostic("IsDiagnostic", "Флаг - диагностический центр"),
    IsClinic("IsClinic", "Флаг - клиника"),
    IsDoctor("IsDoctor", "Флаг - частный врач"),
    Phone("Phone", "Контактный телефон"),
    PhoneAppointment("PhoneAppointment", "Телефон для записи"),
    ReplacementPhone("ReplacementPhone", "Подменный телефон для клиники"),
    ScheduleState("ScheduleState", "Работа по онлайн расписанию"),
    DistrictId("DistrictId", "Идентификатор района"),
    Email("Email", "Email"),
    MinPrice("MinPrice", "Минимальная цена первичного приема"),
    MaxPrice("MaxPrice", "Максимальная цена первичного приема"),
    Diagnostics("Diagnostics", "Список исследований"),
    Stations("Stations", "Список станций метро"),
    Specialities("Specialities", "Список специальностей"),
    Schedule("Schedule", "Время работы по дням недели"),
    // входные:
    startFrom("start", "Начиная с какого порядкого номера вернуть список врачей"),
    countList("count", "Сколько врачей должно быть в списке (не более 500)"),
    cityID("city", "Уникальный числовой идентификатор города");

    public final String key;
    public final String title;

    ClinicParams(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
