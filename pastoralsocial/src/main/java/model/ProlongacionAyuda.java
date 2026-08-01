package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import java.util.Date;

// las "prolongaciones de la ayuda" del recuadro de arriba de la ficha.
// en el papel hay 5 espacios fijos, pero preferí hacerlo tabla hija en vez de
// 5 columnas: si algún día necesitan una sexta, no hay que tocar nada.
@Entity
@Table(name = "prolongacion_ayuda")
public class ProlongacionAyuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "expediente_id", nullable = false)
    private Expediente expediente;

    @Column(name = "fecha")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;

    public ProlongacionAyuda() {
    }

    public ProlongacionAyuda(Expediente expediente, Date fecha) {
        this.expediente = expediente;
        this.fecha = fecha;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Expediente getExpediente() { return expediente; }
    public void setExpediente(Expediente expediente) { this.expediente = expediente; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
