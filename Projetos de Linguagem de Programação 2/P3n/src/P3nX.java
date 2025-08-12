import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.GregorianCalendar;
import java.io.*;
import lp2g06.biblioteca.*;

public class P3nX {
	private Scanner input = new Scanner(System.in);
	private Biblioteca minhaBiblioteca;
	private String nomeHistoricoUsuario = null;
	private String nomeHistoricoLivro = null;
	
	// OBSERVACAO: Ao sair do modulo emprestimo(), sera feito uma tentativa de salvamento automatico (se selecionar a opcao 5) se os arquivos de historico (as Strings acima) forem incializados.
	public static void main(String[] args) {
		P3nX meuPrograma = new P3nX();
		meuPrograma.inicializacao();   // Questiona se o bibliotecario quer inicializar do zero o cadastro ou se quer carregar os arquivos de historico. 
		
		String menu = "\n\nMENU PRINCIPAL:\n"
				    + "1) manutencao (carregar/salvar arquivos)\n"
				    + "2) cadastro\n"
				    + "3) emprestimo\n"
				    + "4) relatorio\n"
				    + "5) fechar programa\n";
							
		
		boolean ficar = true;
		while(ficar) {
			int opcao = meuPrograma.lerOpcao(menu, 1, 5);
			switch(opcao) {
				case 1:
					meuPrograma.manutencao();
					break;
					
				case 2:
					meuPrograma.cadastro();
					break;
					
				case 3:
					meuPrograma.emprestimo();
					break;
					
				case 4:
					meuPrograma.relatorio();
					break;
					
				default:
					ficar = false;
					break;
			}	
		}
		System.out.println("\nEncerrando o programa...");
		meuPrograma.minhaBiblioteca = null;
		meuPrograma.input.close();
	}
	
	
	
	/* Os modulos do meu programa */
	/* Os modulos do meu programa */
	/* Os modulos do meu programa */
	private void inicializacao() {
		String menu = "\nSuas opcoes sao: \n"
				    + "1) carregar biblioteca (com historicos existentes)\n"
				    + "2) comecar o cadastro zerado\n";
		
		boolean ficar = true;
		String historicoUsuario, historicoLivro;
		
		System.out.println("Ola bibliotecario!\nPara o correto inicializamento, tem o seguinte:\n");
		while(ficar) {
			int opcao = lerOpcao(menu, 1, 2);
			if (opcao == 1) {
				historicoUsuario = lerUmaString("Digite o nome do historico de usuarios: ");
				if (historicoUsuario.charAt(0) != 'u') {
					System.out.println("Leitura invalida: seu arquivo precisa ter como primeiro caractere o 'u'. Tente novamente:\n");
					continue;
				}
				
				historicoLivro = lerUmaString("Digite o nome do historico de livros: ");
				if (historicoLivro.charAt(0) != 'l') {
					System.out.println("Leitura invalida: seu arquivo precisa ter como primeiro caractere o 'l'. Tente novamente:\n");
					continue;
				}
				
				try {
					minhaBiblioteca = new Biblioteca(historicoUsuario, historicoLivro);
					this.nomeHistoricoUsuario = historicoUsuario;
					this.nomeHistoricoLivro = historicoLivro;
					
					ficar = false;
				}
				catch(ClassNotFoundException | IOException e) {
					System.out.println("Não foi possivel carregar a biblioteca: \n");
					System.out.println(e + "\n");
				}
				
			}
			else {
				minhaBiblioteca = new Biblioteca();
				ficar = false;
			}
			
		}
		
		
		
		
	}
	
	private void manutencao() {  // Completar depois!
		String menu = "\nSuas opcoes aqui em manutencao sao: \n"
				    + "1) salvar historico de usuarios\n"
				    + "2) salvar historico de livros\n"
				    + "3) ler historico de usuarios\n"
				    + "4) ler historico de livros\n"
				    + "5) voltar ao menu anterior\n";
		
		boolean ficar = true;
		while(ficar) {
			int opcao = lerOpcao(menu, 1, 5);
			switch(opcao) {
				case 1:
					if(salvarHistoricoUsuario())
						System.out.println("Salvamento concluido!!!");
					break;
				case 2:
					if(salvarHistoricoLivro())
						System.out.println("Salvamento concluido!!!");
					break;
				case 3:
					lerHistoricoUsuario();
					break;
				case 4:
					lerHistoricoLivro();
					break;
				default:
					ficar = false;
					break;
			
			
			}
			
		}
	}
	
	
	
