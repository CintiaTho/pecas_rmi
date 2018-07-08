/**
 * ServerPart implemeta parte do Servidor do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import serializaveis.*;

public class ServerPartRepository {
	public static void main(String args[]) {
		try {
			//Crio o objeto servidor: criando 3 Repositórios de peça
			PartRepositoryImpl partRepos1 = new PartRepositoryImpl("partRepos1");
			PartRepositoryImpl partRepos2 = new PartRepositoryImpl("partRepos2");
			PartRepositoryImpl partRepos3 = new PartRepositoryImpl("partRepos3");
			//Criamos o stub do objeto que sera registrado
			PartRepository stub1 = (PartRepository)UnicastRemoteObject.exportObject(partRepos1, 0);
			PartRepository stub2 = (PartRepository)UnicastRemoteObject.exportObject(partRepos2, 0);
			PartRepository stub3 = (PartRepository)UnicastRemoteObject.exportObject(partRepos3, 0);
			//Registra (binds) o stub no registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("partRepos1", stub1);
			registry.bind("partRepos2", stub2);
			registry.bind("partRepos3", stub3);
			System.out.println("Servidor iniciado.");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
  }
}
