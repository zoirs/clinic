package com.clinic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Diagnostic.
 */
@Entity
@Table(name = "diagnostic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="diagnostic")
public class Diagnostic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "docdoc_id")
    private Long docdocId;

    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

    @OneToMany(mappedBy = "diagnostic")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Diagnostic> diagnostics = new HashSet<>();

    @ManyToOne
    private Diagnostic diagnostic;

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

    public Set<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(Set<Diagnostic> diagnostics) {
        this.diagnostics = diagnostics;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Diagnostic diagnostic = (Diagnostic) o;

        if ( ! Objects.equals(id, diagnostic.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Diagnostic{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", alias='" + alias + "'" +
                ", docdocId='" + docdocId + "'" +
                ", lastUpdated='" + lastUpdated + "'" +
                '}';
    }
}