	private void cadastro() {
		EntradaDados entrada = new EntradaDados();    // cria essa instancia para usar os metodos nextUsuario() e nextLivro() que criei.
		String str;
		boolean permanecer = true;
		Usuario user;
		Livro book;
		
		String menu = "\nAqui em cadastro voce tem as seguintes opcoes: \n"
					+ "1) cadastrar usuario\n"
					+ "2) cadastrar livro\n"
					+ "3) salvar cadastro\n"
					+ "4) voltar ao menu anterior\n";
		
		
		while(permanecer) {
			int opcao = lerOpcao(menu, 1, 4);
			switch(opcao) {
				case 1:
					System.out.println();
					user = entrada.nextUsuario();
					try {
						minhaBiblioteca.cadastraUsuario(user);
						
						boolean ficar = true;
						boolean foiSalvo = false;
						while(ficar) {
							System.out.print("Voce quer salvar o cadastro realizado(S/N)? ");
							str = input.nextLine();
							if (str.length() == 1) {
								if (str.charAt(0) == 'S' || str.charAt(0) == 's') {
									String menuInterno = "1) salvar o arquivo que ja esta usando (backup)\n"
													   + "2) salvar em outro arquivo\n";
									
									
									int opcaoInterna = lerOpcao(menuInterno, 1, 2);
									if (opcaoInterna == 1) {
										if (nomeHistoricoUsuario == null) {
											System.out.println("Erro: voce nao inicializou um arquivo ainda.\n");
											continue;
										}
										else {
											try {
												minhaBiblioteca.salvaArqUsu(nomeHistoricoUsuario);
												foiSalvo = true;
											}
											catch(IOException e) {
												System.out.println("Nao foi possivel salvar o arquivo: ");
												System.out.println(e + "\n");
												continue;
											}
										}
											
									}
									else
										foiSalvo = salvarHistoricoUsuario();   // Aqui e o usuario digitando um nome de arquivo.
								
									
								if (foiSalvo) 
									System.out.println("Salvamento concluido!!!\n");
								ficar = false;
								}
								else if (str.charAt(0) == 'N' || str.charAt(0) == 'n')
									ficar = false;
								else
									System.out.println("Leitura invalida: Voce nao digitou S ou N. Tente novamente.\n");
							}
							else
								System.out.println("Leitura invalida: sua resposta precisa ter apenas um caractere. Tente novamente.\n");
						}
					}
					catch(MinhasExcecoes e) {
						System.out.println(e + "\n");
					}
					catch(NoSuchElementException e) {
						encerrarForcado();
					}
					
					System.out.println();
					break;
					
				case 2:
					System.out.println();
					book = entrada.nextLivro();
					try {
						minhaBiblioteca.cadastraLivro(book);
						
						boolean ficar = true;
						boolean foiSalvo = false;
						while(ficar) {
							System.out.print("Voce quer salvar o cadastro realizado(S/N)? ");
							str = input.nextLine();
							if (str.length() == 1) {
								if (str.charAt(0) == 'S' || str.charAt(0) == 's') {
									String menuInterno = "1) salvar o arquivo que ja esta usando (backup)\n"
													   + "2) salvar em outro arquivo\n";
											
											
									int opcaoInterna = lerOpcao(menuInterno, 1, 2);
									if (opcaoInterna == 1) {
										if (nomeHistoricoLivro == null) {
											System.out.println("Erro: voce nao inicializou um arquivo ainda.\n");
											continue;
										}
										else {
											try {
												minhaBiblioteca.salvaArqLiv(nomeHistoricoLivro);
												foiSalvo = true;
											}
											catch(IOException e) {
												System.out.println("Nao foi possivel salvar o arquivo: ");
												System.out.println(e + "\n");
												continue;
											}
										}
													
									}
									else
										foiSalvo = salvarHistoricoLivro();   // Aqui e o usuario digitando um nome de arquivo.
										
											
									if (foiSalvo) 
										System.out.println("Salvamento concluido!!!\n");
									ficar = false;
								}
								else if (str.charAt(0) == 'N' || str.charAt(0) == 'n')
									ficar = false;
								else
									System.out.println("Leitura invalida: Voce nao digitou S ou N. Tente novamente.\n");
							}
							else
								System.out.println("Leitura invalida: sua resposta precisa ter apenas um caractere. Tente novamente.\n");
						}
					}
					catch(MinhasExcecoes e) {
						System.out.println(e + "\n");
					}
					catch(NoSuchElementException e) {
						encerrarForcado();
					}
					
					System.out.println();
					break;
				
				case 3:
					boolean sair = false;
					String menuInterno = "\nPara salvamento, tem as seguintes opcoes: \n"
									   + "1) salvar usuarios\n"
									   + "2) salvar livros\n"
									   + "3) voltar para  menu anterior\n";
					
					while(sair == false) {
						int opcaoInterna = lerOpcao(menuInterno, 1, 3);
						switch(opcaoInterna) {
							case 1:
								if (salvarHistoricoUsuario())
									System.out.println("Salvamento concluido!!!\n");
								break;
							case 2:
								if (salvarHistoricoLivro())
									System.out.println("Salvamento concluido!!!\n");
								break;
							default:
								sair = true;
								break;
						}
						
					}
					System.out.println();
					break;
					
				default:
					permanecer = false;
					break;
			}
			
		}

		entrada = null;
	}
	
	
	
