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
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // lo dejo nullable a propósito aunque en el formulario sí lo pida obligatorio:
    // los usuarios que ya estaban en la base no tienen correo, y si lo pongo
    // nullable = false Hibernate no puede agregar la columna a la tabla que ya
    // tiene filas. el admin se lo llena editando la ficha.
    @Column(length = 150, unique = true)
    private String correo;

    private boolean activo;

    // ===================== recuperación de contraseña =====================
    // acá guardo el código de 6 dígitos que se le manda al correo y hasta cuándo
    // sirve. los dos quedan en null apenas la contraseña se cambia, para que el
    // mismo código no se pueda usar dos veces.

    @Column(name = "reset_token", length = 255)
    private String resetToken;

    @Column(name = "token_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenExpiration;

    // el rol ahora es una tabla aparte, no un enum, así se puede administrar.
    // queda nullable a propósito: Hibernate agrega esta columna a la tabla que ya
    // tiene filas y esas quedan en NULL. eso lo arreglo con asegurarAdminInicial().
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    public Usuario() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public Date getTokenExpiration() { return tokenExpiration; }
    public void setTokenExpiration(Date tokenExpiration) { this.tokenExpiration = tokenExpiration; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    @Override
    public String toString() {
        return nombre + " (" + username + ")";
    }
}