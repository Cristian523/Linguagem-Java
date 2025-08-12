public class SobrenomeInvalidoException extends Exception {
	public SobrenomeInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Sobrenome invalido: " + super.getMessage());
	}
	
}
