package pastoralsocial.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: DocumentoCaso
 *
 */

@Entity
@Table(name = "documentosCaso")
public class DocumentoCaso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idDocumento;

	@ManyToOne
	@JoinColumn(name = "idCasoSocial", nullable = false)
	private CasoSocial casoSocial;

	@ManyToOne
	@JoinColumn(name = "idDetalleTipoDocumento", nullable = false)
	private Detalle detalleTipoDocumento;

	@Column(nullable = false, length = 255)
	private String nombreArchivo;

	@Column(nullable = false, length = 500)
	private String rutaArchivo;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date fechaRegistro;

	public DocumentoCaso() {
	}

	public DocumentoCaso(int idDocumento, CasoSocial casoSocial, Detalle detalleTipoDocumento, String nombreArchivo, String rutaArchivo,
			Date fechaRegistro) {
		this.idDocumento = idDocumento;
		this.casoSocial = casoSocial;
		this.detalleTipoDocumento = detalleTipoDocumento;
		this.nombreArchivo = nombreArchivo;
		this.rutaArchivo = rutaArchivo;
		this.fechaRegistro = fechaRegistro;
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public CasoSocial getCasoSocial() {
		return casoSocial;
	}

	public void setCasoSocial(CasoSocial casoSocial) {
		this.casoSocial = casoSocial;
	}

	public Detalle getDetalleTipoDocumento() {
		return detalleTipoDocumento;
	}

	public void setDetalleTipoDocumento(Detalle detalleTipoDocumento) {
		this.detalleTipoDocumento = detalleTipoDocumento;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
