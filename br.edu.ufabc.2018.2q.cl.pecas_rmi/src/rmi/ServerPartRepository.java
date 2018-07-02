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
			//Crio o objeto servidor
			PartRepositoryImpl partRepos = new PartRepositoryImpl("partRepos");
			//Criamos o stub do objeto que sera registrado
			PartRepository stub = (PartRepository)UnicastRemoteObject.exportObject(partRepos, 0);
			//Registra (binds) o stub no registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("partRepos", stub);
			System.out.println("Servidor iniciado.");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
  }
}
