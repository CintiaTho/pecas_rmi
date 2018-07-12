/**
 * Interface Part a ser utilizada entre servidores.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.HashMap;

public interface Part extends java.io.Serializable {

	// Descricao da peca
	public String getPartDescricao() throws RemoteException;

	// Nome da peca
	public String getPartNome() throws RemoteException;

	// Repositorio registrado
	public PartRepository getPartRepository() throws RemoteException;

	// Codigo da peca
	public UID getPartUID() throws RemoteException;

	// Mapeamento da peca de subcomponentes por quantidade
	public HashMap<Part, Integer> getSubcomponentes() throws RemoteException;

	// Informa se a peca eh primitiva
	public boolean isPrimitiva() throws RemoteException;

	// Informa se ja esta registrado em um PartRepository
	public boolean isRegistrado() throws RemoteException;

	// Registra um PartRepository se nao estiver registrado
	public void setPartRepository(PartRepository partRepository) throws RemoteException, PartRegistradaException;
}
