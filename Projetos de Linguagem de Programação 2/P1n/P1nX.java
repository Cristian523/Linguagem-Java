import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class P1nX {
   	private static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
      	if (linhaDeComando(args))  
      		lerDados();
         
      	input.close();      
    }
	
	
	private static boolean linhaDeComando(String[] args) {
		try {
      		executar(args);
      		return true;	
      	}
      	catch(LeituraInvalidaException | GeneroInvalidoException | NomeInvalidoException | SobrenomeInvalidoException | DataInvalidaException | CPFInvalidoException | PesoInvalidoException | AlturaInvalidaException e) {
      		// Imprimindo o erro e colocando o menu. 
      		System.out.println("\n" + e + "\n");
      		System.out.println("Parece que voce obteve um erro de sintaxe na linha de comando. Caso venha executar este programa novamente, eis a sintaxe correta:\n");
      		System.out.println("java P1nX <genero> <nome> <sobre> <dia> <mes> ano> <CPF> <peso> <altura>\n");
      		System.out.println("O genero deve ser Masculino(represente por 'M' ou 'm') ou Feminino(represente por 'F' ou 'f').\n\n");
      		return false;
      	}
	}
	
	private static void executar(String[] args) throws LeituraInvalidaException, GeneroInvalidoException, NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException, PesoInvalidoException, AlturaInvalidaException {  // Este e para a linha de comando.
		float peso, altura;
		char genero;
		
		
		// Descobrindo se foram digitadas todas as informacoes necessarias
		if (args.length != 9)
			throw new LeituraInvalidaException("Nao foram digitadas exatamente todas as informacoes necessarias.");
		
		
		// Descobrindo se o peso e convertivel para float
		try {
			peso = Float.parseFloat(args[7]);
		}
		catch (NumberFormatException e) {
			throw new LeituraInvalidaException("O peso digitado nao e um numero ponto flutuante.");	
		}
		
		
		// Descobrindo se a altura e convertivel para float
		try {
			altura = Float.parseFloat(args[8]);
		}
		catch (NumberFormatException e) {
			throw new LeituraInvalidaException("A altura digitada nao e um numero ponto flutuante.");	
		}
		
		
		// Descobrindo se o genero e valido	
		if (args[0].length() != 1)
			throw new GeneroInvalidoException("Genero so pode conter uma letra");
		genero = args[0].charAt(0);
		if (genero != 'F' && genero != 'f' && genero != 'M' && genero != 'm')
			 throw new GeneroInvalidoException("O genero digitado nao e Masculino ou Feminino.");
		
		
		// Tentando criar o objeto e, em caso de sucesso, retorna-o
		Homem H;
		Mulher M;
		if (genero == 'M' || genero == 'm') {
			H = new Homem(args[1], args[2], args[3], args[4], args[5], args[6], peso, altura);
			System.out.println("\n" + H + "\n");
		}
		else {
			M = new Mulher(args[1], args[2], args[3], args[4], args[5], args[6], peso, altura);
			System.out.println("\n" + M + "\n");
		}
	}
	
	
	
	
	
	private static void lerDados() {  // Este e para a entrada do usuario e imprimir os dados
		int n = 0, i;
		float entradaFloat;
		boolean leuTamanho = false, terminouCedo = false;
		String entrada;
		String dia, mes, ano;
		char genero = 'a';
		boolean leuGenero = false, leuNome = false, leuSobrenome = false, leuPeso = false, leuAltura = false, leuCPF = false, leuData = false;
		
		
		while (!leuTamanho) {  // Lendo o tamanho do array ate ser digitado um numero valido.
			System.out.print("Digite quantas pessoas a mais quer colocar: ");
			try {
				entrada = input.nextLine();
				n = Integer.parseInt(entrada);
			}
			catch(NumberFormatException e) {
				System.out.println("Leitura invalida: Você não digitou um numero inteiro para ser a quantidade de pessoas. Tente novamente.\n");
				continue;
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usurario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			
			if (n >= 0)
				leuTamanho = true;
			else
				System.out.println("Leitura invalida: Você digitou um numero negativo. Tente novamente.\n"); 	
		}	
		
		Pessoa[] array = new Pessoa[n]; // Criando a instancia do array.
		
		for(i = 0; i < n; i++) {  // Lendo as informacoes das pessoas
			leuGenero = false; leuNome = false; leuSobrenome = false; leuPeso = false; leuAltura = false; leuCPF = false; leuData = false;
			System.out.printf("\n%s° Pessoa: \n\n", i + 1);
			
			while(!leuGenero) {  // Lendo o genero da pessoa para criar o objeto
				System.out.print("Essa pessoa e do genero feminino ou masculino(f ou m)? ");
				try {
					entrada = input.nextLine();
					if (entrada.length() == 0) {
						terminouCedo = true;
						break;
					}
					else if (entrada.length() > 1)
						throw new LeituraInvalidaException("Genero pode conter apenas uma letra. Tente novamente.\n");
					
					genero = entrada.charAt(0); 
					if (genero != 'f' && genero != 'F' && genero != 'm' && genero != 'M')
						throw new LeituraInvalidaException("O genero digitado é invalido. Tente novamente.\n");
					else
						leuGenero = true;
				}
				catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usurario (isso me causou problemas...)
					if (!input.hasNextLine()) {
						System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
						input.close();
						System.exit(0);
					}
				}
				catch(LeituraInvalidaException e) {
					System.out.println(e);
				}
			}
			
			if (terminouCedo)  // Isso e so para terminar cedo caso tenha digitado nada
			 	break;
			
			if (genero == 'F' || genero == 'f')   // Alocando um espaco para uma pessoa.
				array[i] = new Mulher();
			else
				array[i] = new Homem();
				
				
			while (!leuNome) {  // Lendo o nome
				System.out.print("Digite o nome: ");
				try {
					entrada = input.nextLine();
					if (entrada.length() == 0) {
						terminouCedo = true;	
						break;
					}
					array[i].setNome(entrada);
					leuNome = true;
				}
				catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usurario (isso me causou problemas...)
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
			
			if (terminouCedo)  // Isso e so para terminar cedo caso tenha digitado nada
			 	break;
			
			while (!leuSobrenome) {  // Lendo o sobrenome
				System.out.print("Digite o sobrenome: ");
				try {
					entrada = input.nextLine();
					if (entrada.length() == 0) {
						terminouCedo = true;	
						break;
					}
					array[i].setSobreNome(entrada);
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
			
			if (terminouCedo)  // Isso e so para terminar cedo caso tenha digitado nada
			 	break;
			
			while(!leuData) {  // Lendo a data de nascimento
				try {
					System.out.print("Digite o dia de nascimento: ");
					dia = input.nextLine();
					if (dia.length() == 0) {
						terminouCedo = true;	
						break;
					}
					System.out.print("Digite o mes de nascimento: ");
					mes = input.nextLine();
					if (mes.length() == 0) {
						terminouCedo = true;	
						break;
					}
					System.out.print("Digite o ano de nascimento: ");
					ano = input.nextLine();
					if (ano.length() == 0) {
						terminouCedo = true;	
						break;
					}
					array[i].setDataNasc(dia, mes, ano);
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
			
			if (terminouCedo)  // Isso e so para terminar cedo caso tenha digitado nada
			 	break;
			
			while(!leuCPF) {  // Lendo o CPF
				System.out.print("Digite o CPF: ");
				try {
					entrada = input.nextLine();
					if (entrada.length() == 0) {
						terminouCedo = true;	
						break;
					}
					array[i].setNumCPF(entrada);
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
			
			if (terminouCedo)  // Isso e so para terminar cedo caso tenha digitado nada
			 	break;
			
			while(!leuPeso) {  // Lendo o peso
				System.out.print("Digite o peso: ");
				try {
					entrada = input.nextLine();
					if (entrada.length() == 0) {
						terminouCedo = true;	
						break;
					}
					entradaFloat = Float.parseFloat(entrada);
					array[i].setPeso(entradaFloat);
					leuPeso = true;
				}
				catch(NumberFormatException e) {
					System.out.println("Leitura invalida: Voce nao digitou um numero ponto flutuante para o peso. Tente novamente.\n");
				}
				catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
					if (!input.hasNextLine()) {
						System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
						input.close();
						System.exit(0);
					}
				}
		
				catch(PesoInvalidoException e) {
					System.out.println(e + " Tente novamente.\n");
				}	
			}
			
			if (terminouCedo)  // Isso e so para terminar cedo caso tenha digitado nada
			 	break;
			
			while(!leuAltura) {  // Lendo a altura
				System.out.print("Digite a altura: ");
				try {
					entrada = input.nextLine();
					if (entrada.length() == 0) {
						terminouCedo = true;	
						break;
					}
					entradaFloat = Float.parseFloat(entrada);
					array[i].setAltura(entradaFloat);
					leuAltura = true;
				}
				catch(NumberFormatException e) {
					System.out.println("Leitura invalida: voce nao digitou um numero ponto flutuante para a altura. Tente novamente.\n");
				}
				catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
					if (!input.hasNextLine()) {
						System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
						input.close();
						System.exit(0);
					}
				}
				
				catch(AlturaInvalidaException e) {
					System.out.println(e + " Tente novamente.\n");
				}	
			}
			
			if (terminouCedo)  // Isso e so para terminar cedo caso tenha digitado nada
			 	break;
		}	
			
			
			
		n = Pessoa.numPessoas() - 1; // Atualizando o numero de pessoas E desconsiderando a pessoa passada pela linha de comando
		if (terminouCedo && leuGenero && n > 0) // Isso aqui e para caso tenha ficado um dado de uma pessoa incompleta, logo nao deve ser considerada
			n--;
		System.out.println("\nImprimindo os dados das pessoas:\n");
		for (i = 0; i < n; i++)  // Imprimindo os dados de cada pessoa
			System.out.println(array[i] + "\n");			
		
		int numero_mulheres = 0, numero_homens;
		
		for (i = 0; i < n; i++) {  // Contando o numero de mulheres com o instanceof
			if (array[i] instanceof Mulher)
				numero_mulheres++;
		}
		numero_homens = n - numero_mulheres;
		System.out.println("Quantidade de Homens: " + numero_homens + "\nQuantidade de Mulheres: " + numero_mulheres);
		
			
	}
	
	
}


