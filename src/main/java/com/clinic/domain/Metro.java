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
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Metro.
 */
@Entity
@Table(name = "metro")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="metro")
public class Metro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "alias", nullable = false)
    private String alias;

    @NotNull
    @Column(name = "line_name", nullable = false)
    private String lineName;

    @NotNull
    @Column(name = "line_color", nullable = false)
    private String lineColor;

    @Column(name = "docdoc_id")
    private Long docdocId;

    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

    @ManyToOne
    private City city;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "metro_nearest_metro",
        joinColumns = @JoinColumn(name="metro_id", referencedColumnName="ID"),
        inverseJoinColumns = @JoinColumn(name="nearest_metro_id", referencedColumnName="ID"))
    private Set<Metro> nearest = new HashSet<>();

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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
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

    public Set<Metro> getNearest() {
        return nearest;
    }

    public void setNearest(Set<Metro> nearest) {
        this.nearest = nearest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Metro metro = (Metro) o;

        if ( ! Objects.equals(id, metro.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Metro{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", alias='" + alias + "'" +
                ", lineName='" + lineName + "'" +
                ", lineColor='" + lineColor + "'" +
                ", docdocId='" + docdocId + "'" +
                ", lastUpdated='" + lastUpdated + "'" +
                '}';
    }
}
