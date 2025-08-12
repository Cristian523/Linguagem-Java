import java.util.Scanner;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class P2nX {
	private static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException, PesoInvalidoException, AlturaInvalidaException{
		MinhaListaOrdenavel pessoas = new MinhaListaOrdenavel(10);
		Homem H1, H2, H3, H4, H5;
		Mulher M1, M2, M3, M4, M5;
		
		/* Criando esses objetos "na mão" */
		H1 = new Homem("Cristian", "Cortes da Silva", 13, 04, 2006, "01234567890", 72f, 1.86f);
		H2 = new Homem("Clebio", "Lima da Silva", "16", "dezembro", "1977", "12345678909", 80f, 1.88f);
		H3 = new Homem("Cleber", "Lucas Cortes da Silva", "01", "02", "2002", "234.567.890-92", 72f, 1.72f);
		H4 = new Homem("Fulano", "de Tal", 29, 02, 2024, "123.456.780-62", 90f, 1.65f);  // Ano bissexto
		H5 = new Homem("Outro","Fulaninho de Tal", "15", "agosto", "1999", "234.569.012/75", 45f, 1.59f);
		M1 = new Mulher("Melissa", "Vitoria Cortes da Silva", 16, 05, 2000, "12345556706", 80f, 1.75f);
		M2 = new Mulher("Claudia", "Cortes", "07", "maio", "1977", "00123456797", 85f, 1.62f);
		M3 = new Mulher("Tati", "Soares", 25, 11, 1987, "34567890175", 70f, 1.68f);
		M4 = new Mulher("Vitoria", "Vitoriosa", "05", "janeiro", "2011", "34567880102", 50f, 1.4f);
		M5 = new Mulher("Mais", "um nome", "09", "7", "1998", "44567880110", 79f, 1.8f);
		/* Criando esses objetos "na mão" */
		
		/* Adicionando na lista pessoas */
		pessoas.add(H1);  pessoas.add(H2);
		pessoas.add(H3);  pessoas.add(H4);
		pessoas.add(H5);  pessoas.add(M1);
		pessoas.add(M2);  pessoas.add(M3);
		pessoas.add(M4);  pessoas.add(M5);
		/* Adicionando na lista pessoas */
		
		/* Lendo a opcao e retornando a lista ordenada */
		Criterio[] vetor = Criterio.values();   // Vetor das constantes do enum Criterio (esta em MinhaListaOrdenavel)
		ArrayList<PessoaIMC> saida; int opcao;
		while (true) {
			try {
				imprimirMenu();
				System.out.print("Digite sua opcao: ");
				String str = input.nextLine();
				if (str.length() == 0)
					break;
				else
					opcao = verificarOpcao(str);
				saida = pessoas.ordena(vetor[opcao - 1]);
				System.out.println(pessoas);
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				if (!input.hasNextLine()) {
					System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
					input.close();
					System.exit(0);
				}
			}
			
			catch (LeituraInvalidaException e) {
				System.out.println(e.getMessage());
			}
			catch (IllegalArgumentException e) {
				System.out.println("Argumento invalido: " + e.getMessage());
			}
		}
		/* Lendo a opcao e retornando a lista ordenada */
		
		input.close();
	}
	
	private static void imprimirMenu() {
		System.out.println("\nEscolha seu modo de ordenacao:\n");
		System.out.println("-------------------------");
		System.out.println("1. Alfabetica (A-Z)\n2. Alfabetica (Z-A)");
		System.out.println("3. Menor Peso\n4. Maior Peso");
		System.out.println("5. Menor IMC\n6. Maior IMC");
		System.out.println("7. Menor altura\n8. Maior altura");
		System.out.println("9. Mulher e depois Homem\n10. Homem e depois Mulher");
		System.out.println("11. Menor Data\n12. Maior Data");
		System.out.println("13. Menor Idade\n14. Maior Idade");
		System.out.println("15. Menor CPF\n16. Maior CPF");
		System.out.println("-------------------------\n");
	}
	
	private static int verificarOpcao(String str) throws LeituraInvalidaException{
		int n;
		try {
			n = Integer.parseInt(str);
			if (n < 1 || n > 16)
				throw new LeituraInvalidaException("Opcao Invalida!!");
			else
				return n;
		}
		catch (NumberFormatException e) {
			throw new LeituraInvalidaException("Opcao Invalida!!");
		}	
	}
}
