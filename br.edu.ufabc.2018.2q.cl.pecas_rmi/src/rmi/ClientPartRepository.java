/**
 * ClientPart implemeta parte do Cliente do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import classes.Part;
import classes.PartRepository;

public class ClientPartRepository {
	public static void main(String[] args) {
		//Criacao das principais variaveis utilizadas pelo cliente
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		String text;

		//variaveis atuais do cliente
		Part peca = null;
		PartRepository partRepos = null;
		List<Part> list_subpecas;

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
						System.out.println("clearlist: esvaziar a lista de subpe�as atual;");
						System.out.println("addsubpart: adicionar � lista de subpe�as atual n unidades da pe�a atual;");
						System.out.println("addp: adicionar uma pe�a ao reposit�rio atual. *A lista de subpe�as atual � usada como lista de subcomponentes diretos da nova pe�a;");
						System.out.println("quit: encerra sua sess�o;");
					}
					break;
				//-------------------------------------------------
				case "bind":
					try {
						try {
							System.out.println("Reposit�rio conectado: " + partRepos.getPartRepositoryNome());
						} catch (NullPointerException e) {
							System.out.println("Conectado a nenhum reposit�rio.");
						}

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
					} catch (NotBoundException e) {
						System.err.println("Nome incorreto!");
					}
					break;
				//-------------------------------------------------
				case "listp":
					Iterator<Part> iterator = partRepos.getPartRepositoryParts().iterator();
					if(!iterator.hasNext()) System.out.println("N�o h� pe�as");
					else {
						for (iterator = partRepos.getPartRepositoryParts().iterator();iterator.hasNext();) {
							Part part = iterator.next();
							System.out.println("  "+part.getPartUID());
							System.out.println("	- "+part.getPartNome());
							System.out.println("	- "+part.getPartDescricao());
							System.out.println("");
						}
					}
					break;
				//-------------------------------------------------
				case "getp":
					System.out.print("Diga o c�digo da pe�a que quer buscar: ");
					int num = entrada.nextLine().hashCode();
					break;
				//-------------------------------------------------
				case "showp":
					
					break;
				//-------------------------------------------------
				case "clearlist":
					
					break;
				//-------------------------------------------------
				case "addsubpart":
					
					break;
				//-------------------------------------------------
				case "addp":
					
					break;
				//-------------------------------------------------
				case "quit":
					throw new RuntimeException(comando);
				//-------------------------------------------------
				default:
					System.out.println("Este n�o � um comando v�lido!");
				}
			}
		} catch (RuntimeException e) {
			if(e.getMessage() == comando) System.out.println("Encerrada sua sess�o com sucesso!");
			else System.err.println("Ocorreu um erro: " + e.toString());
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}
}	