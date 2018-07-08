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

		System.out.println("Digite: 'commands' para visualisar a lista de possíveis comandos permitidos ao usuário.");
		System.out.println("Caso já os conheça, digite a primeira ação que deseja realizar e aperte enter...");
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
						System.out.println("bind: conectar à um repositório;");
						System.out.println("quit: encerra sua sessão;");
					}
					else {
						System.out.println("bind: mudar de repositório;");
						System.out.println("listp: listar as peças do repositório atual;");
						System.out.println("getp: buscar uma peça por código e torna-la a peça atual;");
						System.out.println("showp: mostrar os atributos da peça atual;");
						System.out.println("clearlist: esvaziar a lista de subpeças atual;");
						System.out.println("addsubpart: adicionar à lista de subpeças atual n unidades da peça atual;");
						System.out.println("addp: adicionar uma peça ao repositório atual. *A lista de subpeças atual é usada como lista de subcomponentes diretos da nova peça;");
						System.out.println("quit: encerra sua sessão;");
					}
					break;
				//-------------------------------------------------
				case "bind":
					try {
						try {
							System.out.println("Repositório conectado: " + partRepos.getPartRepositoryNome());
						} catch (NullPointerException e) {
							System.out.println("Conectado a nenhum repositório.");
						}

						boundNames = registry.list();
						System.out.println("Repositórios encontrados");
						for (String name : boundNames) System.out.println(" - "+name);

						System.out.print("Diga o nome do repositório que quer se conectar: ");
						text = entrada.next();
						try {
							if(text.equals(partRepos.getPartRepositoryNome())) System.out.println("Você já está conectado a este repositório.");
							else {
								partRepos = (PartRepository) registry.lookup(text);
								System.out.println("Conectado à "+text);
							}
						} catch (NullPointerException e) {
							partRepos = (PartRepository) registry.lookup(text);
							System.out.println("Conectado à "+text);
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
					if(!iterator.hasNext()) System.out.println("Não há peças");
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
					System.out.print("Diga o código da peça que quer buscar: ");
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
					System.out.println("Este não é um comando válido!");
				}
			}
		} catch (RuntimeException e) {
			if(e.getMessage() == comando) System.out.println("Encerrada sua sessão com sucesso!");
			else System.err.println("Ocorreu um erro: " + e.toString());
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}
}	