	private void emprestimo() {    
		String menu = "\nSuas opcoes em emprestimo sao:\n"
				    + "1) exibir o cadastro de livros\n"
				    + "2) fazer um emprestimo (sem verificacao de data limite abaixo)\n"
				    + "3) fazer um emprestimo (data limite deve ser posterior a data atual)\n"
				    + "4) fazer uma devolucao\n"
				    + "5) voltar ao menu anterior\n"
				    + "6) voltar ao menu anterior (sem salvar automaticamente)\n";
		boolean verificarData = true;
		boolean ficar = true;
		while(ficar) {
			
			Usuario user;
			Livro book;
			String userCPF, bookCodigo;
			GregorianCalendar dataLimite;
			int opcao = lerOpcao(menu, 1, 6);
			switch(opcao) {
				case 1:
					System.out.println(minhaBiblioteca.imprimeLivros());
					break;
					
				case 2:
					verificarData = false;
					//break;  //esta comentado de proposito, ja que nao quero verificar a data e irei para o case 3
					
				case 3:
					userCPF = lerUmaString("Digite o CPF do usuario cadastrado: ");
					bookCodigo = lerUmaString("Digite o codigo do livro: ");
					try {
						user = minhaBiblioteca.getUsuario(Long.parseLong(userCPF));
						book = minhaBiblioteca.getLivro(Integer.parseInt(bookCodigo));
						
						// Verificando se o usuario esta regular
						if (!user.getSituacaoMultaUsuario().equals("Regular")) {
							if (!pagarMultaUsuario(user, "Este usuario tem uma multa pendente. Ele precisa pagar esta multa para prosseguir com o emprestimo."))
								continue;  // ja que não foi pago, logo nao pode pedir outro emprestimo!
						}
							
						
						dataLimite = lerDataLimite();
						minhaBiblioteca.emprestaLivro(dataLimite, verificarData, user, book);
						
						System.out.println("\n\nO livro de codigo " + bookCodigo + " foi emprestado para o usuario de CPF " + userCPF + " com sucesso.");
						System.out.println("\n" + minhaBiblioteca.imprimeLivros() + "\n" + minhaBiblioteca.imprimeUsuarios() + "\n");
						
						
					}
					catch(NumberFormatException e) {
						System.out.println("Leitura invalida: CPF precisa ser um long e codigo do livro precisa ser int.\n");
					}
					catch(MinhasExcecoes | CopiaNaoDisponivelEx | LivroNaoCadastradoEx | UsuarioNaoCadastradoEx e) {
						System.out.println(e + "\n");
					}
					
					verificarData = true;
					
					break;
					
				case 4:
					userCPF = lerUmaString("Digite o CPF do usuario cadastrado: ");
					bookCodigo = lerUmaString("Digite o codigo do livro: ");
					try {
						user = minhaBiblioteca.getUsuario(Long.parseLong(userCPF));
						book = minhaBiblioteca.getLivro(Integer.parseInt(bookCodigo));
						
						minhaBiblioteca.devolveLivro(user, book);
						
						if (!user.getSituacaoMultaUsuario().equals("Regular")) {
							pagarMultaUsuario(user, "Este usuario tem uma multa por estar devolvendo o livro atrasado");
							// Se a multa nao for paga ela ficara pendente e so podera pedir um outro livro emprestado se ela for paga.
							
					
						}
						System.out.println("\n\nO livro de codigo " + bookCodigo + " que estava com o usuario de CPF " + userCPF + " foi devolvido com sucesso.");
						System.out.println("\n" + minhaBiblioteca.imprimeLivros() + "\n" + minhaBiblioteca.imprimeUsuarios() + "\n");
					
					}
					catch(NumberFormatException e) {
						System.out.println("Leitura invalida: CPF precisa ser um long e codigo do livro precisa ser int.\n");
					}
					catch(MinhasExcecoes | NenhumaCopiaEmprestadaEx | LivroNaoCadastradoEx | UsuarioNaoCadastradoEx e) {
						System.out.println(e + "\n");
					}
					
					break;
					
					
				case 5:
					if (nomeHistoricoUsuario == null || nomeHistoricoLivro == null) {
						System.out.println("O salvamento automatico aqui so será realiza se ambos arquivos de historico forem inicializados.");
						System.out.println("Portanto, como nao foi salvo aqui automaticamente, tente salvar o arquivo manualmente em \"manutencao\"\n.");
					}
					else {  // Vai tentar salvar automaticamente
						try {
							minhaBiblioteca.salvaArqLiv(nomeHistoricoLivro);
							minhaBiblioteca.salvaArqUsu(nomeHistoricoUsuario);
							System.out.println("Salvamento automatico em " + nomeHistoricoUsuario + " e " + nomeHistoricoLivro + " com sucesso.\n");
						}
						catch(IOException e) {
							System.out.println("Erro: " + e + "\n");
							System.out.println("O salvamento automatico falhou. Portanto, para salvar, tente manualmente em \"manutencao\".\n");
						}
					}
					
					
					ficar = false;
					break;
			
				default:
					ficar = false;
					break;
			}
			
			
			
		}
	}
	
	
	
