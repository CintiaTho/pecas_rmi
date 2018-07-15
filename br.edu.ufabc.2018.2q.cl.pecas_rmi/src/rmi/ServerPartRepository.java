/**
 * ServerPart implemeta parte do Servidor do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import classes.PartRepository;
import classes.PartRepositoryImpl;
import classes.QuitException;

public class ServerPartRepository {
	public static void main(String args[]) {
		//Criacao de variaveis de trabalho dos metodos
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		int valor_server=0, valor_repos=0;
		int num, ok=0;
		String nome, text;
		Registry registry;
		//String[] boundNames = null;
		ArrayList<PartRepository> listRepos = new ArrayList<>();

		try {
			//cada servidor cuida apenas dos seus repositorios
			//variaveis atuais do servidor: registry, repositorio e stub
			registry = LocateRegistry.getRegistry();
			PartRepositoryImpl partRepos;
			PartRepository stub;
			
			//obtendo um numero identificador para o servidor - se houvesse persistencia (com bd) seria atribuido automaticamente
			num = 1;
			for(String name : registry.list()) {
				int n = Integer.parseInt(name.substring(1, name.indexOf("_")));
				if(num <= n) {
					num = n+1;
				}
			}
			valor_server = num;
			
			//texto inicial explicativo sobre o funcionamento da interface
			System.out.println("Digite: 'commands' para visualisar a lista de poss�veis comandos permitidos ao usu�rio.");
			System.out.println("Caso j� os conhe�a, digite a primeira a��o que deseja realizar e aperte enter...");
			
			//loop principal para receber os comandos do usuario e mostrar informacoes
			while (comando != "quit") {
				//informacoes que sempre aparecerao ao usuario
				System.out.println();
				System.out.println("Servidor: " + valor_server);
				System.out.println("Reposit�rio(s): " + listRepos.size());
				System.out.print("Comando: ");
				comando = entrada.nextLine();
				System.out.println();
				
				//switch-case dos comandos possiveis
				switch (comando){
				case "commands":
					if(listRepos.size() == 0) {
						System.out.println("N�o h� Reposit�rio(s) criados.");
						System.out.println("Gostaria de: ");
						System.out.println("create - Criar um novo Reposit�rio?");
						System.out.println("quit - Terminar esta sess�o");
					}
					else {
						System.out.println("J� existe(m): "+ listRepos.size() + " Reposit�rio(s) criados.");
						System.out.println("Gostaria de: ");
						System.out.println("create - Criar um Reposit�rio;");
						System.out.println("off - Desligar um Reposit�rio;");
						System.out.println("reload - listar e escolher reiniciar um Reposit�rio *isto apagar� os dados j� inseridos;");
						System.out.println("quit - Terminar esta sess�o.");
					}
					break;
				//-------------------------------------------------
				case "create":
					System.out.print("Quer realmente realizar esta a��o? (s/n) ");
					text = entrada.nextLine();
					if(text.equals("s")){
						if(listRepos.size() == 0) {
							//refazer a verifica��o para atribuir numero ao server
							//j� que s� agora estamos registrando um repos. no registry - nos mostrando para os outros;
							//poderia ter sido criado um repositorio com o mesmo numero de servidor durante este periodo
							num = 1;
							for(String name : registry.list()) {
								int n = Integer.parseInt(name.substring(1, name.indexOf("_")));
								if(num <= n) {
									num = n+1;
								}
							}
							valor_server = num;
						}
						//Crio o objeto Repositorio de peca
						valor_repos++;
						nome = "S"+String.valueOf(valor_server)+"_PartRepos"+String.valueOf(valor_repos);
						partRepos = new PartRepositoryImpl(nome);
						//Criamos o stub do objeto que sera registrado
						stub = (PartRepository)UnicastRemoteObject.exportObject(partRepos, 0);
						//Registra (binds) o stub no registry
						registry.bind(nome, stub);
						listRepos.add(stub);
						System.out.println("Reposit�rio "+nome+" iniciado.");
					} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
					  else System.out.println("Comando inv�lido, opera��o cancelada!");
					break;
				//-------------------------------------------------
				case "off":
					if(listRepos.size() != 0) {
						System.out.println("Reposit�rios encontrados:");
						for (PartRepository name : listRepos) System.out.println(" - "+name.getPartRepositoryNome());
						System.out.print("Diga o nome do reposit�rio desejado: ");
						nome = entrada.nextLine();
						
						ok = 0;
						int index=0;
						for(PartRepository name : listRepos) {
							if(name.getPartRepositoryNome().equals(nome)) {
								index = listRepos.indexOf(name);
								ok=1;
							}
						}
						
						if(ok != 0) {
							//Deleta no registry e na lista de repos.
							registry.unbind(nome);
							listRepos.remove(listRepos.get(index));
							System.out.println("Reposit�rio "+nome+" desligado.");
						} else System.out.println("A��o inv�lida: Nome de Servidor incorreto!");					
					} else System.out.println("N�o foram encontrados Reposit�rios ativos.");
					break;
				//-------------------------------------------------
				case "reload":
					if(listRepos.size() != 0) {
						System.out.println("Reposit�rios dispon�veis:");
						for (PartRepository name : listRepos) System.out.println(" - "+name.getPartRepositoryNome());

						System.out.print("Diga o nome do reposit�rio desejado: ");
						nome = entrada.nextLine();
						
						ok = 0;
						int index=0;
						for(PartRepository name : listRepos) {
							if(name.getPartRepositoryNome().equals(nome)) {
								index = listRepos.indexOf(name);
								ok=1;
							}
						}
						
						if(ok != 0) {
							partRepos = new PartRepositoryImpl(nome);
							//Criamos o stub do objeto que sera registrado
							stub = (PartRepository)UnicastRemoteObject.exportObject(partRepos, 0);
							//Re-registra (rebinds) o stub no registry
							registry.rebind(nome, stub);
							listRepos.set(index, stub);
							System.out.println("Reposit�rio "+ nome +" reiniciado.");
						} else System.out.println("A��o inv�lida: Nome de Reposit�rio incorreto!");
					} else System.out.println("N�o foram encontrados Reposit�rios ativos.");
					break;	
				//-------------------------------------------------
				case "quit":
					System.out.println("Seus dados ser�o perdidos e seus reposit�rios apagados e deslistados do Registry!"); 
					System.out.print("Deseja realmente terminar sua sess�o?(s/n) ");
					text = entrada.nextLine();
					if(text.equals("s")){
						for (PartRepository name : listRepos){
							//Deleta no registry
							registry.unbind(name.getPartRepositoryNome());
							System.out.println("Reposit�rio "+name.getPartRepositoryNome()+" desligado.");							
						}
						registry = null;
						listRepos.clear();
						partRepos = null;
						stub = null;
						throw new QuitException();
					}
					else if(text.equals("n")) System.out.println("Opera��o cancelada!");
					else System.out.println("Comando inv�lido, opera��o cancelada!");
				//-------------------------------------------------
				default:
					System.out.println("Este n�o � um comando v�lido!");
				}
			}
			
		} catch (AlreadyBoundException e) {
			System.err.println("J� est� rodando uma inst�ncia do servidor.");
		} catch (QuitException e) {
			System.out.println("Encerrada sua sess�o com sucesso!");
		} catch (ConnectException e) {
			System.err.println("Problemas com o Registry: " + e.toString());
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
	}
}
