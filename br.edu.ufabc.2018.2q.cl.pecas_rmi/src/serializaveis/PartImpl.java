/**
 * Classe que implementa Part como PartImpl.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package serializaveis;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.HashMap;

public class PartImpl implements Part {

	static final long serialVersionUID = 2306145370616365061L;
	private UID uid;
	private String nome;
	private String descricao;
	private HashMap<Part, Integer> subPartQuant;
	private PartRepository repositorio;

	public PartImpl(String nome, String descricao, HashMap<Part, Integer> subPartQuant)
			throws QuantidadeInvalidaException, NullPointerException, RemoteException {
		this.uid = new UID();
		this.nome = nome;
		this.descricao = descricao;
		this.subPartQuant = new HashMap<Part, Integer>();
		// Inicialmente cria a peça sem atrelar a um repositório
		this.repositorio = null;

		// Efetua triagem dos subcomponentes
		HashMap<Part, Integer> subcomponentes = new HashMap<Part, Integer>();
		// Popula subPartQuant validando as entradas do parâmetro
		for (HashMap.Entry<Part, Integer> entrada : subPartQuant.entrySet()) {
			Part part = entrada.getKey();
			if (part == null) {
				throw new NullPointerException();
			}
			Integer quant = entrada.getValue();
			if (quant == null) {
				throw new NullPointerException();
			}
			if (quant <= 0) {
				throw new QuantidadeInvalidaException();
			}
			// Part de subcomponente já precisa estar registrado em repositório
			if (!part.isRegistrado()) {
				throw new PartNaoRegistradaException();
			}
			subcomponentes.put(part, quant);
		}
		this.subPartQuant = subcomponentes;
	}

	// Descrição da peça
	@Override
	public String getPartDescricao() {
		return this.descricao;
	}

	// Nome da peça
	@Override
	public String getPartNome() {
		return this.nome;
	}

	// Repositório registrado
	@Override
	public PartRepository getPartRepository() {
		return this.repositorio;
	}

	// Código da peça
	@Override
	public UID getPartUID() {
		return this.uid;
	}

	// Mapeamento da peça de subcomponentes por quantidade
	@Override
	public HashMap<Part, Integer> getSubcomponentes() {
		return this.subPartQuant;
	}

	// Informa se a peça é primitiva
	@Override
	public boolean isPrimitiva() {
		return this.subPartQuant.isEmpty();
	}

	// Informa se já está registrado em um PartRepository
	@Override
	public boolean isRegistrado() {
		return this.repositorio != null;
	}

	// Registra um PartRepository se não estiver registrado
	@Override
	public void setPartRepository(PartRepository partRepository) throws PartRegistradaException {
		if (this.isRegistrado()) {
			throw new PartRegistradaException();
		}
		this.repositorio = partRepository;
	}

}