	private void relatorio() {
		String menu = "\nAs opcoes de relatorio sao: \n"
				    + "1) listar todos os usuarios\n"
				    + "2) listar todos os livros\n"
				    + "3) imprimir dados de um usuario em especifico\n"
				    + "4) imprimir dados de um livro especifico\n"
				    + "5) voltar ao menu anterior\n";
		
		boolean ficar = true;
		while(ficar) {
			int opcao = lerOpcao(menu, 1, 5);
			switch(opcao) {
				case 1:
					System.out.println(minhaBiblioteca.imprimeUsuarios());
					break;
					
				case 2:
					System.out.println(minhaBiblioteca.imprimeLivros());
					break;
					
				case 3:
					Usuario user;
					String cpf;
					try {
						cpf = lerUmaString("Digite o cpf do usuario: ");
						user = minhaBiblioteca.getUsuario(Long.parseLong(cpf));
						System.out.println("\nOs dados são\n" + user);
					}
					
					catch(NumberFormatException e) {
						System.out.println("Leitura invalida: seu cpf precisa ser um long.\n");
					}
					catch(UsuarioNaoCadastradoEx e) {
						System.out.println(e + "\n");
					}
					
					break;
					
				case 4:
					Livro book;
					String codigo;
					try {
						codigo = lerUmaString("Digite o codigo do livro: ");
						book = minhaBiblioteca.getLivro(Integer.parseInt(codigo));
						System.out.println("\nOs dados são\n" + book);
					}
					
					catch(NumberFormatException e) {
						System.out.println("Leitura invalida: seu cpf precisa ser um long.\n");
					}
					catch(LivroNaoCadastradoEx e) {
						System.out.println(e + "\n");
					}
					
					break;
					
				default:
					ficar = false;
					break;
			}
			
			
		}
	}
	/* Os modulos do meu programa */
	/* Os modulos do meu programa */
	/* Os modulos do meu programa */
	
	
	
	
	
	
	
	
	/* Metodos auxiliares */
	private int lerOpcao(String menu, int primeiro, int ultimo) {
		String entrada;
		int numero;
		while(true) {
			try {
				System.out.println(menu);
				System.out.print("Digite sua opcao: ");
				entrada = input.nextLine();
				numero = Integer.parseInt(entrada);
				if (numero >= primeiro && numero <= ultimo)
					break;
				else
					System.out.println("Opcao invalida: Voce nao digitou uma opcao existente de " + primeiro + " ate " + ultimo + ". Tente novamente.\n");
				
			}
			catch (NumberFormatException e) {
				System.out.println("Leitura invalida: Voce nao digitou uma opcao que seja convertivel para um numero inteiro. Tente novamente.\n");
			}
			catch(NoSuchElementException e) {   // Caso de um possivel "ctrl + d" pelo usuario (isso me causou problemas...)
				encerrarForcado();
			}
			
		}
		
		return numero;
	}
	
	
	private GregorianCalendar lerDataLimite() {
		GregorianCalendar data = null;
		String dia, mes, ano;
		while(true) {
			dia = lerUmaString("Digite o dia de devolucao: ");
			mes = lerUmaString("Digite o mes de devolucao: ");
			ano = lerUmaString("Digite o ano de devolucao: ");
			try {
				data = FuncoesDataAuxiliares.converterData(dia, mes, ano);
				break;
			}
			catch(DataInvalidaException e) {
				System.out.println(e + " Tente novamente: ");
			}
			
		}
		return data;
	}
	
	
	private boolean pagarMultaUsuario(Usuario user, String mensagem) {
		boolean multaFoiPaga = false;
		String str;
		
		System.out.println(mensagem);
		while(true) {
			str = lerUmaString("Ele pagou a multa(S/N)? ");
			if (str.length() == 1) {
				if (str.charAt(0) == 'S' || str.charAt(0) == 's') {
					multaFoiPaga = true;
					user.setSituacaoMultaUsuario("Regular");
					break;
				}
				else if (str.charAt(0) == 'N' || str.charAt(0) == 'n')
					break;
				else
					System.out.println("Leitura invalida: deve ser respondido apenas S/N. Tente novamente.\n");
			}
			else 
				System.out.println("Leitura invalida: deve ser respondido apenas S/N. Tente novamente.\n");
		}
		return multaFoiPaga;
		
	}
	
	
	
	
	
