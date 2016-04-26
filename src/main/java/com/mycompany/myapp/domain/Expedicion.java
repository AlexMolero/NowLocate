package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Expedicion.
 */
@Entity
@Table(name = "expedicion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "expedicion")
public class Expedicion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "frigorifico")
    private Boolean frigorifico;

    @Column(name = "temp_max")
    private Integer tempMax;

    @Column(name = "temp_min")
    private Integer tempMin;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "camion_id")
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "delegacion_inicio_id")
    private Delegacion delegacionInicio;

    @ManyToOne
    @JoinColumn(name = "delegacion_fin_id")
    private Delegacion delegacionFin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Boolean getFrigorifico() {
        return frigorifico;
    }

    public void setFrigorifico(Boolean frigorifico) {
        this.frigorifico = frigorifico;
    }

    public Integer getTempMax() {
        return tempMax;
    }

    public void setTempMax(Integer tempMax) {
        this.tempMax = tempMax;
    }

    public Integer getTempMin() {
        return tempMin;
    }

    public void setTempMin(Integer tempMin) {
        this.tempMin = tempMin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public Delegacion getDelegacionInicio() {
        return delegacionInicio;
    }

    public void setDelegacionInicio(Delegacion delegacion) {
        this.delegacionInicio = delegacion;
    }

    public Delegacion getDelegacionFin() {
        return delegacionFin;
    }

    public void setDelegacionFin(Delegacion delegacion) {
        this.delegacionFin = delegacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expedicion expedicion = (Expedicion) o;
        return Objects.equals(id, expedicion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Expedicion{" +
            "id=" + id +
            ", fechaInicio='" + fechaInicio + "'" +
            ", fechaEntrega='" + fechaEntrega + "'" +
            ", frigorifico='" + frigorifico + "'" +
            ", tempMax='" + tempMax + "'" +
            ", tempMin='" + tempMin + "'" +
            ", descripcion='" + descripcion + "'" +
            '}';
    }
}
