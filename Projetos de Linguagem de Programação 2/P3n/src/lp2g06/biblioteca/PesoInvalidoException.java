package lp2g06.biblioteca;

public class PesoInvalidoException extends MinhasExcecoes {
	private static final long serialVersionUID = 1L;

	public PesoInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Peso invalido: " + super.getMessage());
	}

}
