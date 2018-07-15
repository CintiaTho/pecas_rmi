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
		System.out.println("Digite: 'commands' para visualisar a lista de poss�veis comandos permitidos ao usu�rio.");
		System.out.println("Caso j� os conhe�a, digite a primeira a��o que deseja realizar e aperte enter...");
		
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
							System.out.println("bind: conectar � um reposit�rio;");
							System.out.println("quit: encerra sua sess�o;");
						}
						else {
							System.out.println("Gostaria de: ");
							System.out.println("bind: mudar de reposit�rio;");
							System.out.println("listp: listar as pe�as do reposit�rio atual;");
							
							System.out.println("getp: buscar uma pe�a por c�digo e torna-la a pe�a atual;");
							System.out.println("showp: mostrar os atributos da pe�a atual;");
							
							System.out.println("showlsp: visualizar a lista de subpe�as atual;");
							System.out.println("clearlsp: esvaziar/limpar a lista de subpe�as atual;");
							System.out.println("addlsp: adicionar � lista de subpe�as atual n unidades da pe�a atual;");
							
							System.out.println("addp: adicionar uma pe�a ao reposit�rio atual (prim�ria ou utilizando a sua lista de subpe�as atual);");
							System.out.println("quit: encerra sua sess�o;");
						}
						break;
					//-------------------------------------------------
					case "bind":
						if(partRepos != null) System.out.println("Reposit�rio conectado: " + partRepos.getPartRepositoryNome());
						else System.out.println("N�o est� conectado � um reposit�rio.");
	
						boundNames = registry.list();
						if(boundNames.length == 0) System.out.println("N�o foram encontrados Reposit�rios dispon�veis.");
						else{
							System.out.println("Reposit�rios encontrados:");
							for (String name : boundNames) System.out.println(" - "+name);
	
							try {
								System.out.print("Diga o nome do reposit�rio que quer se conectar: ");
								text = entrada.nextLine();
								if(text.equals(partRepos.getPartRepositoryNome())) System.out.println("Voc� j� est� conectado a este reposit�rio.");
								else {
									ok=0;
									for (String name : boundNames) if(name.equals(text)) ok=1;
									if(ok != 0) {
										partRepos = (PartRepository) registry.lookup(text);
										System.out.println("Conectado � "+text);
									} else System.out.println("A��o inv�lida: Nome de Servidor incorreto!");
								}
							} catch (NullPointerException e) {
								ok=0;
								for (String name : boundNames) if(name.equals(text)) ok=1;
								if(ok != 0) {
									partRepos = (PartRepository) registry.lookup(text);
									System.out.println("Conectado � "+text);
								} else System.out.println("A��o inv�lida: Nome de Servidor incorreto!");
							}
						}
						break;
					//-------------------------------------------------
					case "listp":
						if(partRepos != null) {
							Iterator<Part> iterator = partRepos.getPartRepositoryParts().iterator();
							if(!iterator.hasNext()) System.out.println("N�o h� pe�as neste reposit�rio.");
							else {
								for (iterator = partRepos.getPartRepositoryParts().iterator(); iterator.hasNext();) {
									Part part = iterator.next();
									System.out.println("  ID: " + part.getPartUID());
									System.out.println("  - nome: " + part.getPartNome());
									System.out.println("  - desc: " + part.getPartDescricao());
									if(part.isPrimitiva()) System.out.println("  - prim�ria: 0 subpe�as;");
									else System.out.println("    - possui: "+ part.getSubcomponentes().size() +" subpe�as.");
									System.out.println("");
								}
							}
						} else System.out.println("A��o inv�lida: N�o est� conectado � um reposit�rio.");
						break;
					//-------------------------------------------------
					case "getp":
						if(partRepos != null) {
							if(partRepos.getPartRepositoryParts().isEmpty()) System.out.println("N�o h� pe�as neste reposit�rio.");
							else{
								if(peca != null) System.out.println("Voc� possui uma pe�a selecionada para ser a atual.");
								else System.out.println("Voc� n�o possui uma pe�a selecionada para ser a atual.");
								
								System.out.println("Isto tornar� a pe�a inserida em sua atual.");
								System.out.print("Deseja continuar o processo? (s/n) ");
								text = entrada.nextLine();

								if(text.equals("s")) {
									System.out.print("Diga o c�digo da pe�a que quer buscar: ");
									//Procura apenas no atual repositorio (bind)
									text = entrada.nextLine();
									try{
										peca = partRepos.getPartPorUID(text);
										System.out.print("Pe�a: "+peca.getPartNome()+" � a sua atual!");
									}catch(NullPointerException e) {
										System.err.println("C�digo da pe�a inserido est� errado ou n�o exite: " + e.toString());
									}
								}
								else if(text.equals("n")) System.out.println("Opera��o cancelada!");
								else System.out.println("Comando inv�lido, opera��o cancelada!");

							}
						} else System.out.println("A��o inv�lida: N�o est� conectado � um reposit�rio.");
						break;
					//-------------------------------------------------
					case "showp":
						if(peca != null) {
							System.out.println("Sua pe�a atual:");
							System.out.println("ID: " + peca.getPartUID());
							System.out.println(" - Nome: " + peca.getPartNome());
							System.out.println(" - Descr: " + peca.getPartDescricao());
							System.out.println(" - Repos: " + peca.getPartRepository().getPartRepositoryNome());
							if(peca.isPrimitiva()) System.out.println(" - Prim�ria - n�o possui subpe�as.");
							else {
								System.out.println(" - Lista de subpe�as:");
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
						} else System.out.println("A��o inv�lida: Ainda n�o selecionada uma pe�a para ser a atual.");
						break;
					//-------------------------------------------------
					case "showlsp":
						if(list_subpecas.isEmpty()) System.out.println("A sua lista est� vazia.");
						else {
							System.out.println("Sua lista de subpe�as atual:");
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
						if(list_subpecas.isEmpty()) System.out.println("A sua lista j� est� vazia.");
						else {
							System.out.print("Quer realmente realizar esta a��o? (s/n) ");
							text = entrada.nextLine();
							if(text.equals("s")){
								list_subpecas.clear();
								System.out.println("A lista foi limpa com sucesso!");
							}
							else if(text.equals("n")) System.out.println("Opera��o cancelada!");
							else System.out.println("Comando inv�lido, opera��o cancelada!");
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
								System.out.println("Quantas unidades de "+ peca.getPartNome() + " quer adicionar na lista de subpe�as atual?");
								System.out.print("*Se quiser cancelar a opera��o digite qualquer letra: ");
								num = Integer.parseInt(entrada.nextLine());
								if(num > 0) {
									list_subpecas.put(peca, num);
									System.out.println("Adi��o � lista de subpe�as efetuada.");
								}
								else System.out.println("A��o inv�lida/cancelada: Quantidade nula ou negativa.");
							}
							else {
								if(i == null || j <= 0) System.out.println("A��o inv�lida/cancelada: Quantidade nula ou negativa.");
								else {
									System.out.println(peca.getPartNome() + " j� possui " + j + " unidades na lista;");
									System.out.println("Digite um n�mero para atualizar a quantidade na lista de subpe�as atual;");
									System.out.print("*Se quiser cancelar a opera��o digite qualquer letra: ");
									num = Integer.parseInt(entrada.nextLine());
									if(num > 0) {
										list_subpecas.replace(i, j, num);
										System.out.println("Atualiza��o � lista de subpe�as efetuada.");
									}
									else System.out.println("A��o inv�lida/cancelada: Quantidade nula ou negativa.");
								}
							}
						} else System.out.println("A��o inv�lida: Ainda n�o selecionada uma pe�a para ser a atual.");
						break;
					//-------------------------------------------------
					case "addp":
						//Adicionar uma nova atraves da insercao de dados pelo usuario
						if(partRepos != null) {
							if(peca != null) System.out.println("Voc� possui uma pe�a selecionada para ser a atual.");
							else System.out.println("Voc� n�o possui uma pe�a selecionada para ser a atual.");
							
							System.out.println("Isto tornar� a pe�a inserida em sua atual.");
							System.out.print("Deseja continuar o processo? (s/n) ");
							text = entrada.nextLine();

							if(text.equals("s")) {
								System.out.print("Qual o Nome da pe�a a ser inserida em " + partRepos.getPartRepositoryNome() + "? ");
								String nome = entrada.nextLine();
								System.out.print("Qual a Dercri��o da pe�a? ");
								String descr = entrada.nextLine();

								if(list_subpecas.isEmpty()) {
									System.out.println("Voc� n�o tem pe�as na sua Lista de Subpe�as");
									System.out.print("Deseja continuar o processo e criar uma pe�a prim�ria? (s/n) ");
									text = entrada.nextLine();
									if(text.equals("s")){
										peca = new PartImpl(nome, descr, list_subpecas);
										partRepos.registraPart(peca);
										partRepos = (PartRepository) registry.lookup(partRepos.getPartRepositoryNome());
										System.out.println("A pe�a foi inserida com sucesso!");
									}
									else if(text.equals("n")) System.out.println("Opera��o cancelada!");
									else System.out.println("Comando inv�lido, opera��o cancelada!");
								}
								else{
									System.out.println("Voc� tem pe�as na sua Lista de Subpe�as, deseja: ");
									System.out.println("- prim: Criar a pe�a como Prim�ria - isto manter� sua lista intacta;");
									System.out.println("- comsub: Criar a pe�a com subpe�as (da sua Lista de Subpe�as atual) - *isto apagar� sua lista;");
									System.out.println("- cancel: cancelar a a��o;");
									text = entrada.nextLine();
									if(text.equals("comsub")){
										peca = new PartImpl(nome, descr, list_subpecas);
										partRepos.registraPart(peca);
										peca.setPartRepository(partRepos);
										partRepos = (PartRepository) registry.lookup(partRepos.getPartRepositoryNome());
										System.out.println("A pe�a foi inserida com sucesso!");
										list_subpecas.clear();
										System.out.println("A lista foi limpa com sucesso!");
									}
									else if(text.equals("prim")){
										peca = new PartImpl(nome, descr, null);
										partRepos.registraPart(peca);
										System.out.println("A pe�a foi inserida com sucesso!");
									}
									else if(text.equals("cancel")) System.out.println("Opera��o cancelada!");
									else System.out.println("Comando inv�lido, opera��o cancelada!");
								}
							} 
							else if(text.equals("n")) System.out.println("Opera��o cancelada!");
							else System.out.println("Comando inv�lido, opera��o cancelada!");

						} else System.out.println("A��o inv�lida: N�o est� conectado � um reposit�rio.");
						break;
					//-------------------------------------------------
					case "quit":
						System.out.print("Deseja realmente terminar sua sess�o?");
						System.out.print("Seus dados poder�o ser perdidos! (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							throw new QuitException();
						}
						else if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					//-------------------------------------------------
					default:
						System.out.println("Este n�o � um comando v�lido!");
					}	
				}catch (ConnectException e) {
					System.err.println("Ocorreu um erro no servidor, tente se conectar a outro reposit�rio.");
					partRepos = null;
				}
			}
		} catch (QuitException e) {
			System.out.println("Encerrada sua sess�o com sucesso!");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}
}	