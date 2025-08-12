import java.util.Scanner;
import java.util.NoSuchElementException;
import lp2g06.biblioteca.*;

public class EntradaDados {
	private Scanner input;
	
	public EntradaDados() {
		input = new Scanner(System.in);
	}
	
	public Usuario nextUsuario() {
		// OBSERVACAO: muito do que escrevi aqui eu reaproveitei do P1nX.java de P1n, apenas fazendo as devidas adaptacoes para o meu contexto atual.
		// Diante da observacao acima, se tiver algo que nao gostou em P1nX.java, tambem nao deve gostar aqui...
		// Removi a entrada de peso e altura que tinha colocado, já que isso não faz sentido em uma biblioteca!
		
		float entradaFloat;
		String entrada;
		String dia, mes, ano;
		boolean leuNome = false, leuSobrenome = false, leuCPF = false, leuData = false;
		
		Usuario user = new Usuario();
		
		while (!leuNome) {  // Lendo o nome
			System.out.print("Digite o nome: ");
			try {
				entrada = input.nextLine();
				user.setNome(entrada);
				leuNome = true;
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			catch (NomeInvalidoException e) {
				System.out.println(e + " Tente novamente.\n");
			}
		}
		
		while (!leuSobrenome) {  // Lendo o sobrenome
			System.out.print("Digite o sobrenome: ");
			try {
				entrada = input.nextLine();
				user.setSobreNome(entrada);
				leuSobrenome = true;
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			catch (SobrenomeInvalidoException e) {
				System.out.println(e + " Tente novamente.\n");
			}
		}
		
		while(!leuData) {  // Lendo a data de nascimento
			try {
				System.out.print("Digite o dia de nascimento: ");
				dia = input.nextLine();
				System.out.print("Digite o mes de nascimento: ");
				mes = input.nextLine();
				System.out.print("Digite o ano de nascimento: ");
				ano = input.nextLine();
				user.setDataNasc(dia, mes, ano);
				leuData = true; 
			
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			catch (DataInvalidaException e) {
				System.out.println(e + " Tente novamente.\n");
			}
		}
		
		while(!leuCPF) {  // Lendo o CPF
			System.out.print("Digite o CPF: ");
			try {
				entrada = input.nextLine();
				user.setNumCPF(entrada);
				leuCPF = true;
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			catch(CPFInvalidoException e) {
				System.out.println(e + " Tente novamente.\n");
			}
			
		}
		
		try {  // lendo o endereco
			System.out.print("Digite o endereco: ");
			entrada = input.nextLine();
			user.setEndereco(entrada);   // Sim, aqui sem tratamento, vai que o cliente nao quer colocar o seu endereco e essa String pode ser qualquer coisa·
		}
		
		catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
			if (!input.hasNextLine()) {
				System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
				input.close();
				System.exit(0);
			}
		}
		
		return user;
	}
	
	
	
	public Livro nextLivro() {
		// OBSERVACAO: apesar dos setters de Livro ja terem uma certa verificacao, decidi fazer essas verificacoes manualmente para depois retornar uma instancia de Livro.
		
		int codigoLivro, copias,  emprestados;
		String tituloLivro, entrada;
		CategoriasLivro categoria;
		
		while(true) {    // Lendo o codigo
			try {
				System.out.print("Digite o codigo do livro: ");
				entrada = input.nextLine();
				codigoLivro = Integer.parseInt(entrada);
				if (codigoLivro >= 1 && codigoLivro <= 999)
					break;
				else
					System.out.println("Codigo invalido: Voce digitou um inteiro nao pertencente ao intervalo entre 1 a 999. Tente novamente.\n");
				
			}
			catch (NumberFormatException e) {
				System.out.println("Leitura invalida: Voce nao digitou um numero inteiro para o codigo do livro. Tente novamente.\n");
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
		}
		
		while(true) {    // Lendo o titulo do livro.
			try {
				System.out.print("Digite o titulo do livro: ");
				tituloLivro = input.nextLine();
				if (tituloLivro.length() > 0)
					break;
				else
					System.out.println("Titulo invalido: O titulo precisa ter pelo menos um caractere. Tente novamente.\n");
				
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			
		}
		
		while(true) {  // lendo a categoria
			try {
				System.out.print("Digite a categoria do livro: ");
				entrada = input.nextLine();
				categoria = CategoriasLivro.converter(entrada);
				if (categoria != null)
					break;
				else {
					System.out.println("Categoria invalida: Voce digitou uma categoria de livro que nao existe na biblioteca.");
					System.out.println("Tente novamente, lembrando que as categorias são: [ROMANCE, FANTASIA, FICCAO, SUSPENSE, AVENTURA, DRAMA, TERROR]\n");
				}
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			
			
		}
		
		
		while(true) {   // Lendo o numero de copias
			try {	
				System.out.print("Digite o numero de copias do livro: ");
				entrada = input.nextLine();
				copias = Integer.parseInt(entrada);
				if (copias >= 0)
					break;
				else
					System.out.println("Copia invalida: Voce digitou um inteiro negativo. Tente novamente.\n");
				
			}
			catch (NumberFormatException e) {
				System.out.println("Leitura invalida: Voce nao digitou um numero inteiro para o numero de copias do livro. Tente novamente.\n");
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
		}
		
		while(true) {    // lendo o numero de emprestimos
			try {	
				System.out.print("Digite o numero de emprestimos do livro: ");
				entrada = input.nextLine();
				emprestados = Integer.parseInt(entrada);
				if (emprestados >= 0  && emprestados <= copias)
					break;
				else
					System.out.println("Emprestimo invalido: Voce digitou um inteiro nao pertencente ao intervalo entre 0 e o numero de copias, que neste caso e " + copias + ". Tente novamente.\n");
				
			}
			catch (NumberFormatException e) {
				System.out.println("Leitura invalida: Voce nao digitou um numero inteiro para o numero de emprestimos do livro. Tente novamente.\n");
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
		}
		
		return new Livro(codigoLivro, tituloLivro, categoria, copias, emprestados);
	}
}
