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

    @Override
    public String toString() {
        return apellidosNombres + " (" + edad + " años)";
    }
}