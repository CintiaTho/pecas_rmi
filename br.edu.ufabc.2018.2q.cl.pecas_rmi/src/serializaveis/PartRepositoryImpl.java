/**
 * Classe que implementa PartRepository como PartRepositoryImpl.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package serializaveis;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.HashSet;

public class PartRepositoryImpl implements PartRepository {
	static final long serialVersionUID = 2306139568115548160L;
	private UID uid;
	private String nome;
	private HashSet<Part> parts;
	private HashMap<UID, Part> uid_part;

	public PartRepositoryImpl(String nome) {
		// PartRepositoryImpl só possui o nome e o inventário está vazio
		this.uid = new UID();
		this.nome = nome;
		this.parts = new HashSet<Part>();
		this.uid_part = new HashMap<UID, Part>();
	}

	// Busca Part por UID
	@Override
	public Part getPartPorUID(UID uid) {
		return this.uid_part.get(uid);
	}

	// Nome do repositório
	@Override
	public String getPartRepositoryNome() {
		return this.nome;
	}

	// Mapeamento do repositório de peça por quantidade
	@Override
	public HashSet<Part> getPartRepositoryParts() {
		return this.parts;
	}

	// Código do repositório
	@Override
	public UID getPartRepositoryUID() {
		return this.uid;
	}

	// Registra Part em repositório
	@Override
	public void registraPart(Part part) throws RemoteException, PartRegistradaException {
		if (part.isRegistrado()) {
			throw new PartRegistradaException();
		}
		if (this.parts.contains(part)) {
			throw new PartRegistradaException();
		}
		part.setPartRepository(this);
		this.parts.add(part);
		this.uid_part.put(part.getPartUID(), part);
	}
}
