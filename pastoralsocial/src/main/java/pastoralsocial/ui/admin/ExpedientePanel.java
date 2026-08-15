package pastoralsocial.ui.admin;

import javax.swing.JPanel;

import pastoralsocial.controller.AdendumController;
import pastoralsocial.modelo.CasoSocial;

public class ExpedientePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final CasoSocial casoSocial;
	private final AdendumController adendumController;

	public ExpedientePanel(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		this.casoSocial = casoSocial;
		this.adendumController = new AdendumController();

		// inicializarInterfaz();
		// cargarAdendums();
	}
}
