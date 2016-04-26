package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Temperatura.
 */
@Entity
@Table(name = "temperatura")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "temperatura")
public class Temperatura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "hora")
    private ZonedDateTime hora;

    @Column(name = "temperatura")
    private Integer temperatura;

    @ManyToOne
    @JoinColumn(name = "expedicion_id")
    private Expedicion expedicion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getHora() {
        return hora;
    }

    public void setHora(ZonedDateTime hora) {
        this.hora = hora;
    }

    public Integer getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Integer temperatura) {
        this.temperatura = temperatura;
    }

    public Expedicion getExpedicion() {
        return expedicion;
    }

    public void setExpedicion(Expedicion expedicion) {
        this.expedicion = expedicion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Temperatura temperatura = (Temperatura) o;
        return Objects.equals(id, temperatura.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Temperatura{" +
            "id=" + id +
            ", hora='" + hora + "'" +
            ", temperatura='" + temperatura + "'" +
            '}';
    }
}
