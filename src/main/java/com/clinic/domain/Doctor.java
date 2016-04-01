package com.clinic.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.clinic.domain.util.CustomDateTimeDeserializer;
import com.clinic.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="doctor")
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "fio")
    private String fio;

    @Column(name = "alias")
    private String alias;

    @Min(value = 0)
    @Column(name = "rating")
    private Float rating;

    @Min(value = 0)
    @Column(name = "rating_internal")
    private Float ratingInternal;

    @Column(name = "price_first")
    private Integer priceFirst;

    @Column(name = "price_special")
    private Integer priceSpecial;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "img")
    private String img;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "text_about")
    private String textAbout;

    @Column(name = "experienca_year")
    private Integer experiencaYear;

    @Column(name = "departure")
    private Boolean departure;

    @Column(name = "category")
    private String category;

    @Column(name = "degree")
    private String degree;

    @Column(name = "rank")
    private String rank;

    @Column(name = "extra")
    private String extra;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "docdoc_id")
    private Long docdocId;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "last_updated")
    private DateTime lastUpdated;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "doctor_clinic",
               joinColumns = @JoinColumn(name="doctors_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="clinics_id", referencedColumnName="ID"))
    private Set<Clinic> clinics = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER) //todo переделать на lazy, при необходимости брать с помощью join fetch
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "doctor_speciality",
               joinColumns = @JoinColumn(name="doctors_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="specialitys_id", referencedColumnName="ID"))
    private Set<Speciality> specialitys = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER) //todo переделать на lazy, при необходимости брать с помощью join fetch
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "doctor_metro",
               joinColumns = @JoinColumn(name="doctors_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="metros_id", referencedColumnName="ID"))
    private Set<Metro> metros = new HashSet<>();

    @ManyToOne
    private City city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Float getRatingInternal() {
        return ratingInternal;
    }

    public void setRatingInternal(Float ratingInternal) {
        this.ratingInternal = ratingInternal;
    }

    public Integer getPriceFirst() {
        return priceFirst;
    }

    public void setPriceFirst(Integer priceFirst) {
        this.priceFirst = priceFirst;
    }

    public Integer getPriceSpecial() {
        return priceSpecial;
    }

    public void setPriceSpecial(Integer priceSpecial) {
        this.priceSpecial = priceSpecial;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getTextAbout() {
        return textAbout;
    }

    public void setTextAbout(String textAbout) {
        this.textAbout = textAbout;
    }

    public Integer getExperiencaYear() {
        return experiencaYear;
    }

    public void setExperiencaYear(Integer experiencaYear) {
        this.experiencaYear = experiencaYear;
    }

    public Boolean getDeparture() {
        return departure;
    }

    public void setDeparture(Boolean departure) {
        this.departure = departure;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getDocdocId() {
        return docdocId;
    }

    public void setDocdocId(Long docdocId) {
        this.docdocId = docdocId;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(Set<Clinic> clinics) {
        this.clinics = clinics;
    }

    public Set<Speciality> getSpecialitys() {
        return specialitys;
    }

    public void setSpecialitys(Set<Speciality> specialitys) {
        this.specialitys = specialitys;
    }

    public Set<Metro> getMetros() {
        return metros;
    }

    public void setMetros(Set<Metro> metros) {
        this.metros = metros;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Doctor doctor = (Doctor) o;

        if ( ! Objects.equals(id, doctor.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", fio='" + fio + "'" +
                ", alias='" + alias + "'" +
                ", rating='" + rating + "'" +
                ", ratingInternal='" + ratingInternal + "'" +
                ", priceFirst='" + priceFirst + "'" +
                ", priceSpecial='" + priceSpecial + "'" +
                ", sex='" + sex + "'" +
                ", img='" + img + "'" +
                ", reviewCount='" + reviewCount + "'" +
                ", textAbout='" + textAbout + "'" +
                ", experiencaYear='" + experiencaYear + "'" +
                ", departure='" + departure + "'" +
                ", category='" + category + "'" +
                ", degree='" + degree + "'" +
                ", rank='" + rank + "'" +
                ", extra='" + extra + "'" +
                ", isActive='" + isActive + "'" +
                ", docdocId='" + docdocId + "'" +
                ", lastUpdated='" + lastUpdated + "'" +
                '}';
    }
}
