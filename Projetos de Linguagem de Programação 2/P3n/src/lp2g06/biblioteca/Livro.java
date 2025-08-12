package lp2g06.biblioteca;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.io.Serializable;

public class Livro implements Serializable{  
	private static final long serialVersionUID = 1L;
	
	private int codigoLivro;
	private String tituloLivro;
	private CategoriasLivro categoria;
	private int copias = 0;
	private int emprestados;
	
	private ArrayList<EmprestPara> hist;
	
	/* Construtores */
	public Livro(int codigoLivro, String tituloLivro, String categoria, int copias, int emprestados) throws MinhasExcecoes { 
		setCodigoLivro(codigoLivro);
		setTituloLivro(tituloLivro);
		setCategoria(categoria);
		setCopias(copias);
		setEmprestados(emprestados);
		this.hist = new ArrayList<>();
	}
	
	public Livro(int codigoLivro, String tituloLivro, CategoriasLivro categoria, int copias, int emprestados) {
		this.codigoLivro = codigoLivro;
		this.tituloLivro = tituloLivro;
		this.categoria = categoria;
		this.copias = copias;
		this.emprestados = emprestados;
		this.hist = new ArrayList<>();
	}
	/* Construtores */

	
	/* Getters */
	public int getCodigoLivro() {
		return codigoLivro;
	}
	public String getTituloLivro() {
		return tituloLivro;
	}
	public CategoriasLivro getCategoria() {
		return categoria;
	}
	public int getCopias() {
		return copias;
	}
	public int getEmprestados() {
		return emprestados;
	}
	public ArrayList<EmprestPara> getHist() {
		return hist;
	}
	
	/* Getters */
	
	/* Setters */
	public void setCodigoLivro(int codigoLivro) throws MinhasExcecoes{
		if (codigoLivro >= 1 && codigoLivro <= 999)
			this.codigoLivro = codigoLivro;
		else
			throw new MinhasExcecoes("Codigo do livro e invalido, pois precisa pertencer ao intervalo entre 1 a 999.");
	}
	
	public void setTituloLivro(String tituloLivro) throws MinhasExcecoes {
		if (tituloLivro.length() == 0)
			throw new MinhasExcecoes("O titulo deve ter um nome com pelo menos um caractere.");
		else
			this.tituloLivro = tituloLivro;
	}
	
	public void setCategoria(String categoria) throws MinhasExcecoes{
		CategoriasLivro meuLivro = CategoriasLivro.converter(categoria);
		if (meuLivro == null)
			throw new MinhasExcecoes("A categoria do livro citada nao existe na biblioteca.");
		this.categoria = meuLivro;
	}
	
	public void setCopias(int copias) throws MinhasExcecoes {
		if (copias < 0)
			throw new MinhasExcecoes("Quantidade de copias nao pode ser um numero negativo.");
		this.copias = copias;
	}
	
	public void setEmprestados(int emprestados) throws MinhasExcecoes {
		if (emprestados < 0 || emprestados > copias)
			throw new MinhasExcecoes("Quantidade de livros emprestados nao pode ser um numero negativo ou maior que o numero de copias.");
		this.emprestados = emprestados;
	}
	/* Setters */
	
	
	
	/* default */ void addUsuarioHist(GregorianCalendar dataLocacao, GregorianCalendar dataDevolucao, long numCPF) {
		EmprestPara EmprestadoPara = new EmprestPara(dataLocacao, dataDevolucao, numCPF);
		hist.add(EmprestadoPara);
	}
	
	/* default */ void empresta() throws CopiaNaoDisponivelEx { 
		int diferenca = copias - emprestados;
		if (diferenca <= 0)
			throw new CopiaNaoDisponivelEx("Todas as copias ja foram emprestadas.");
	
		emprestados++;
	}
	
	/* default */ void devolve() throws NenhumaCopiaEmprestadaEx { 
		if (emprestados == 0)
			throw new NenhumaCopiaEmprestadaEx("Nenhuma copia ainda foi emprestada.");
		emprestados--;
	}
	
	
	
	private String historico() {
		String str = "\n---------------";
		boolean temPessoas = false;
		for (EmprestPara pessoas: hist) {
			str += "\n\n" + pessoas;
			temPessoas = true;
		}
		str += "\n\n---------------";
		if (!temPessoas) str = "null";
		return "\nO historico e: " + str + "\n";
	}

	@Override
	public String toString() {
		return  "codigoLivro: " + getCodigoLivro() + "\n" +
				"tituloLivro: " + getTituloLivro() + "\n" +
				"categoria: " + getCategoria() + "\n" +
				"copias: " + getCopias() + "\n" +
				"emprestados: " + getEmprestados() + "\n" +
				historico()
				;
	}
	
	
	/*
	public static void main(String[] args) { 
		Livro ficcao; 
			try {
				ficcao = new Livro(568, "O menino do pijama listrado", "ficcao", 10, 0);
				ficcao.empresta(01234567890L);
				ficcao.devolve(01234567890L);
				ficcao.empresta(12345678909L);
				
				System.out.println(ficcao);
			}
			catch (MinhasExcecoes | NenhumaCopiaEmprestadaEx | CopiaNaoDisponivelEx e) {
				System.out.println(e);
			}
		
	}
	*/
}
