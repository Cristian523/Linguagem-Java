package lp2g06.biblioteca;

public class SobrenomeInvalidoException extends MinhasExcecoes {
	private static final long serialVersionUID = 1L;

	public SobrenomeInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Sobrenome invalido: " + super.getMessage());
	}
	
}
