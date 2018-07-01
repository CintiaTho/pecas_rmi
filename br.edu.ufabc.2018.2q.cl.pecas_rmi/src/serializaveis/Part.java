/**
 * Interface Part a ser utilizada entre servidores.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 *
 */

package serializaveis;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.HashMap;

public interface Part extends java.io.Serializable {

	// Descrição da peça
	public String getPartDescricao() throws RemoteException;

	// Nome da peça
	public String getPartNome() throws RemoteException;

	// Repositório registrado
	public PartRepository getPartRepository() throws RemoteException;

	// Código da peça
	public UID getPartUID() throws RemoteException;

	// Mapeamento da peça de subcomponentes por quantidade
	public HashMap<Part, Integer> getSubcomponentes() throws RemoteException;

	// Informa se a peça é primitiva
	public boolean isPrimitiva() throws RemoteException;

	// Informa se já está registrado em um PartRepository
	public boolean isRegistrado() throws RemoteException;

	// Registra um PartRepository se não estiver registrado
	public void setPartRepository(PartRepository partRepository) throws RemoteException, PartRegistradaException;
}
