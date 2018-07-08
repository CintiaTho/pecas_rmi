/**
 * ClientPart implemeta parte do Cliente do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;
import java.io.DataInput;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
		int num=0;

		//variaveis atuais do cliente
		Part peca = null;
		PartRepository partRepos = null;
		List<Part> list_subpecas = null;

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
				comando = entrada.next();
				System.out.println();

				switch (comando){
				case "commands":
					if(partRepos.equals(null)) {
						System.out.println("bind: conectar � um reposit�rio;");
						System.out.println("quit: encerra sua sess�o;");
					}
					else {
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
					try {
						if(!partRepos.equals(null)) System.out.println("Reposit�rio conectado: " + partRepos.getPartRepositoryNome());
						else System.out.println("Conectado a nenhum reposit�rio.");

						boundNames = registry.list();
						System.out.println("Reposit�rios encontrados");
						for (String name : boundNames) System.out.println(" - "+name);

						System.out.print("Diga o nome do reposit�rio que quer se conectar: ");
						text = entrada.next();
						try {
							if(text.equals(partRepos.getPartRepositoryNome())) System.out.println("Voc� j� est� conectado a este reposit�rio.");
							else {
								partRepos = (PartRepository) registry.lookup(text);
								System.out.println("Conectado � "+text);
							}
						} catch (NullPointerException e) {
							partRepos = (PartRepository) registry.lookup(text);
							System.out.println("Conectado � "+text);
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				//-------------------------------------------------
				case "listp":
					Iterator<Part> iterator = partRepos.getPartRepositoryParts().iterator();
					if(!iterator.hasNext()) System.out.println("N�o h� pe�as");
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
					break;
				//-------------------------------------------------
				case "getp":
					System.out.print("Diga o c�digo da pe�a que quer buscar: ");
					UID id = new UID();
					peca = partRepos.getPartPorUID(id);
					break;
				//-------------------------------------------------
				case "showp":
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
					break;
				//-------------------------------------------------
				case "showlsp":
					if(list_subpecas.isEmpty()) System.out.println("A lista j� est� vazia.");
					else {
						for (Part i : list_subpecas) {
							System.out.println("ID: " + i.getPartUID());
							System.out.println(" - Nome: " + i.getPartNome());
							System.out.println(" - Descri��o: " + i.getPartDescricao());
							System.out.println(" - Reposit�rio: " + i.getPartRepository());
						}
					}
					break;
				//-------------------------------------------------
				case "clearlist":
					if(list_subpecas.isEmpty()) System.out.println("A lista j� est� vazia.");
					else {
						System.out.print("Quer realmente realizar esta a��o? (S/N)");
						text = entrada.next();
						if(text.equals("S")){
							list_subpecas.clear();
							System.out.println("A lista limpa!");
						}
						else if(text.equals("N")) break;
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					}
					break;
				//-------------------------------------------------
				case "addsubpart":
					System.out.print("Quantas unidades de "+ peca.getPartNome() + "quer adicionar na lista de subpe�as atual?");
					num = entrada.nextInt();
					list_subpecas.add(peca);
					break;
				//-------------------------------------------------
				case "addp":
					partRepos.registraPart(peca);
					
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