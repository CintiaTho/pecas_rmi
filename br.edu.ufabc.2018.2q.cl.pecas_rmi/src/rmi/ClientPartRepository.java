/**
 * ClientPart implemeta parte do Cliente do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;
import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import classes.Part;
import classes.PartImpl;
import classes.PartRepository;
import classes.QuitException;

public class ClientPartRepository {
	public static void main(String[] args) {
		//Criacao de variaveis de trabalho dos metodos
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		String text="";
		int num=0, ok=0;
		Registry registry;
		String[] boundNames;

		//variaveis atuais do cliente: peca, subpecas e repositorio
		Part peca = null;
		PartRepository partRepos = null;
		HashMap<Part, Integer> list_subpecas = new HashMap<Part, Integer>();
		
		//texto inicial explicativo sobre o funcionamento da interface
		System.out.println("Digite: 'commands' para visualisar a lista de possíveis comandos permitidos ao usuário.");
		System.out.println("Caso já os conheça, digite a primeira ação que deseja realizar e aperte enter...");
		
		try {
			//conexao com o registry e lista de servidores ativos nele
			registry = LocateRegistry.getRegistry(null);
			boundNames = registry.list();
			
			//loop principal para receber os comandos do usuario e mostrar informacoes
			while (comando != "quit") {
				try{
					//informacoes que sempre aparecerao ao usuario
					System.out.println();
					try {
						System.out.println("Serv-Repos conectado: " + partRepos.getPartRepositoryNome());
					} catch (NullPointerException e) {
						System.out.println("Conectado a nenhum servidor.");
					}
	
					System.out.print("Comando: ");
					comando = entrada.nextLine();
					System.out.println();
					
					//switch-case dos comandos possiveis
					switch (comando){
					case "commands":
						if(partRepos == null) {
							System.out.println("Gostaria de: ");
							System.out.println("bind: conectar à um repositório;");
							System.out.println("quit: encerra sua sessão;");
						}
						else {
							System.out.println("Gostaria de: ");
							System.out.println("bind: mudar de repositório;");
							System.out.println("listp: listar as peças do repositório atual;");
							
							System.out.println("getp: buscar uma peça por código e torna-la a peça atual;");
							System.out.println("showp: mostrar os atributos da peça atual;");
							
							System.out.println("showlsp: visualizar a lista de subpeças atual;");
							System.out.println("clearlsp: esvaziar/limpar a lista de subpeças atual;");
							System.out.println("addlsp: adicionar à lista de subpeças atual n unidades da peça atual;");
							
							System.out.println("addp: adicionar uma peça ao repositório atual (primária ou utilizando a sua lista de subpeças atual);");
							System.out.println("quit: encerra sua sessão;");
						}
						break;
					//-------------------------------------------------
					case "bind":
						if(partRepos != null) System.out.println("Repositório conectado: " + partRepos.getPartRepositoryNome());
						else System.out.println("Não está conectado à um repositório.");
	
						boundNames = registry.list();
						if(boundNames.length == 0) System.out.println("Não foram encontrados Repositórios disponíveis.");
						else{
							System.out.println("Repositórios encontrados:");
							for (String name : boundNames) System.out.println(" - "+name);
	
							try {
								System.out.print("Diga o nome do repositório que quer se conectar: ");
								text = entrada.nextLine();
								if(text.equals(partRepos.getPartRepositoryNome())) System.out.println("Você já está conectado a este repositório.");
								else {
									ok=0;
									for (String name : boundNames) if(name.equals(text)) ok=1;
									if(ok != 0) {
										partRepos = (PartRepository) registry.lookup(text);
										System.out.println("Conectado à "+text);
									} else System.out.println("Ação inválida: Nome de Servidor incorreto!");
								}
							} catch (NullPointerException e) {
								ok=0;
								for (String name : boundNames) if(name.equals(text)) ok=1;
								if(ok != 0) {
									partRepos = (PartRepository) registry.lookup(text);
									System.out.println("Conectado à "+text);
								} else System.out.println("Ação inválida: Nome de Servidor incorreto!");
							}
						}
						break;
					//-------------------------------------------------
					case "listp":
						if(partRepos != null) {
							Iterator<Part> iterator = partRepos.getPartRepositoryParts().iterator();
							if(!iterator.hasNext()) System.out.println("Não há peças neste repositório.");
							else {
								for (iterator = partRepos.getPartRepositoryParts().iterator(); iterator.hasNext();) {
									Part part = iterator.next();
									System.out.println("  ID: " + part.getPartUID());
									System.out.println("  - nome: " + part.getPartNome());
									System.out.println("  - desc: " + part.getPartDescricao());
									if(part.isPrimitiva()) System.out.println("  - primária: 0 subpeças;");
									else System.out.println("    - possui: "+ part.getSubcomponentes().size() +" subpeças.");
									System.out.println("");
								}
							}
						} else System.out.println("Ação inválida: Não está conectado à um repositório.");
						break;
					//-------------------------------------------------
					case "getp":
						if(partRepos != null) {
							if(partRepos.getPartRepositoryParts().isEmpty()) System.out.println("Não há peças neste repositório.");
							else{
								if(peca != null) System.out.println("Você possui uma peça selecionada para ser a atual.");
								else System.out.println("Você não possui uma peça selecionada para ser a atual.");
								
								System.out.println("Isto tornará a peça inserida em sua atual.");
								System.out.print("Deseja continuar o processo? (s/n) ");
								text = entrada.nextLine();

								if(text.equals("s")) {
									System.out.print("Diga o código da peça que quer buscar: ");
									//Procura apenas no atual repositorio (bind)
									text = entrada.nextLine();
									try{
										peca = partRepos.getPartPorUID(text);
										System.out.print("Peça: "+peca.getPartNome()+" é a sua atual!");
									}catch(NullPointerException e) {
										System.err.println("Código da peça inserido está errado ou não exite: " + e.toString());
									}
								}
								else if(text.equals("n")) System.out.println("Operação cancelada!");
								else System.out.println("Comando inválido, operação cancelada!");

							}
						} else System.out.println("Ação inválida: Não está conectado à um repositório.");
						break;
					//-------------------------------------------------
					case "showp":
						if(peca != null) {
							System.out.println("Sua peça atual:");
							System.out.println("ID: " + peca.getPartUID());
							System.out.println(" - Nome: " + peca.getPartNome());
							System.out.println(" - Descr: " + peca.getPartDescricao());
							System.out.println(" - Repos: " + peca.getPartRepository().getPartRepositoryNome());
							if(peca.isPrimitiva()) System.out.println(" - Primária - não possui subpeças.");
							else {
								System.out.println(" - Lista de subpeças:");
								HashMap<Part, Integer> sub = peca.getSubcomponentes();
								for (HashMap.Entry<Part, Integer> it : sub.entrySet()){  
									Part part = it.getKey();
									//Integer quant = it.getValue(); 
									System.out.println("   - id: " + part.getPartUID());
									System.out.println("   - nome: " + part.getPartNome());
									System.out.println("   - quant: " + it.getValue());
									System.out.println("   - descr: " + part.getPartDescricao());
									System.out.println("   - repos: " + part.getPartRepository().getPartRepositoryNome());
									System.out.println();
								}
							}
							System.out.println();
						} else System.out.println("Ação inválida: Ainda não selecionada uma peça para ser a atual.");
						break;
					//-------------------------------------------------
					case "showlsp":
						if(list_subpecas.isEmpty()) System.out.println("A sua lista está vazia.");
						else {
							System.out.println("Sua lista de subpeças atual:");
							for (HashMap.Entry<Part, Integer> it : list_subpecas.entrySet()){  
								Part part = it.getKey();
								Integer quant = it.getValue();
								System.out.println(" - id: " + part.getPartUID());
								System.out.println("   - nome: " + part.getPartNome());
								System.out.println("   - quant: " + quant);
								System.out.println("   - descr: " + part.getPartDescricao());
								System.out.println("   - repos: " + part.getPartRepository().getPartRepositoryNome());
								System.out.println();
							}
						}
						break;
					//-------------------------------------------------
					case "clearlsp":
						if(list_subpecas.isEmpty()) System.out.println("A sua lista já está vazia.");
						else {
							System.out.print("Quer realmente realizar esta ação? (s/n) ");
							text = entrada.nextLine();
							if(text.equals("s")){
								list_subpecas.clear();
								System.out.println("A lista foi limpa com sucesso!");
							}
							else if(text.equals("n")) System.out.println("Operação cancelada!");
							else System.out.println("Comando inválido, operação cancelada!");
						}
						break;
					//-------------------------------------------------
					case "addlsp":
						if(peca != null) {
							ok=0;
							Part i = null;
							int j = 0;
							for (HashMap.Entry<Part, Integer> it : list_subpecas.entrySet()){  
								Part part = it.getKey();
								if(peca.equals(part)) {
									i = part;
									j = it.getValue();
									ok = 1;
								}
							}
							if(ok==0) {
								System.out.println("Quantas unidades de "+ peca.getPartNome() + " quer adicionar na lista de subpeças atual?");
								System.out.print("*Se quiser cancelar a operação digite qualquer letra: ");
								num = Integer.parseInt(entrada.nextLine());
								if(num > 0) {
									list_subpecas.put(peca, num);
									System.out.println("Adição à lista de subpeças efetuada.");
								}
								else System.out.println("Ação inválida/cancelada: Quantidade nula ou negativa.");
							}
							else {
								if(i == null || j <= 0) System.out.println("Ação inválida/cancelada: Quantidade nula ou negativa.");
								else {
									System.out.println(peca.getPartNome() + " já possui " + j + " unidades na lista;");
									System.out.println("Digite um número para atualizar a quantidade na lista de subpeças atual;");
									System.out.print("*Se quiser cancelar a operação digite qualquer letra: ");
									num = Integer.parseInt(entrada.nextLine());
									if(num > 0) {
										list_subpecas.replace(i, j, num);
										System.out.println("Atualização à lista de subpeças efetuada.");
									}
									else System.out.println("Ação inválida/cancelada: Quantidade nula ou negativa.");
								}
							}
						} else System.out.println("Ação inválida: Ainda não selecionada uma peça para ser a atual.");
						break;
					//-------------------------------------------------
					case "addp":
						//Adicionar uma nova atraves da insercao de dados pelo usuario
						if(partRepos != null) {
							if(peca != null) System.out.println("Você possui uma peça selecionada para ser a atual.");
							else System.out.println("Você não possui uma peça selecionada para ser a atual.");
							
							System.out.println("Isto tornará a peça inserida em sua atual.");
							System.out.print("Deseja continuar o processo? (s/n) ");
							text = entrada.nextLine();

							if(text.equals("s")) {
								System.out.print("Qual o Nome da peça a ser inserida em " + partRepos.getPartRepositoryNome() + "? ");
								String nome = entrada.nextLine();
								System.out.print("Qual a Dercrição da peça? ");
								String descr = entrada.nextLine();

								if(list_subpecas.isEmpty()) {
									System.out.println("Você não tem peças na sua Lista de Subpeças");
									System.out.print("Deseja continuar o processo e criar uma peça primária? (s/n) ");
									text = entrada.nextLine();
									if(text.equals("s")){
										peca = new PartImpl(nome, descr, list_subpecas);
										partRepos.registraPart(peca);
										partRepos = (PartRepository) registry.lookup(partRepos.getPartRepositoryNome());
										System.out.println("A peça foi inserida com sucesso!");
									}
									else if(text.equals("n")) System.out.println("Operação cancelada!");
									else System.out.println("Comando inválido, operação cancelada!");
								}
								else{
									System.out.println("Você tem peças na sua Lista de Subpeças, deseja: ");
									System.out.println("- prim: Criar a peça como Primária - isto manterá sua lista intacta;");
									System.out.println("- comsub: Criar a peça com subpeças (da sua Lista de Subpeças atual) - *isto apagará sua lista;");
									System.out.println("- cancel: cancelar a ação;");
									text = entrada.nextLine();
									if(text.equals("comsub")){
										peca = new PartImpl(nome, descr, list_subpecas);
										partRepos.registraPart(peca);
										peca.setPartRepository(partRepos);
										partRepos = (PartRepository) registry.lookup(partRepos.getPartRepositoryNome());
										System.out.println("A peça foi inserida com sucesso!");
										list_subpecas.clear();
										System.out.println("A lista foi limpa com sucesso!");
									}
									else if(text.equals("prim")){
										peca = new PartImpl(nome, descr, null);
										partRepos.registraPart(peca);
										System.out.println("A peça foi inserida com sucesso!");
									}
									else if(text.equals("cancel")) System.out.println("Operação cancelada!");
									else System.out.println("Comando inválido, operação cancelada!");
								}
							} 
							else if(text.equals("n")) System.out.println("Operação cancelada!");
							else System.out.println("Comando inválido, operação cancelada!");

						} else System.out.println("Ação inválida: Não está conectado à um repositório.");
						break;
					//-------------------------------------------------
					case "quit":
						System.out.print("Deseja realmente terminar sua sessão?");
						System.out.print("Seus dados poderão ser perdidos! (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							throw new QuitException();
						}
						else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					//-------------------------------------------------
					default:
						System.out.println("Este não é um comando válido!");
					}	
				}catch (ConnectException e) {
					System.err.println("Ocorreu um erro no servidor, tente se conectar a outro repositório.");
					partRepos = null;
				}
			}
		} catch (QuitException e) {
			System.out.println("Encerrada sua sessão com sucesso!");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}
}	