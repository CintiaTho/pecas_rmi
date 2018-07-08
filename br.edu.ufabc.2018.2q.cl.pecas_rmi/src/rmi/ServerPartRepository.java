/**
 * ServerPart implemeta parte do Servidor do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;

import java.rmi.registry.Registry;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import classes.*;

public class ServerPartRepository {
	public static void main(String args[]) {
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		int valor=0;
		int num;
		String nome;
		Registry registry;
		String[] boundNames;

		try {
			registry = LocateRegistry.getRegistry();
			num = registry.list().length;
			PartRepositoryImpl partRepos;
			PartRepository stub;
			System.out.println("Digite: 'commands' para visualisar a lista de poss�veis comandos permitidos ao usu�rio.");
			System.out.println("Caso j� os conhe�a, digite a primeira a��o que deseja realizar e aperte enter...");
			while (comando != "quit") {
				num = registry.list().length;
				System.out.println("Servidor(es) rodando: " + num);
				System.out.print("Comando: ");
				comando = entrada.nextLine();
				System.out.println();
				switch (comando){
				case "commands":
					if(num == 0) {
						System.out.println("N�o h� Servidor(es) rodando.");
						System.out.println("Gostaria de: ");
						System.out.println("create - Criar um novo Servidor-Reposit�rio?");
						System.out.println("quit - Terminar esta sess�o");
					}
					else {
						System.out.println("J� existe(m): "+ num + " Servidor(es) rodando.");
						System.out.println("Gostaria de: ");
						System.out.println("reload - Reiniciar uma inst�ncia (Servidor-Reposit�rio)? *isto apagar� os dados j� inseridos;");
						System.out.println("create - Criar um novo Servidor-Reposit�rio?");
						System.out.println("off - Desligar um Servidor-Reposit�rio?");
						System.out.println("quit - Terminar esta sess�o");
					}
					break;

				case "reload":
					if(num != 0) {
						boundNames = registry.list();
						int ok = 0;
						
						System.out.println("Reposit�rios encontrados");
						for (String name : boundNames) System.out.println(" - "+name);
						
						System.out.print("Diga o nome do reposit�rio desejado: ");
						nome = entrada.nextLine();
						for (String name : boundNames) {
							if(name.equals(nome)) ok=1;
						}
						
						if(ok != 0) {
							partRepos = new PartRepositoryImpl(nome);
							//Criamos o stub do objeto que sera registrado
							stub = (PartRepository)UnicastRemoteObject.exportObject(partRepos, 0);
							//Registra (binds) o stub no registry
							registry.rebind(nome, stub);
							System.out.println("Servidor-Reposit�rio "+valor+" reiniciado.");
						}
						else System.out.println("Nome de Servidor incorreto!");
					}
					break;

				case "create":
					valor = num+1;
					//Crio o objeto servidor: criando 1 Repositorio de peca
					nome = "S"+String.valueOf(valor)+"_PartRepos";
					partRepos = new PartRepositoryImpl(nome);
					//Criamos o stub do objeto que sera registrado
					stub = (PartRepository)UnicastRemoteObject.exportObject(partRepos, 0);
					//Registra (binds) o stub no registry
					registry.bind(nome, stub);
					System.out.println("Servidor-Reposit�rio "+valor+" iniciado.");
					break;

				case "off":
					if(num != 0) {
						int ok = 0;
						boundNames = registry.list();
						System.out.println("Reposit�rios encontrados:");
						for (String name : boundNames) System.out.println(" - "+name);
						System.out.print("Diga o nome do reposit�rio desejado: ");
						nome = entrada.nextLine();
						for (String name : boundNames) {
							if(name.equals(nome)) ok=1;
						}
						if(ok != 0) {
							//Deleta no registry
							registry.unbind(nome);
							System.out.println("Servidor-Reposit�rio "+valor+" desligado.");
						}
						else System.out.println("Nome de Servidor incorreto!");					
					}
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
		}catch (AlreadyBoundException e) {
			System.err.println("J� est� rodando uma inst�ncia do servidor.");
		}catch (Exception e) {
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
	}
}
