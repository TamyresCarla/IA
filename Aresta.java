package IA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Aresta {
	private String valor;
	private ArrayList<Registro> registros;
	private Node otosan;
	private Node child;

	public Aresta() {
		valor = "";
	}

	public Aresta(String valor, ArrayList<Registro> registros, Node child, Node otosan) {
		this.valor = valor;
		this.registros = registros;
		this.child = child;
		this.otosan = otosan;
		Main.numArestas++;
	}

	public Node getChild() { return child; }

	public String getClasseMajor() {

		Registro regAtu;
		String classeAtu, majorClass = "";
		int countMaior = 0, count;

		HashMap<String, Integer> classes = new HashMap<>();
		Iterator<Registro> it = registros.iterator();
		while (it.hasNext()) {
			regAtu = it.next();
			classeAtu = regAtu.getClasse();
			if (!classes.containsKey(classeAtu)) classes.put(classeAtu, 0);
			classes.put(classeAtu, classes.get(classeAtu) + 1);
		}

		Iterator<String> classIt = classes.keySet().iterator();
		while (classIt.hasNext()) {
			classeAtu = classIt.next();
			count = classes.get(classeAtu);
			if (count > countMaior) {
				countMaior = count;
				majorClass = classeAtu;
			}
		}
		return majorClass;
	}

	public HashMap<String, Integer> getClasseNumbers() {
		
		Registro regAtu;
		String classeAtu, majorClass = "";
		int countMaior = 0, count;
		
		HashMap<String, Integer> classes = new HashMap<>();
		Iterator<Registro> it = registros.iterator();
		while (it.hasNext()) {
			regAtu = it.next();
			classeAtu = regAtu.getClasse();
			if (!classes.containsKey(classeAtu)) classes.put(classeAtu, 0);
			classes.put(classeAtu, classes.get(classeAtu) + 1);
		}
		return classes;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public ArrayList<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(ArrayList<Registro> registros) {
		this.registros = registros;
	}

	public Node getOtosan() {
		return otosan;
	}

	public void setOtosan(Node otosan) {
		this.otosan = otosan;
	}

	public void setChild(Node child) {
		this.child = child;
	}
}