package lp2g06.biblioteca;

public class MinhasExcecoes extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MinhasExcecoes() {
		super("Um erro desconhecido ocorreu");
	}
	
	public MinhasExcecoes(String mensagem) {
		super(mensagem);
	}

	@Override
	public String toString() {
		return ("Um erro ocorreu: " + super.getMessage());
	
	}
}
