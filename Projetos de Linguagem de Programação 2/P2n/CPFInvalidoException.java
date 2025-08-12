public class CPFInvalidoException extends Exception {
	public CPFInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("CPF invalido: " + super.getMessage());
	}
	
}
