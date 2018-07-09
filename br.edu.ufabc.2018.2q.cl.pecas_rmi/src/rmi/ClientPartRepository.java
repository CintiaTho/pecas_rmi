/**
 * ClientPart implemeta parte do Cliente do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import classes.Part;
import classes.PartRepository;
import classes.QuitException;

public class ClientPartRepository {
	public static void main(String[] args) {
		//Criacao das principais variaveis utilizadas pelo cliente
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		String text="";
		int num=0, ok=0;

		//variaveis atuais do cliente
		Part peca = null;
		PartRepository partRepos = null;
		HashMap<Part, Integer> list_subpecas = new HashMap<Part, Integer>();

		System.out.println("Digite: 'commands' para visualisar a lista de poss�veis comandos permitidos ao usu�rio.");
		System.out.println("Caso j� os conhe�a, digite a primeira a��o que deseja realizar e aperte enter...");
		try {
			Registry registry = LocateRegistry.getRegistry(null);
			String[] boundNames = registry.list();

			while (comando != "quit") {
				System.out.println();
				try {
					System.out.println("Servidor conectado: " + partRepos.getPartRepositoryNome());
				} catch (NullPointerException e) {
					System.out.println("Conectado a nenhum servidor.");
				}

				System.out.print("Comando: ");
				comando = entrada.nextLine();
				System.out.println();

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
						System.out.println("clearlist: esvaziar/limpar a lista de subpe�as atual;");
						System.out.println("addsubpart: adicionar � lista de subpe�as atual n unidades da pe�a atual;");
						System.out.println("addp: adicionar uma pe�a ao reposit�rio atual. *A lista de subpe�as atual � usada como lista de subcomponentes diretos da nova pe�a;");
						System.out.println("quit: encerra sua sess�o;");
					}
					break;
				//-------------------------------------------------
				case "bind":
					if(partRepos != null) System.out.println("Reposit�rio conectado: " + partRepos.getPartRepositoryNome());
					else System.out.println("N�o est� conectado � um reposit�rio.");

					boundNames = registry.list();
					System.out.println("Reposit�rios encontrados");
					for (String name : boundNames) System.out.println(" - "+name);

					try {
						System.out.print("Diga o nome do reposit�rio que quer se conectar: ");
						text = entrada.nextLine();
						if(text.equals(partRepos.getPartRepositoryNome())) System.out.println("Voc� j� est� conectado a este reposit�rio.");
						else {
							for (String name : boundNames) {
								if(name.equals(text)) ok=1;
							}
							if(ok != 0) {
								partRepos = (PartRepository) registry.lookup(text);
								System.out.println("Conectado � "+text);
							} else System.out.println("A��o inv�lida: Nome de Servidor incorreto!");
						}
					} catch (NullPointerException e) {
						for (String name : boundNames) {
							if(name.equals(text)) ok=1;
						}
						if(ok != 0) {
							partRepos = (PartRepository) registry.lookup(text);
							System.out.println("Conectado � "+text);
						} else System.out.println("A��o inv�lida: Nome de Servidor incorreto!");
					}
					break;
				//-------------------------------------------------
				case "listp":
					if(partRepos != null) {
						Iterator<Part> iterator = partRepos.getPartRepositoryParts().iterator();
						if(!iterator.hasNext()) System.out.println("N�o h� pe�as neste reposit�rio.");
						else {
							for (iterator = partRepos.getPartRepositoryParts().iterator();iterator.hasNext();) {
								Part part = iterator.next();
								System.out.println("  " + part.getPartUID());
								System.out.println("	- " + part.getPartNome());
								System.out.println("	- " + part.getPartDescricao());
								HashMap<Part, Integer> sub = part.getSubcomponentes();
								if(sub.isEmpty()) System.out.println("Pe�a Prim�ria - n�o possui subpe�as.");
								else System.out.println("Pe�a possui "+ sub.size()+" subpe�as.");
								System.out.println("");
							}
						}
					} else System.out.println("A��o inv�lida: N�o est� conectado � um reposit�rio.");
					break;
				//-------------------------------------------------
				case "getp":
					//Procurar em todos os repositorios, devolver a pe�a e seu repos., mantendo o bind original;
					if(partRepos != null) {
						System.out.print("Diga o c�digo da pe�a que quer buscar: ");
						UID id = new UID();
						peca = partRepos.getPartPorUID(id);
					} else System.out.println("A��o inv�lida: N�o est� conectado � um reposit�rio.");
					break;
				//-------------------------------------------------
				case "showp":
					if(peca != null) {
						System.out.println("ID: " + peca.getPartUID());
						System.out.println(" - Nome: " + peca.getPartNome());
						System.out.println(" - Descri��o: " + peca.getPartDescricao());
						System.out.println(" - Reposit�rio: " + peca.getPartRepository());

						HashMap<Part, Integer> sub = peca.getSubcomponentes();
						if(sub.isEmpty()) System.out.println("Pe�a Prim�ria - n�o possui subpe�as.");
						else {
							System.out.println(" - Lista de subpe�as:");
							Set<Part> chaves = sub.keySet();  
							for (Iterator<Part> it = chaves.iterator(); it.hasNext();){  
								Part chave = it.next();  
								if(chave != null){  
									System.out.println(); 
								}
							}
						}
					} else System.out.println("A��o inv�lida: Ainda n�o selecionada uma pe�a para ser a atual.");
					break;
				//-------------------------------------------------
				case "showlsp":
					if(list_subpecas.isEmpty()) System.out.println("A lista est� vazia.");
					else {
						Part i = peca;
						//for (Part i : list_subpecas) {
							System.out.println("ID: " + i.getPartUID());
							System.out.println(" - Nome: " + i.getPartNome());
							System.out.println(" - Descri��o: " + i.getPartDescricao());
							System.out.println(" - Reposit�rio: " + i.getPartRepository());
						//}
					}
					break;
				//-------------------------------------------------
				case "clearlist":
					if(list_subpecas.isEmpty()) System.out.println("A lista j� est� vazia.");
					else {
						System.out.print("Quer realmente realizar esta a��o? (s/n)");
						text = entrada.nextLine();
						if(text.equals("s")){
							list_subpecas.clear();
							System.out.println("A lista limpa!");
						}
						if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					}
					break;
				//-------------------------------------------------
				case "addsubpart":
					if(peca != null) {
						System.out.print("Quantas unidades de "+ peca.getPartNome() + "quer adicionar na lista de subpe�as atual?");
						num = entrada.nextInt();
						//list_subpecas.add(peca);
					} else System.out.println("A��o inv�lida: Ainda n�o selecionada uma pe�a para ser a atual.");
					break;
				//-------------------------------------------------
				case "addp":
					if(partRepos != null) {
						if(peca != null) {
							partRepos.registraPart(peca);
						} else System.out.println("A��o inv�lida: Ainda n�o selecionada uma pe�a para ser a atual.");
					} else System.out.println("A��o inv�lida: N�o est� conectado � um reposit�rio.");
					break;
				//-------------------------------------------------
				case "quit":
					throw new QuitException();
				//-------------------------------------------------
				default:
					System.out.println("Este n�o � um comando v�lido!");
				}
			}
		} catch (QuitException e) {
			System.out.println("Encerrada sua sess�o com sucesso!");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}
}	