package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UID;
import java.util.Iterator;
import java.util.Scanner;

import classes.Part;
import classes.PartRepository;

public class ClientCommand {
	Scanner entrada = new Scanner(System.in);
	String text;
	int num;
	UID id;

	public void command() {
		System.out.println("bind: mudar o reposit�rio atual;");
		System.out.println("listp: listar as pe�as do reposit�rio atual;");
		System.out.println("getp: buscar uma pe�a por c�digo e torna-la a pe�a atual;");
		System.out.println("showp: ;");
		System.out.println("clearlist: ;");
		System.out.println("addsubpart: ;");
		System.out.println("addp: ;");
		System.out.println("quit: encerra sua sess�o;");
		System.out.println("");
	}

	public void bind(PartRepository partRepos, Registry registry) {
		try {
			System.out.println("Voc� est� conectado agora: " + partRepos.getPartRepositoryNome());

			String[] boundNames = registry.list();
			System.out.println("Reposit�rios encontrados");
			for (String name : boundNames)
			{
				System.out.println(" - "+name);
			}

			System.out.print("Diga o nome do reposit�rio que quer se conectar: ");
			text = entrada.nextLine();
			partRepos = (PartRepository) registry.lookup(text);
			System.out.println("Conectado � "+text);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}

	public void listp(PartRepository partRepos) throws RemoteException {
		Iterator<Part> iterator = partRepos.getPartRepositoryParts().iterator();
		if(!iterator.hasNext()) System.out.println("N�o h� pe�as");
		else {
			for (iterator = partRepos.getPartRepositoryParts().iterator();iterator.hasNext();) {
				Part part = iterator.next();
				System.out.println("  "+part.getPartUID());
				System.out.println("	- "+part.getPartNome());
				System.out.println("	- "+part.getPartDescricao());
				System.out.println("");
			}
		}
	}

	public void getp(PartRepository partRepos) {
		System.out.print("Diga o c�digo da pe�a que quer buscar: ");
		num = entrada.nextLine().hashCode();
	}

	public void showp(PartRepository partRepos) {

	}

	public void clearlist(PartRepository partRepos) {

	}

	public void addsubpart(PartRepository partRepos) {

	}

	public void addp(PartRepository partRepos) {

	}

}
