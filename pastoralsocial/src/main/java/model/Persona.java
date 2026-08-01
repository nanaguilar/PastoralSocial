package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "expediente_id", nullable = false)
    private Expediente expediente;

    @Column(name = "apellidos_nombres", nullable = false)
    private String apellidosNombres;

    @Column(name = "documento_identidad")
    private String documentoIdentidad;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private boolean jefatura;
    private String relacion;
    private Integer edad;
    private String pais;
    private String migracion;
    private String educacion;
    private String salud;

    @Column(name = "alarma_medica")
    private boolean alarmaMedica;

    private String seguro;

    // la edad ya no se escribe a mano: ahora se guarda la fecha de nacimiento y
    // la edad se calcula. dejo la columna edad de arriba y la sigo llenando con
    // el valor calculado, para no romper nada que la esté leyendo.
    @Column(name = "fecha_nacimiento")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNacimiento;

    // sección de Ingreso Familiar que pidió el profe (observación C)
    private String ocupacion;
    private boolean trabaja;
    private boolean pensionado;

    @Column(name = "ingreso_mensual")
    private Double ingresoMensual;

    // al integrante nunca lo borro, solo lo marco inactivo, así no se pierde el
    // historial del caso. arranca en true para los que se creen de ahora en adelante.
    private boolean activo = true;

    public enum Sexo { F, M, OTRO }

    public Persona() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Expediente getExpediente() { return expediente; }
    public void setExpediente(Expediente expediente) { this.expediente = expediente; }

    public String getApellidosNombres() { return apellidosNombres; }
    public void setApellidosNombres(String apellidosNombres) { this.apellidosNombres = apellidosNombres; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public boolean isJefatura() { return jefatura; }
    public void setJefatura(boolean jefatura) { this.jefatura = jefatura; }

    public String getRelacion() { return relacion; }
    public void setRelacion(String relacion) { this.relacion = relacion; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getMigracion() { return migracion; }
    public void setMigracion(String migracion) { this.migracion = migracion; }

    public String getEducacion() { return educacion; }
    public void setEducacion(String educacion) { this.educacion = educacion; }

    public String getSalud() { return salud; }
    public void setSalud(String salud) { this.salud = salud; }

    public boolean isAlarmaMedica() { return alarmaMedica; }
    public void setAlarmaMedica(boolean alarmaMedica) { this.alarmaMedica = alarmaMedica; }

    public String getSeguro() { return seguro; }
    public void setSeguro(String seguro) { this.seguro = seguro; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public boolean isTrabaja() { return trabaja; }
    public void setTrabaja(boolean trabaja) { this.trabaja = trabaja; }

    public boolean isPensionado() { return pensionado; }
    public void setPensionado(boolean pensionado) { this.pensionado = pensionado; }

    public Double getIngresoMensual() { return ingresoMensual; }
    public void setIngresoMensual(Double ingresoMensual) { this.ingresoMensual = ingresoMensual; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // saco la edad desde la fecha de nacimiento. devuelvo null si todavía no le
    // pusieron fecha, para no mostrar un 0 que confunde.
    public Integer calcularEdad() {
        if (fechaNacimiento == null) {
            return null;
        }

        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(fechaNacimiento);
        Calendar hoy = Calendar.getInstance();

        int anios = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

        // si todavía no ha cumplido años este año, le resto uno
        boolean mesAnterior = hoy.get(Calendar.MONTH) < nacimiento.get(Calendar.MONTH);
        boolean mismoMesDiaAnterior =
            hoy.get(Calendar.MONTH) == nacimiento.get(Calendar.MONTH)
            && hoy.get(Calendar.DAY_OF_MONTH) < nacimiento.get(Calendar.DAY_OF_MONTH);

        if (mesAnterior || mismoMesDiaAnterior) {
            anios--;
        }
        return anios;
    }

    @Override
    public String toString() {
        return apellidosNombres + " (" + edad + " años)";
    }
}