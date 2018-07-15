/**
 * Classe que implementa PartRepository como PartRepositoryImpl.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class PartRepositoryImpl implements PartRepository {
	static final long serialVersionUID = 2306139568115548160L;
	private UID uid;
	private String nome;
	private HashSet<Part> parts;
	private HashMap<UID, Part> uid_part;

	public PartRepositoryImpl(String nome) {
		// PartRepositoryImpl - possui o nome, e o inventario esta vazio
		this.uid = new UID();
		this.nome = nome;
		this.parts = new HashSet<Part>();
		this.uid_part = new HashMap<UID, Part>();
	}

	// Busca Part por UID
	@Override
	public Part getPartPorUID(String text) {
		for (Entry<UID, Part> it : uid_part.entrySet()){
			UID id = it.getKey();
			if(id.toString().equalsIgnoreCase(text)) return it.getValue();
		}
		return null;
	}

	// Nome do repositorio
	@Override
	public String getPartRepositoryNome() {
		return this.nome;
	}

	// Mapeamento do repositorio de peca por quantidade
	@Override
	public HashSet<Part> getPartRepositoryParts() {
		return this.parts;
	}

	// Codigo do repositorio
	@Override
	public UID getPartRepositoryUID() {
		return this.uid;
	}

	// Registra Part em repositorio
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
