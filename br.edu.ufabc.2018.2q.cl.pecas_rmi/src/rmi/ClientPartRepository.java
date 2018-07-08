/**
 * ClientPart implemeta parte do Cliente do Sistema Distribuido.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package rmi;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import classes.*;

public class ClientPartRepository {
	public static void main(String[] args) {
		try {
			
			// AQUI FAREI UM MÉTODO PARA ENCONTRAR OS REPOSITORIOS DISPONIVEIS
			// Localiza o registry. Eh possivel usar endereço/IP porta
			Registry registry = LocateRegistry.getRegistry(null);
			// Consulta o registry e obtem o stub para o objeto remoto
			PartRepository partRepos = (PartRepository) registry.lookup("partRepos1");
			// A partir deste momento, chamadas ao PartRepository podem ser
			// feitas como qualquer chamada a metodos
			
			ClientCommand cc = new ClientCommand();
			Scanner entrada = new Scanner(System.in);
			String comando = "";
			System.out.println("Digite: 'Commands' para visualisar a lista de possíveis comandos permitidos ao usuário.");
			System.out.println("Caso já os conheça, digite a primeira ação que deseja realizar e aperte enter...");

			while (comando != "quit") {
				comando = entrada.next();
				switch (comando){
					case "Commands":
						cc.command();
						break;
					case "bind":
						cc.bind();
						break;
					case "listp":
						cc.listp();
						break;
					case "getp":
						cc.getp();
						break;
					case "showp":
						cc.showp();
						break;
					case "clearlist":
						cc.clearlist();
						break;
					case "addsubpart":
						cc.addsubpart();
						break;
					case "addp":
						cc.addp();
						break;
					default:
						System.out.println("Este não é um comando válido!");
				}
			}

		} catch (Exception e) {
			System.err.println("Ocorreu um erro no cliente: " + e.toString());
		}
	}	
}

/*Numero num1 = new NumeroImpl(3);
Numero num2 = new NumeroImpl(4);
//Aqui sÃ£o feitas diversas chamadas remotas
Numero soma = calc.soma(num1, num2);
Numero sub = calc.subtrai(num1, num2);
Numero mult = calc.multiplica(num1, num2);
Numero div = calc.divide(num1, num2);
System.out.println("Resultados obtidos do servidor:" +
                   "\n\t+:" + soma.getValor() +
                   "\n\t-:" + sub.getValor()  +
                   "\n\t*:" + mult.getValor() +
                   "\n\t/:" + div.getValor());

try {
    calc.divide(new NumeroImpl(1), new NumeroImpl(0));
} catch (DivisaoPorZeroException e) {
    System.out.println(
      "Tentou dividir por zero! Esta Ã© uma exceÃ§Ã£o remota.");
}*/