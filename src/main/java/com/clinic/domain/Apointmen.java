package com.clinic.domain;

import com.clinic.sync.appointment.AppointmentParams;

import java.io.Serializable;
import java.util.*;


//@Entity
//@Table(name = "doctor")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Document(indexName = "doctor")
public class Apointmen implements Serializable {

    private String clinicid;
    private String doctorid;
    private String fio;
    private String name;
    private String phone;
    private String id;
    private String type;
    private Long speciality;
    private String comment;

    public Map<String, Object> toParams() {
        Map<String, Object> param = new HashMap<>();

        param.put(AppointmentParams.name.nameParam, fio);
        param.put(AppointmentParams.phone.nameParam, phone);
        param.put(AppointmentParams.clinic.nameParam, clinicid);
        param.put(AppointmentParams.comment.nameParam, comment);

        if (type.equals("doctor")) {
            param.put(AppointmentParams.doctor.nameParam, doctorid);
        } else {
            param.put(AppointmentParams.speciality.nameParam, speciality);

        }

        return param;
    }

    @Override
    public String toString() {
        return "Apointmen{" +
            "clinicid=" + clinicid +
            ", doctorid=" + doctorid +
            ", fio='" + fio + '\'' +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", id='" + id + '\'' +
            ", type='" + type + '\'' +
            ", speciality=" + speciality +
            ", comment='" + comment + '\'' +
            '}';
    }

    public String getClinicid() {
        return clinicid;
    }

    public void setClinicid(String clinicid) {
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

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public Long getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Long speciality) {
        this.speciality = speciality;
    }
}