	// Os metodos de salvamento estao desnecessariamente separados em dois metodos, daria para deixa-los em um unico metodo
	private boolean salvarHistoricoUsuario() {
		String str;
		boolean foiSalvo = false;
		boolean tentarNovamente = false;
		while(true) {
			try {	
				System.out.print("Digite o nome do arquivo que deseja: ");
				str = input.nextLine();
				if (str.length() == 0) {
					System.out.println("Leitura invalida: seu arquivo precisa ter pelo menos um caractere. Tente novamente.\n");
					continue;
				}
				else if (str.charAt(0) != 'u') {
					System.out.println("Leitura invalida: seu arquivo precisa ter como primeiro caractere o 'u'. Tente novamente:\n");
					continue;
				}
				
			}
			catch(NoSuchElementException e) {
				encerrarForcado();
				continue;
			}
		
			try {
				minhaBiblioteca.salvaArqUsu(str);
				nomeHistoricoUsuario = str;
				foiSalvo = true;
				break;
			}
			catch (IOException e) {   // Acho dificil que entre nesse catch(), mas esta aí um tratamento.
				tentarNovamente = false;
				System.out.println(e + "\n");
				while(true) {
					str = lerUmaString("Voce quer tentar novamente(S/N)? ");
					if (str.length() == 1) {
						if (str.charAt(0) == 'S' || str.charAt(0) == 's') {
							tentarNovamente = true;
							break;
						}
						else if (str.charAt(0) == 'N' || str.charAt(0) == 'n')
							break;
						else
							System.out.println("Leitura invalida: Voce nao digitou S ou N. Tente novamente.\n");
					}
					else
						System.out.println("Leitura invalida: sua resposta precisa ter apenas um caractere. Tente novamente.\n");
				}
				
				if (tentarNovamente)
					continue;
				else
					break;
			}
		}
		
		return foiSalvo;
		
	}
	
	
	private boolean salvarHistoricoLivro() {
		String str;
		boolean foiSalvo = false;
		boolean tentarNovamente = false;
		while(true) {
			try {	
				System.out.print("Digite o nome do arquivo que deseja: ");
				str = input.nextLine();
				if (str.length() == 0) {
					System.out.println("Leitura invalida: seu arquivo precisa ter pelo menos um caractere. Tente novamente.\n");
					continue;
				}
				else if (str.charAt(0) != 'l') {
					System.out.println("Leitura invalida: seu arquivo precisa ter como primeiro caractere o 'l'. Tente novamente:\n");
					continue;
				}
			}
			
			catch(NoSuchElementException e) {
				encerrarForcado();
				continue;
			}
				
			try {
				minhaBiblioteca.salvaArqLiv(str);
				nomeHistoricoLivro = str;
				foiSalvo = true;
				break;
			}
			catch (IOException e) {   // Acho dificil que entre nesse catch(), mas esta aí um tratamento.
				tentarNovamente = false;
				System.out.println("Nao foi possivel salvar o historico: \n");
				System.out.println(e + "\n");
				while(true) {
					str = lerUmaString("Voce quer tentar novamente(S/N)? ");
					if (str.length() == 1) {
						if (str.charAt(0) == 'S' || str.charAt(0) == 's') {
							tentarNovamente = true;
							break;
						}
						else if (str.charAt(0) == 'N' || str.charAt(0) == 'n')
							break;
						else
							System.out.println("Leitura invalida: Voce nao digitou S ou N. Tente novamente.\n");
					}
					else
						System.out.println("Leitura invalida: sua resposta precisa ter apenas um caractere. Tente novamente.\n");
				}
				
				if (tentarNovamente)
					continue;
				else
					break;
			}
		}
		
		return foiSalvo;
	}
	
	
	private void lerHistoricoUsuario() {  
		String str;
		
		try {
			str = lerUmaString("Digite o arquivo que deseja carregar o seu historico: ");
			if (str.charAt(0) != 'u') {
				System.out.println("Leitura invalida: seu arquivo precisa ter como primeiro caractere o 'u'. Tente novamente:\n");
				return;
			}
			
			minhaBiblioteca.leArqUsu(str);
			nomeHistoricoUsuario = str;
			System.out.println("Carregamento concluido!!!");
		}
			
		catch(ClassNotFoundException | IOException e) {
			System.out.println("Nao foi possivel carregar o historico: \n");
			System.out.println(e + "\n");
		}
		
		
	}
	
