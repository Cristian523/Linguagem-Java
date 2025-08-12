package lp2g06.biblioteca;

public enum CategoriasLivro {
	ROMANCE(1),
	FANTASIA(2),
	FICCAO(3),
	SUSPENSE(4),
	AVENTURA(5),
	DRAMA(6),
	TERROR(7);
	
	private final int num;

	private CategoriasLivro(int num) {
		this.num = num;
	}
	public int numeroLivro() {
		return this.num;
	}
	
	public static CategoriasLivro converter(String str) {
		try {
			return CategoriasLivro.valueOf(str.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}
	
}
