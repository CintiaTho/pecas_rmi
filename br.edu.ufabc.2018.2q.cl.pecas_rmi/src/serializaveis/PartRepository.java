/**
 * Interface PartRepository a ser utilizada entre servidores.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 *
 */

package serializaveis;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.HashSet;

public interface PartRepository extends java.io.Serializable {

	// Busca Part por UID
	public Part getPartPorUID(UID uid) throws RemoteException;

	// Nome do repositório
	public String getPartRepositoryNome() throws RemoteException;

	// Conjunto de peças do repositório
	public HashSet<Part> getPartRepositoryParts() throws RemoteException;

	// Código do repositório
	public UID getPartRepositoryUID() throws RemoteException;

	// Registra Part em repositório
	public void registraPart(Part part) throws RemoteException, PartRegistradaException;
}