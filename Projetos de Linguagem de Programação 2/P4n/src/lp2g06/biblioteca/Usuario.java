package lp2g06.biblioteca;

import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.io.Serializable;

public class Usuario extends Pessoa implements Serializable{
	private static final long serialVersionUID = 1L;  // apenas uma coisinha que a IDE reclama se não tiver

	private String endereco;
	
	private ArrayList<Emprest> hist;
	private int livrosEmprestados = 0;
	private String situacaoMultaUsuario;
	
	/* Construtores */
	public Usuario() {
		super();
		hist = new ArrayList<>();
		this.situacaoMultaUsuario = "Regular";
	}
	
	public Usuario(String novoNome, String novoSobreNome, int dia, int mes, int ano, String novoCPF, String endereco) throws MinhasExcecoes{
		super(novoNome, novoSobreNome, dia, mes, ano, novoCPF);
		setEndereco(endereco);
		hist = new ArrayList<>();
		this.situacaoMultaUsuario = "Regular";
	}

	public Usuario(String novoNome, String novoSobreNome, String dia, String mes, String ano, String novoCPF, String endereco) throws MinhasExcecoes{
		super(novoNome, novoSobreNome, dia, mes, ano, novoCPF);
		setEndereco(endereco);
		hist = new ArrayList<>();
		this.situacaoMultaUsuario = "Regular";
	}
	/* Construtores */
	
	
	/* Getters */
	public String getEndereco() {
		return endereco;
	}
	public int getLivrosEmprestados() {
		return livrosEmprestados;
	}
	public ArrayList<Emprest> getHist() {
		return hist;
	}
	public String getSituacaoMultaUsuario() {
		return this.situacaoMultaUsuario;
	}
	/* Getters */

	/* Setter */
	public void setEndereco(String endereco) throws MinhasExcecoes{
            if (endereco.length() > 0)
                this.endereco = endereco;
            else
                throw new MinhasExcecoes("O campo endereço precisa ter pelo menos um caractere.");
	}
	public void setSituacaoMultaUsuario(String str) {
		this.situacaoMultaUsuario = str; 
	}
	/* Setter */
	
	
	/* default */ void addLivroHist(GregorianCalendar dataLocacao, GregorianCalendar dataLimite, boolean verificarData, int codigoLivro) throws MinhasExcecoes{
		Emprest livroEmprestado = new Emprest(dataLocacao, null, dataLimite, verificarData, codigoLivro);  // sem data de devolucao inicialmente
		hist.add(livroEmprestado);
	}
	/* default */ void addLivroHist(GregorianCalendar dataLocacao, String dia, String mes, String ano, boolean verificarData, int codigoLivro) throws MinhasExcecoes{
		Emprest livroEmprestado = new Emprest(dataLocacao, null, dia, mes, ano, verificarData, codigoLivro);   // sem data de devolucao inicialmente
		hist.add(livroEmprestado);
	}
	
	
	// Estes dois metodos abaixo estao so aqui para manter parecido com que fiz em Livro.
	/* default */ void empresta() {   // aqui fica sem verificacao mesmo ja que o empresta() de Livro ja faz essa verificacao e eu vou o chamar primeiro antes desse.
		livrosEmprestados++;
	}
	/* default */ void devolve() throws MinhasExcecoes{
		if (livrosEmprestados <= 0)
			throw new MinhasExcecoes("Nao há livros emprestados para este usuario.");
		livrosEmprestados--;
	}
	
	
	private String historico() {
		boolean semLivros = true;
		String str = "\n---------------";
		
		for (Emprest livros: hist) {
			semLivros = false;
			str += "\n\n" + livros;
		}
		str += "\n\n---------------";
		if (semLivros)
			str = "null";
		return "\nO historico e: " + str + "\n";
	}
	
	@Override
	public String toString() {
		return  super.toString() + "\n" + 
				"Endereco: " + getEndereco() + "\n" +
				"Situacao: " + situacaoMultaUsuario + "\n" +
				historico() 
				;
	}
}
