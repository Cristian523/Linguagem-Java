package lp2g06.biblioteca;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Calendar;

public class Biblioteca {
	private Hashtable<Long, Usuario> cadastroDeUsuarios;
	private Hashtable<Integer, Livro> cadastroDeLivros;
	
	/* Construtores */
	public Biblioteca() {
		cadastroDeUsuarios = new Hashtable<>();
		cadastroDeLivros = new Hashtable<>();
	}
	public Biblioteca(String todosUsuarios, String todosLivros) throws IOException, ClassNotFoundException {
		leArqUsu(todosUsuarios);
		leArqLiv(todosLivros);
	}
	/* Construtores */
	
	
	/* salvar e carregar arquivos */
	public void salvaArqUsu(String nomeArquivo) throws IOException{
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(nomeArquivo));
		output.writeObject(this.cadastroDeUsuarios);
		output.flush();
		output.close();
	}
	
	public void salvaArqLiv(String nomeArquivo) throws IOException{
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(nomeArquivo));
		output.writeObject(this.cadastroDeLivros);
		output.flush();
		output.close();
	}
	
	@SuppressWarnings("unchecked")   // Apenas ignorando um warning de IDE sobre o casting
	public void leArqUsu(String nomeArquivo) throws IOException, ClassNotFoundException{
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(nomeArquivo));
		this.cadastroDeUsuarios = (Hashtable<Long, Usuario>) input.readObject();
		input.close();
		
	}
	
	
	@SuppressWarnings("unchecked")  // Apenas ignorando um warning de IDE sobre o casting
	public void leArqLiv(String nomeArquivo) throws IOException, ClassNotFoundException{
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(nomeArquivo));
		this.cadastroDeLivros = (Hashtable<Integer, Livro>) input.readObject();
		input.close();
	}
	/* salvar e carregar arquivos */
	
	
	
	public void cadastraUsuario(Usuario user) throws MinhasExcecoes{
		long cpf = user.getNumCPF();
		if (cadastroDeUsuarios.get(cpf) != null)
			throw new MinhasExcecoes("Nao foi possivel realizar o cadastro, pois este CPF ja esta no sistema.");
		cadastroDeUsuarios.put(cpf, user); 
	}
	
	public void cadastraLivro(Livro book) throws MinhasExcecoes {
		int codigo = book.getCodigoLivro();
		if (cadastroDeLivros.get(codigo) != null)
			throw new MinhasExcecoes("Nao foi possivel realizar o cadastro, pois este codigo de Livro ja esta no sistema.");
		cadastroDeLivros.put(codigo, book);
	}
	
	public Livro getLivro(int cod) throws LivroNaoCadastradoEx{
		Livro book = cadastroDeLivros.get(cod);
		if (book == null)
			throw new LivroNaoCadastradoEx("Este livro de codigo " + cod + " nao esta no sistema.");
		return book;
	}
	public Usuario getUsuario(long cpf) throws UsuarioNaoCadastradoEx {
		Usuario user = cadastroDeUsuarios.get(cpf);
		if (user == null)
			throw new UsuarioNaoCadastradoEx("Este usuario de cpf " + ValidaCPF.toString(cpf) + " nao esta no sistema.");
		return user;
	}
	
	
	
	public void emprestaLivro(GregorianCalendar dataLimite, boolean verificarData, Usuario user, Livro book) throws MinhasExcecoes, CopiaNaoDisponivelEx, LivroNaoCadastradoEx, UsuarioNaoCadastradoEx {
		/* Verificando se estes livro e usuario estao cadastrados */
		Usuario cliente = getUsuario(user.getNumCPF());
		Livro produto = getLivro(book.getCodigoLivro());
		/* Verificando se estes livros e usuario estao cadastrados */ 
		
		produto.empresta();
		try {produto.devolve();} // faco isso pois aqui o meu objetivo era apenas ver se eu conseguia emprestar primeiramente
		catch(NenhumaCopiaEmprestadaEx e) {} // Nunca vai entrar aqui nesse contexto
		
		fezEmprestimoUsuario(cliente ,dataLimite, verificarData, produto.getCodigoLivro());
		fezEmprestimoLivro(cliente.getNumCPF(), produto);
		produto.empresta();  // Ai sim de fato como posso emprestar eu faco isso
		cliente.empresta();
		
	}
	
	
	public void emprestaLivro(String dia, String mes, String ano, boolean verificarData, Usuario user, Livro book) throws MinhasExcecoes, CopiaNaoDisponivelEx, LivroNaoCadastradoEx, UsuarioNaoCadastradoEx {
		/* Verificando se estes livro e usuario estao cadastrados */
		Usuario cliente = getUsuario(user.getNumCPF());
		Livro produto = getLivro(book.getCodigoLivro());
		/* Verificando se estes livros e usuario estao cadastrados */ 
		
		produto.empresta();
		try {produto.devolve();} // faco isso pois aqui o meu objetivo era apenas ver se eu conseguia emprestar primeiramente
		catch(NenhumaCopiaEmprestadaEx e) {}  // Nunca vai entrar aqui nesse contexto
		
		fezEmprestimoUsuario(cliente, dia, mes, ano, verificarData, produto.getCodigoLivro());  
		fezEmprestimoLivro(cliente.getNumCPF(), produto); 
		produto.empresta();  // Ai sim de fato como posso emprestar eu faco isso
		
	}
	
	
	public void devolveLivro(Usuario user, Livro book) throws MinhasExcecoes, NenhumaCopiaEmprestadaEx, LivroNaoCadastradoEx, UsuarioNaoCadastradoEx {
		/* Verificando se estes livro e usuario estao cadastrados */
		Usuario cliente = getUsuario(user.getNumCPF());
		Livro produto = getLivro(book.getCodigoLivro());
		/* Verificando se estes livros e usuario estao cadastrados */ 
		
		if (cliente.getLivrosEmprestados() == 0)
			throw new MinhasExcecoes("O usuario nao tem livros emprestados no momento.");
		
		fezDevolucaoLivro(cliente.getNumCPF(), produto);
		fezDevolucaoUsuario(user, produto.getCodigoLivro());   
	}
	
	
	
	
	
	
	/* Metodos auxiliares */
	private void fezEmprestimoLivro(long numCPF, Livro book) throws CopiaNaoDisponivelEx{
		GregorianCalendar data_atual = new GregorianCalendar();
		book.addUsuarioHist(data_atual, null, numCPF);
	}
	
	private void fezDevolucaoLivro(long numCPF, Livro book) throws NenhumaCopiaEmprestadaEx {
		ArrayList<EmprestPara> hist = book.getHist();
		boolean foi_emprestado = false;
		GregorianCalendar data_atual = new GregorianCalendar();
		
		for(EmprestPara user: hist) {
			if (user.getNumCPF() == numCPF && user.getDataDevolucao() == null) {  // Verificando se existe algum livro que o usuario nao devolveu.
				foi_emprestado = true;
				user.setDataDevolucao(data_atual);
				break;
			}
		}
		if (!foi_emprestado)
			throw new NenhumaCopiaEmprestadaEx("Esse usuario nao solicitou o emprestimo deste livro ou todas as copias ja foram devolvidas.");
		
		book.devolve();
	}
	
	private void fezEmprestimoUsuario(Usuario user, GregorianCalendar dataLimite, boolean verificarData, int codigoLivro) throws MinhasExcecoes{ 
		GregorianCalendar dataAtual = new GregorianCalendar();
		user.addLivroHist(dataAtual, dataLimite, verificarData, codigoLivro);
		user.empresta();
	}
	private void fezEmprestimoUsuario(Usuario user, String dia, String mes, String ano, boolean verificarData, int codigoLivro) throws MinhasExcecoes{ 
		GregorianCalendar dataAtual = new GregorianCalendar();
		user.addLivroHist(dataAtual, dia, mes, ano, verificarData, codigoLivro);
		user.empresta();
	}
	private void fezDevolucaoUsuario(Usuario user, int codigoLivro) throws MinhasExcecoes{
		boolean foiEmprestado = false;
		ArrayList<Emprest> hist = user.getHist();
		
		
		for (Emprest book: hist) {
			if (book.getCodigo() == codigoLivro && book.getDataDevolucao() == null) {
				GregorianCalendar data = book.getDataLimite();
				// se entrar no if abaixo, entao o livro esta sendo devolvido apos a data limite pre-estabelecida pelo bibliotecario
				if (FuncoesDataAuxiliares.dataInferiorInferior(data.get(Calendar.DAY_OF_MONTH), data.get(Calendar.MONTH) + 1, data.get(Calendar.YEAR)))   
					user.setSituacaoMultaUsuario("Multa pendente!");
				foiEmprestado = true;
				book.setDataDevolucao(new GregorianCalendar());
				break;
			}
		}
		if (!foiEmprestado)
			throw new MinhasExcecoes("O usuario nao pegou este livro ou ja foi devolvido");
		
		user.devolve();
	}
	/* Metodos auxiliares */
	
	
	
	
	
	/* Comparadores */
	private Comparator<Integer> integerC = new Comparator<>() {
		public int compare(Integer a, Integer b) {
			return a.compareTo(b);
		}
	};
	private Comparator<Long> longC = new Comparator<>() {
		public int compare(Long a, Long b) {
			return a.compareTo(b);
		}
	};
	/* Comparadores */
	
	
	
	/* imprimeLivros() e imprimeUsuarios() */
	public String imprimeLivros() {
		String str = "Imprimindo os livros: ";
		boolean interou = false; 
		
		ArrayList<Integer> codigos = new ArrayList<>();       
		Enumeration<Integer> chaves = cadastroDeLivros.keys();
		
		while (chaves.hasMoreElements())
			codigos.add(chaves.nextElement());   // Colocando as chaves dentro do ArrayList codigos.
		
		codigos.sort(integerC);   // Ordenando esses codigos (ArrayList dos codigos)
		
		for (Integer chave: codigos) {
			interou = true;
			str += "\n\n" + cadastroDeLivros.get(chave);
		}
		
		if (!interou)
			return str + "null";
		else
			return str;
	}
	
	public String imprimeUsuarios() {
		String str = "Imprimindo os usuarios: ";
		boolean interou = false; 
		
		ArrayList<Long> cpfs = new ArrayList<>();       
		Enumeration<Long> chaves = cadastroDeUsuarios.keys();
		
		while (chaves.hasMoreElements())
			cpfs.add(chaves.nextElement());   // Colocando as chaves dentro do ArrayList cpfs.
		
		cpfs.sort(longC);   // Ordenando esses cpfs (ArrayList dos cpfs).
		
		for (Long chave: cpfs) {
			interou = true;
			str += "\n\n" + cadastroDeUsuarios.get(chave);
		}
		
		if (!interou)
			return str + "null";
		else
			return str;
	}
	/* imprimeLivros() e imprimeUsuarios() */
	
	
	
	// Teste apenas
	/* 
	public static void main(String[] args) throws MinhasExcecoes, NenhumaCopiaEmprestadaEx, LivroNaoCadastradoEx, UsuarioNaoCadastradoEx, CopiaNaoDisponivelEx{
		Biblioteca b = new Biblioteca();
		
		b.cadastraLivro(new Livro(123, "diario", "horror", 1, 0));
		b.cadastraLivro(new Livro(534, "banana", "drama", 2, 0));
		
		Usuario u1 = new Usuario("Cristian", "Cortes da Silva", 13, 04, 2006, "01234567890", 72f, 1.86f, "Rua L");
		Usuario u2 = new Usuario("Uma", "pessoa", "30", "abril", "2006", "12345678909", 55f, 1.65f, "Rua B");
		Usuario u3 = new Usuario("Fulano", "de Tal", "5", "agosto", "2008", "12345556706", 80f, 1.7f, "Rua C");
		
		b.cadastraUsuario(u1);
		b.cadastraUsuario(u2);
		b.cadastraUsuario(u3);
		
		b.emprestaLivro("06", "junho", "2025", true , b.getUsuario(12345678909L), b.getLivro(123));
		//b.devolveLivro(u2, b.getLivro(123));
		
		
		System.out.println(b.imprimeUsuarios() + "\n\n" + b.imprimeLivros());
		
	}
	*/
}
