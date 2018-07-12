/**
 * Interface PartRepository a ser utilizada entre servidores.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.HashSet;

public interface PartRepository extends Remote{

	// Busca Part por UID
	public Part getPartPorUID(String text) throws RemoteException;

	// Nome do repositorio
	public String getPartRepositoryNome() throws RemoteException;

	// Conjunto de pecas do repositorio
	public HashSet<Part> getPartRepositoryParts() throws RemoteException;

	// Codigo do repositorio
	public UID getPartRepositoryUID() throws RemoteException;

	// Registra Part em repositorio
	public void registraPart(Part part) throws RemoteException, PartRegistradaException;
}