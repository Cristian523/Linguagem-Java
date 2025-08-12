package lp2g06.biblioteca;

public class NomeInvalidoException extends MinhasExcecoes {
	private static final long serialVersionUID = 1L;

	public NomeInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Nome invalido: " + super.getMessage());
	}
	
}
