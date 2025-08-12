public class DataInvalidaException extends Exception {
	public DataInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Data invalida: " + super.getMessage());
	}
	
}
