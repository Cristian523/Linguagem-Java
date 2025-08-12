package lp2g06.biblioteca;

public enum CategoriasLivro {
	ROMANCE("Romance"),
	FANTASIA("Fantasia"),
	FICCAO("Ficcao"),
	SUSPENSE("Suspense"),
	AVENTURA("Aventura"),
	DRAMA("Drama"),
	TERROR("Terror");
	
	private final String str;

	private CategoriasLivro(String str) {
		this.str = str;
	}
	public String categoriaLivro() {
		return this.str;
	}
	
	public static CategoriasLivro converter(String str) {
		try {
			return CategoriasLivro.valueOf(str.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}
	
        @Override
        public String toString() {
            return this.str;
        }
        
}
