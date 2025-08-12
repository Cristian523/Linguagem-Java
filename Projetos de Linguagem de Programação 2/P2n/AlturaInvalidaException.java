public class AlturaInvalidaException extends Exception {
	public AlturaInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Altura invalida: " + super.getMessage());
	}
	
}
