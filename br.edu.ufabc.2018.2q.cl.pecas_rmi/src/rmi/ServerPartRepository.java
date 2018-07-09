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

import classes.PartRepository;
import classes.PartRepositoryImpl;
import classes.QuitException;

public class ServerPartRepository {
	public static void main(String args[]) {
		//Criacao de variaveis de trabalho dos metodos
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		int valor=0;
		int num;
		String nome, text;
		Registry registry;
		String[] boundNames;

		try {
			//variaveis atuais do servidor: registry, repositorio e stub
			registry = LocateRegistry.getRegistry();
			num = registry.list().length;
			PartRepositoryImpl partRepos;
			PartRepository stub;
			
			//texto inicial explicativo sobre o funcionamento da interface
			System.out.println("Digite: 'commands' para visualisar a lista de poss�veis comandos permitidos ao usu�rio.");
			System.out.println("Caso j� os conhe�a, digite a primeira a��o que deseja realizar e aperte enter...");
			
			//loop principal para receber os comandos do usuario e mostrar informacoes
			while (comando != "quit") {
				//informacoes que sempre aparecerao ao usuario
				System.out.println();
				num = registry.list().length;
				System.out.println("Servidor(es) rodando: " + num);
				System.out.print("Comando: ");
				comando = entrada.nextLine();
				System.out.println();
				
				//switch-case dos comandos possiveis
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
				//-------------------------------------------------
				case "reload":
					if(num != 0) {
						boundNames = registry.list();
						int ok = 0;
						
						System.out.println("Reposit�rios encontrados");
						for (String name : boundNames) System.out.println(" - "+name);
						
						System.out.print("Diga o nome do reposit�rio desejado: ");
						nome = entrada.nextLine();
						for (String name : boundNames) if(name.equals(nome)) ok=1;
						if(ok != 0) {
							partRepos = new PartRepositoryImpl(nome);
							//Criamos o stub do objeto que sera registrado
							stub = (PartRepository)UnicastRemoteObject.exportObject(partRepos, 0);
							//Registra (binds) o stub no registry
							registry.rebind(nome, stub);
							System.out.println("Servidor-Reposit�rio "+ num +" reiniciado.");
						} else System.out.println("A��o inv�lida: Nome de Servidor incorreto!");
					} else System.out.println("N�o foram encontrados Reposit�rios ativos.");
					break;
				//-------------------------------------------------
				case "create":
					System.out.print("Quer realmente realizar esta a��o? (s/n)");
					text = entrada.nextLine();
					if(text.equals("s")){
						valor = num+1;
						//Crio o objeto servidor: criando 1 Repositorio de peca
						nome = "S"+String.valueOf(valor)+"_PartRepos";
						partRepos = new PartRepositoryImpl(nome);
						//Criamos o stub do objeto que sera registrado
						stub = (PartRepository)UnicastRemoteObject.exportObject(partRepos, 0);
						//Registra (binds) o stub no registry
						registry.bind(nome, stub);
						System.out.println("Servidor-Reposit�rio "+valor+" iniciado.");
					} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
					  else System.out.println("Comando inv�lido, opera��o cancelada!");
					break;
				//-------------------------------------------------
				case "off":
					if(num != 0) {
						int ok = 0;
						boundNames = registry.list();
						System.out.println("Reposit�rios encontrados:");
						for (String name : boundNames) System.out.println(" - "+name);
						System.out.print("Diga o nome do reposit�rio desejado: ");
						nome = entrada.nextLine();
						for (String name : boundNames) if(name.equals(nome)) ok=1;
						if(ok != 0) {
							//Deleta no registry
							registry.unbind(nome);
							System.out.println("Servidor-Reposit�rio "+valor+" desligado.");
						} else System.out.println("A��o inv�lida: Nome de Servidor incorreto!");					
					} else System.out.println("N�o foram encontrados Reposit�rios ativos.");
					break;
				//-------------------------------------------------
				case "quit":
					throw new QuitException();
				//-------------------------------------------------
				default:
					System.out.println("Este n�o � um comando v�lido!");
				}
			}
			
		} catch (AlreadyBoundException e) {
			System.err.println("J� est� rodando uma inst�ncia do servidor.");
		} catch (QuitException e) {
			System.out.println("Encerrada sua sess�o com sucesso!");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
	}
}
