package lp2g06.biblioteca;

public class DataInvalidaException extends MinhasExcecoes {
	private static final long serialVersionUID = 1L;

	public DataInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Data invalida: " + super.getMessage());
	}
	
}
