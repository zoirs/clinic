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
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


//@Entity
//@Table(name = "doctor")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Document(indexName = "doctor")
public class Apointmen implements Serializable {

    private Long clinicid;
    private String fio;
    private String name;
    private String phone;
    private String id;
    private String type;
    private Long speciality;
    private String comment;

    @Override
    public String toString() {
        return "Apointmen{" +
            "clinicid=" + clinicid +
            ", fio='" + fio + '\'' +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", id='" + id + '\'' +
            ", type='" + type + '\'' +
            ", speciality=" + speciality +
            ", comment='" + comment + '\'' +
            '}';
    }

    public Long getClinicid() {
        return clinicid;
    }

    public void setClinicid(Long clinicid) {
        this.clinicid = clinicid;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
