package lp2g06.biblioteca;

import java.io.Serializable;

public class AlturaInvalidaException extends MinhasExcecoes implements Serializable{
	private static final long serialVersionUID = 1L;

	public AlturaInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Altura invalida: " + super.getMessage());
	}
	
}