	private void lerHistoricoLivro() {  
		String str;
		
		try {
			str = lerUmaString("Digite o arquivo que deseja carregar o seu historico: ");
			if (str.charAt(0) != 'l') {
				System.out.println("Leitura invalida: seu arquivo precisa ter como primeiro caractere o 'l'. Tente novamente:\n");
				return;
			}
			minhaBiblioteca.leArqLiv(str);
			nomeHistoricoLivro = str;
			System.out.println("Carregamento concluido!!!");	
		}
			
		catch(ClassNotFoundException | IOException e) {
			System.out.println("Nao foi possivel carregar o historico: \n");
			System.out.println(e + "\n");
		}
	}
	
	private String lerUmaString(String mensagem) {
		String str = null;
		while(true) {
			try {
				System.out.print(mensagem);
				str = input.nextLine();
				if (str.length() == 0)
					System.out.println("Leitura invalida: precisa de ao menos um simbolo. Tente novamente:\n");
				else
					break;
			}
			catch (NoSuchElementException e) {
				encerrarForcado();
			}
			
		}
		return str;
		
	}
	
	
	private void encerrarForcado() {
		System.out.println("\n\nEntrada encerrada pelo usuário de maneira forçada.\nSendo assim, encerrando programa...");
		input.close();
		System.exit(0);
	}
	/* Metodos auxiliares */
	
}
