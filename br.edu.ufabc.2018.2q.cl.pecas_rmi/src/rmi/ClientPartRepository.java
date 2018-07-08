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
import java.util.List;
import java.util.Scanner;

import classes.Part;
import classes.PartRepository;

public class ClientPartRepository {
	public static void main(String[] args) {
		//Criacao das principais variaveis utilizadas pelo cliente
		ClientCommand cc = new ClientCommand();
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		
		//variaveis atuais do cliente
		UID peca_atual = null;
		PartRepository partRepos = null;
		List<Part> subpeca_atual;
		
		System.out.println("Digite: 'commands' para visualisar a lista de poss�veis comandos permitidos ao usu�rio.");
		System.out.println("Caso j� os conhe�a, digite a primeira a��o que deseja realizar e aperte enter...");

		try {	
			// M�TODO PARA ENCONTRAR OS REPOSITORIOS DISPONIVEIS
			// Localiza o registry. Eh possivel usar endere�o/IP porta
			Registry registry = LocateRegistry.getRegistry(null);
			String[] boundNames = registry.list();

			// Consulta o registry e obtem o stub para o objeto remoto
			partRepos = (PartRepository) registry.lookup("partRepos1");
			System.out.println("Conectado � partRepos1");
			// A partir deste momento, chamadas ao�PartRepository podem ser
			// feitas como qualquer chamada a metodos

			while (comando != "quit") {
				comando = entrada.next();
				switch (comando){
				case "commands":
					cc.command();
					break;
				case "bind":
					cc.bind(partRepos, registry);
					break;
				case "listp":
					cc.listp(partRepos);
					break;
				case "getp":
					cc.getp(partRepos);
					break;
				case "showp":
					cc.showp(partRepos);
					break;
				case "clearlist":
					cc.clearlist(partRepos);
					break;
				case "addsubpart":
					cc.addsubpart(partRepos);
					break;
				case "addp":
					cc.addp(partRepos);
					break;
				case "quit":
					throw new RuntimeException(comando);
				default:
					System.out.println("Este n�o � um comando v�lido!");
				}
			}

		}catch (RuntimeException e) {
			if(e.getMessage() == comando) System.out.println("Encerrada sua sess�o com sucesso!");
			else System.err.println("Ocorreu um erro: " + e.toString());
		}catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}
}	