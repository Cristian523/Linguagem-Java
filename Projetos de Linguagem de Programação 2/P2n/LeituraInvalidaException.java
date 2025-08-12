public class LeituraInvalidaException extends Exception {
	public LeituraInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Leitura invalida: " + super.getMessage());
	}
	
}
