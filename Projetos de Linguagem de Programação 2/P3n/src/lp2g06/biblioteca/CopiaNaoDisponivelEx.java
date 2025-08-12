package lp2g06.biblioteca;

public class CopiaNaoDisponivelEx extends Exception {
	private static final long serialVersionUID = 1L;

	public CopiaNaoDisponivelEx() {
		super("Um erro desconhecido ocorreu");
	}
	public CopiaNaoDisponivelEx(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return ("CopiaNaoDisponivelEx: " + super.getMessage());
	}
}