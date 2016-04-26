package com.clinic.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Clinic.
 */
@Entity
@Table(name = "clinic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="clinic")
public class Clinic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "short_name", nullable = false)
    private String shortName;

    @NotNull
    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "url")
    private String url;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "house")
    private String house;

    @Column(name = "description")
    private String description;

    @Column(name = "weekdays_open")
    private String weekdaysOpen;

    @Column(name = "weekend_open")
    private String weekendOpen;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "is_diagnostic")
    private Boolean isDiagnostic;

    @Column(name = "is_clinic")
    private Boolean isClinic;

    @Column(name = "is_doctor")
    private Boolean isDoctor;

    @Column(name = "phone_contact")
    private String phoneContact;

    @Column(name = "phone_appointmen")
    private String phoneAppointmen;

    @Column(name = "phone_replacement")
    private String phoneReplacement;

    @Column(name = "logo_path")
    private String logoPath;

    @Column(name = "logo")
    private String logo;

    @Column(name = "schedule_state_online")
    private Boolean scheduleStateOnline;

    @Column(name = "email")
    private String email;

    @Column(name = "min_price", precision=10, scale=2, nullable = false)
    private BigDecimal minPrice;

    @Column(name = "max_price", precision=10, scale=2, nullable = false)
    private BigDecimal maxPrice;

    @Column(name = "docdoc_id")
    private Long docdocId;

    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

    @ManyToOne
    private City city;

    @ManyToOne
    private Street street;

    @ManyToOne
    private Area area;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "clinic_diagnostic",
               joinColumns = @JoinColumn(name="clinics_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="diagnostics_id", referencedColumnName="ID"))
    private Set<Diagnostic> diagnostics = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER) //todo переделать на lazy, при необходимости брать с помощью join fetch
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "clinic_metro",
               joinColumns = @JoinColumn(name="clinics_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="metros_id", referencedColumnName="ID"))
    private Set<Metro> metros = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER) //todo переделать на lazy, при необходимости брать с помощью join fetch
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "clinic_speciality",
               joinColumns = @JoinColumn(name="clinics_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="specialitys_id", referencedColumnName="ID"))
    private Set<Speciality> specialitys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeekdaysOpen() {
        return weekdaysOpen;
    }

    public void setWeekdaysOpen(String weekdaysOpen) {
        this.weekdaysOpen = weekdaysOpen;
    }

    public String getWeekendOpen() {
        return weekendOpen;
    }

    public void setWeekendOpen(String weekendOpen) {
        this.weekendOpen = weekendOpen;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Boolean getIsDiagnostic() {
        return isDiagnostic;
    }

    public void setIsDiagnostic(Boolean isDiagnostic) {
        this.isDiagnostic = isDiagnostic;
    }

    public Boolean getIsClinic() {
        return isClinic;
    }

    public void setIsClinic(Boolean isClinic) {
        this.isClinic = isClinic;
    }

    public Boolean getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(Boolean isDoctor) {
        this.isDoctor = isDoctor;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(String phoneContact) {
        this.phoneContact = phoneContact;
    }

    public String getPhoneAppointmen() {
        return phoneAppointmen;
    }

    public void setPhoneAppointmen(String phoneAppointmen) {
        this.phoneAppointmen = phoneAppointmen;
    }

    public String getPhoneReplacement() {
        return phoneReplacement;
    }

    public void setPhoneReplacement(String phoneReplacement) {
        this.phoneReplacement = phoneReplacement;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getScheduleStateOnline() {
        return scheduleStateOnline;
    }

    public void setScheduleStateOnline(Boolean scheduleStateOnline) {
        this.scheduleStateOnline = scheduleStateOnline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Long getDocdocId() {
        return docdocId;
    }

    public void setDocdocId(Long docdocId) {
        this.docdocId = docdocId;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Set<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(Set<Diagnostic> diagnostics) {
        this.diagnostics = diagnostics;
    }

    public Set<Metro> getMetros() {
        return metros;
    }

    public void setMetros(Set<Metro> metros) {
        this.metros = metros;
    }

    public Set<Speciality> getSpecialitys() {
        return specialitys;
    }

    public void setSpecialitys(Set<Speciality> specialitys) {
        this.specialitys = specialitys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Clinic clinic = (Clinic) o;

        if ( ! Objects.equals(id, clinic.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", shortName='" + shortName + "'" +
                ", alias='" + alias + "'" +
                ", url='" + url + "'" +
                ", longitude='" + longitude + "'" +
                ", latitude='" + latitude + "'" +
                ", streetName='" + streetName + "'" +
                ", house='" + house + "'" +
                ", description='" + description + "'" +
                ", weekdaysOpen='" + weekdaysOpen + "'" +
                ", weekendOpen='" + weekendOpen + "'" +
                ", shortDescription='" + shortDescription + "'" +
                ", isDiagnostic='" + isDiagnostic + "'" +
                ", isClinic='" + isClinic + "'" +
                ", isDoctor='" + isDoctor + "'" +
                ", phoneContact='" + phoneContact + "'" +
                ", phoneAppointmen='" + phoneAppointmen + "'" +
                ", phoneReplacement='" + phoneReplacement + "'" +
                ", logoPath='" + logoPath + "'" +
                ", logo='" + logo + "'" +
                ", scheduleStateOnline='" + scheduleStateOnline + "'" +
                ", email='" + email + "'" +
                ", minPrice='" + minPrice + "'" +
                ", maxPrice='" + maxPrice + "'" +
                ", docdocId='" + docdocId + "'" +
                ", lastUpdated='" + lastUpdated + "'" +
                '}';
    }
}
