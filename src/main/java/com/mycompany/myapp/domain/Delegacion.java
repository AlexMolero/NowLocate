package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Delegacion.
 */
@Entity
@Table(name = "delegacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "delegacion")
public class Delegacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "volumen_almacen")
    private Integer volumenAlmacen;

    @Column(name = "calle")
    private String calle;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @OneToMany(mappedBy = "delegacionInicio")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Expedicion> expedicions = new HashSet<>();

    @OneToMany(mappedBy = "delegacionFin")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Expedicion> expedicionFins = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Integer getVolumenAlmacen() {
        return volumenAlmacen;
    }

    public void setVolumenAlmacen(Integer volumenAlmacen) {
        this.volumenAlmacen = volumenAlmacen;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Set<Expedicion> getExpedicions() {
        return expedicions;
    }

    public void setExpedicions(Set<Expedicion> expedicions) {
        this.expedicions = expedicions;
    }

    public Set<Expedicion> getExpedicionFins() {
        return expedicionFins;
    }

    public void setExpedicionFins(Set<Expedicion> expedicions) {
        this.expedicionFins = expedicions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Delegacion delegacion = (Delegacion) o;
        return Objects.equals(id, delegacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Delegacion{" +
            "id=" + id +
            ", localidad='" + localidad + "'" +
            ", volumenAlmacen='" + volumenAlmacen + "'" +
            ", calle='" + calle + "'" +
            ", latitud='" + latitud + "'" +
            ", longitud='" + longitud + "'" +
            '}';
    }
}
