package com.clinic.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Speciality.
 */
@Entity
@Table(name = "speciality")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="speciality")
public class Speciality implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "name_genitive")
    private String nameGenitive;

    @Column(name = "name_plural")
    private String namePlural;

    @Column(name = "name_plural_genitive")
    private String namePluralGenitive;

    @Column(name = "docdoc_id")
    private Long docdocId;

    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNameGenitive() {
        return nameGenitive;
    }

    public void setNameGenitive(String nameGenitive) {
        this.nameGenitive = nameGenitive;
    }

    public String getNamePlural() {
        return namePlural;
    }

    public void setNamePlural(String namePlural) {
        this.namePlural = namePlural;
    }

    public String getNamePluralGenitive() {
        return namePluralGenitive;
    }

    public void setNamePluralGenitive(String namePluralGenitive) {
        this.namePluralGenitive = namePluralGenitive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Speciality speciality = (Speciality) o;

        if ( ! Objects.equals(id, speciality.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Speciality{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", alias='" + alias + "'" +
                ", nameGenitive='" + nameGenitive + "'" +
                ", namePlural='" + namePlural + "'" +
                ", namePluralGenitive='" + namePluralGenitive + "'" +
                ", docdocId='" + docdocId + "'" +
                ", lastUpdated='" + lastUpdated + "'" +
                '}';
    }
}
