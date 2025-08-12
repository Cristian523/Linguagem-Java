public class PesoInvalidoException extends Exception {
	public PesoInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Peso invalido: " + super.getMessage());
	}

}
