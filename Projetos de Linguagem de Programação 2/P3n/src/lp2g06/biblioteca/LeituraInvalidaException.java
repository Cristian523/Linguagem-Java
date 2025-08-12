package lp2g06.biblioteca;

public class LeituraInvalidaException extends MinhasExcecoes {
	private static final long serialVersionUID = 1L;

	public LeituraInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Leitura invalida: " + super.getMessage());
	}
	
}
