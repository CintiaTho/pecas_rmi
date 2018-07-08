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
						System.out.println("showlsp: visualizar a lista de subpeças atual;");
						System.out.println("clearlist: esvaziar/limpar a lista de subpeças atual;");
						System.out.println("addsubpart: adicionar à lista de subpeças atual n unidades da peça atual;");
						System.out.println("addp: adicionar uma peça ao repositório atual. *A lista de subpeças atual é usada como lista de subcomponentes diretos da nova peça;");
						System.out.println("quit: encerra sua sessão;");
					}
					break;
				//-------------------------------------------------
				case "bind":
					try {
						if(!partRepos.equals(null)) System.out.println("Repositório conectado: " + partRepos.getPartRepositoryNome());
						else System.out.println("Conectado a nenhum repositório.");

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
					}
					break;
				//-------------------------------------------------
				case "listp":
					Iterator<Part> iterator = partRepos.getPartRepositoryParts().iterator();
					if(!iterator.hasNext()) System.out.println("Não há peças");
					else {
						for (iterator = partRepos.getPartRepositoryParts().iterator();iterator.hasNext();) {
							Part part = iterator.next();
							System.out.println("  " + part.getPartUID());
							System.out.println("	- " + part.getPartNome());
							System.out.println("	- " + part.getPartDescricao());
							HashMap<Part, Integer> sub = part.getSubcomponentes();
							if(sub.isEmpty()) System.out.println("Peça Primária - não possui subpeças.");
							else System.out.println("Peça possui "+ sub.size()+" subpeças.");
							System.out.println("");
						}
					}
					break;
				//-------------------------------------------------
				case "getp":
					System.out.print("Diga o código da peça que quer buscar: ");
					UID id = new UID();
					peca = partRepos.getPartPorUID(id);
					break;
				//-------------------------------------------------
				case "showp":
					System.out.println("ID: " + peca.getPartUID());
					System.out.println(" - Nome: " + peca.getPartNome());
					System.out.println(" - Descrição: " + peca.getPartDescricao());
					System.out.println(" - Repositório: " + peca.getPartRepository());
					
					HashMap<Part, Integer> sub = peca.getSubcomponentes();
					if(sub.isEmpty()) System.out.println("Peça Primária - não possui subpeças.");
					else {
						System.out.println(" - Lista de subpeças:");
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
					if(list_subpecas.isEmpty()) System.out.println("A lista já está vazia.");
					else {
						for (Part i : list_subpecas) {
							System.out.println("ID: " + i.getPartUID());
							System.out.println(" - Nome: " + i.getPartNome());
							System.out.println(" - Descrição: " + i.getPartDescricao());
							System.out.println(" - Repositório: " + i.getPartRepository());
						}
					}
					break;
				//-------------------------------------------------
				case "clearlist":
					if(list_subpecas.isEmpty()) System.out.println("A lista já está vazia.");
					else {
						System.out.print("Quer realmente realizar esta ação? (S/N)");
						text = entrada.next();
						if(text.equals("S")){
							list_subpecas.clear();
							System.out.println("A lista limpa!");
						}
						else if(text.equals("N")) break;
						else System.out.println("Comando inválido, operação cancelada!");
					}
					break;
				//-------------------------------------------------
				case "addsubpart":
					System.out.print("Quantas unidades de "+ peca.getPartNome() + "quer adicionar na lista de subpeças atual?");
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
					System.out.println("Este não é um comando válido!");
				}
			}
		} catch (QuitException e) {
			System.out.println("Encerrada sua sessão com sucesso!");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}
}	