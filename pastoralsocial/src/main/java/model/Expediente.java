package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // la parroquia y el sector antes eran texto libre y cada quien los escribía
    // distinto. ahora salen del catálogo, así no se repiten mal escritos.
    @ManyToOne
    @JoinColumn(name = "parroquia_id")
    private Parroquia parroquia;

    @ManyToOne
    @JoinColumn(name = "sector_filial_id")
    private SectorFilial sectorFilial;

    private String direccion;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    // estos tres eran enums fijos adentro de esta misma clase. el profe los
    // rechazó, así que ahora apuntan a las tablas de catálogo que se administran
    // desde la pantalla de Catálogos.
    @ManyToOne
    @JoinColumn(name = "tipo_vivienda_id")
    private TipoVivienda tipoVivienda;

    @ManyToOne
    @JoinColumn(name = "tenencia_id")
    private Tenencia tenencia;

    @ManyToOne
    @JoinColumn(name = "condicion_vivienda_id")
    private CondicionVivienda condicionVivienda;

    private String entrevistador;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // el estado lo guardo como texto y no como enum, para no repetir el error que
    // el profe ya me marcó. tampoco le hice tabla porque no es un catálogo que el
    // usuario administre: los dos estados están amarrados a la lógica del código.
    @Column(name = "estado")
    private String estado;

    @Column(name = "motivo_cierre")
    private String motivoCierre;

    @Column(name = "fecha_cierre")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCierre;

    @Column(name = "motivo_reapertura")
    private String motivoReapertura;

    // me guardo quién lo creó para la bitácora y para saber a quién preguntarle
    @Column(name = "creado_por")
    private String creadoPor;

    // estas tres listas son para poder llenar la ficha completa ANTES de guardarla,
    // como se hace en el papel. mientras el caso es nuevo los hijos viven acá en
    // memoria, y al persistir el expediente se guardan todos de una con la cascada.
    //
    // ojo: le puse solo CascadeType.PERSIST a propósito. si le pusiera ALL o
    // orphanRemoval, el merge() de una edición podría borrarme hijos sin querer,
    // porque en ese caso las listas vienen sin cargar. cuando el caso YA existe,
    // los hijos se manejan con sus propios DAO y estas listas ni se tocan.
    @OneToMany(mappedBy = "expediente", cascade = CascadeType.PERSIST)
    private List<Persona> personas = new ArrayList<Persona>();

    @OneToMany(mappedBy = "expediente", cascade = CascadeType.PERSIST)
    private List<Asistencia> asistencias = new ArrayList<Asistencia>();

    @OneToMany(mappedBy = "expediente", cascade = CascadeType.PERSIST)
    private List<ProlongacionAyuda> prolongaciones = new ArrayList<ProlongacionAyuda>();

    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_CERRADO = "CERRADO";

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

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivoCierre() { return motivoCierre; }
    public void setMotivoCierre(String motivoCierre) { this.motivoCierre = motivoCierre; }

    public Date getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(Date fechaCierre) { this.fechaCierre = fechaCierre; }

    public String getMotivoReapertura() { return motivoReapertura; }
    public void setMotivoReapertura(String motivoReapertura) { this.motivoReapertura = motivoReapertura; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    // lo uso en varias pantallas para decidir si dejo editar o no
    public boolean estaActivo() {
        return ESTADO_ACTIVO.equals(estado);
    }

    public List<Persona> getPersonas() { return personas; }
    public List<Asistencia> getAsistencias() { return asistencias; }
    public List<ProlongacionAyuda> getProlongaciones() { return prolongaciones; }

    // en los tres agrego por los dos lados de la relación: si no le pongo el
    // expediente al hijo, la cascada lo guarda con expediente_id en null y truena
    public void agregarPersona(Persona persona) {
        persona.setExpediente(this);
        this.personas.add(persona);
    }

    public void agregarAsistencia(Asistencia asistencia) {
        asistencia.setExpediente(this);
        this.asistencias.add(asistencia);
    }

    public void agregarProlongacion(ProlongacionAyuda prolongacion) {
        prolongacion.setExpediente(this);
        this.prolongaciones.add(prolongacion);
    }

    @Override
    public String toString() {
        // ahora parroquia es un objeto, así que si viene en null esto imprimiría
        // "null" y se ve feo en la lista. por eso el chequeo.
        String nombreParroquia = (parroquia == null) ? "sin parroquia" : parroquia.getNombre();
        return "Expediente N°" + numeroFicha + " - " + nombreParroquia;
    }
}