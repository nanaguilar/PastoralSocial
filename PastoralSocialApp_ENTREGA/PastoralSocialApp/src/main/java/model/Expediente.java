package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Temporal;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Maestro del módulo "Caso Social".
 *
 * <p>Un expediente representa un caso social (una familia / núcleo). Contiene:
 * <ul>
 *   <li>los datos generales y de la <b>ubicación del núcleo familiar</b>, que se
 *       registran una sola vez aquí (parroquia, sector, dirección) y son
 *       heredados por todos los familiares — evitando ubicaciones duplicadas;</li>
 *   <li>el <b>detalle</b> de familiares ({@link Persona}) mediante una relación
 *       maestro-detalle con cascada, de modo que se gestionan junto con el
 *       expediente en una sola operación.</li>
 * </ul>
 */
@Entity
@Table(name = "expediente")
public class Expediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_ficha", nullable = false, unique = true)
    private String numeroFicha;

    @Column(name = "fecha_inicio")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "fecha_prevista_fin")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaPrevistaFin;

    /** Parroquia seleccionada desde el catálogo (antes era texto libre). */
    @ManyToOne
    @JoinColumn(name = "parroquia_id")
    private Parroquia parroquia;

    /** Sector / filial seleccionado desde el catálogo (detalle de la parroquia). */
    @ManyToOne
    @JoinColumn(name = "sector_filial_id")
    private SectorFilial sectorFilial;

    private String direccion;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vivienda")
    private TipoVivienda tipoVivienda;

    @Enumerated(EnumType.STRING)
    private Tenencia tenencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_vivienda")
    private CondicionVivienda condicionVivienda;

    private String entrevistador;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Detalle del maestro-detalle: los familiares del caso social.
     * La cascada y {@code orphanRemoval} permiten guardar, actualizar y borrar
     * los familiares junto con el expediente sin lógica duplicada.
     */
    @OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Persona> personas = new ArrayList<>();

    public enum TipoVivienda { CASA, APARTAMENTO, CUARTO, TUGURIO, ALBERGUE_REFUGIO, NINGUNA }
    public enum Tenencia { PROPIA, PAGA_A_PLAZOS, ALQUILADA, PRESTADA, PRECARIO }
    public enum CondicionVivienda { BUENA, REGULAR, MALA, NO_APLICA }

    public Expediente() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumeroFicha() { return numeroFicha; }
    public void setNumeroFicha(String numeroFicha) { this.numeroFicha = numeroFicha; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaPrevistaFin() { return fechaPrevistaFin; }
    public void setFechaPrevistaFin(Date fechaPrevistaFin) { this.fechaPrevistaFin = fechaPrevistaFin; }

    public Parroquia getParroquia() { return parroquia; }
    public void setParroquia(Parroquia parroquia) { this.parroquia = parroquia; }

    public SectorFilial getSectorFilial() { return sectorFilial; }
    public void setSectorFilial(SectorFilial sectorFilial) { this.sectorFilial = sectorFilial; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public TipoVivienda getTipoVivienda() { return tipoVivienda; }
    public void setTipoVivienda(TipoVivienda tipoVivienda) { this.tipoVivienda = tipoVivienda; }

    public Tenencia getTenencia() { return tenencia; }
    public void setTenencia(Tenencia tenencia) { this.tenencia = tenencia; }

    public CondicionVivienda getCondicionVivienda() { return condicionVivienda; }
    public void setCondicionVivienda(CondicionVivienda condicionVivienda) { this.condicionVivienda = condicionVivienda; }

    public String getEntrevistador() { return entrevistador; }
    public void setEntrevistador(String entrevistador) { this.entrevistador = entrevistador; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<Persona> getPersonas() { return personas; }
    public void setPersonas(List<Persona> personas) { this.personas = personas; }

    /**
     * Agrega un familiar al caso social manteniendo sincronizada la relación
     * bidireccional (el familiar hereda su expediente/ubicación de este maestro).
     */
    public void agregarPersona(Persona persona) {
        persona.setExpediente(this);
        this.personas.add(persona);
    }

    /** Quita un familiar del caso social (será eliminado por {@code orphanRemoval}). */
    public void quitarPersona(Persona persona) {
        this.personas.remove(persona);
        persona.setExpediente(null);
    }

    @Override
    public String toString() {
        String nombreParroquia = (parroquia != null) ? parroquia.getNombre() : "Sin parroquia";
        return "Expediente N°" + numeroFicha + " - " + nombreParroquia;
    }
}
