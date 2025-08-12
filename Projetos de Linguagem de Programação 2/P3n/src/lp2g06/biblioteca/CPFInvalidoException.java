package lp2g06.biblioteca;

public class CPFInvalidoException extends MinhasExcecoes {
	private static final long serialVersionUID = 1L;

	public CPFInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("CPF invalido: " + super.getMessage());
	}
	
}
