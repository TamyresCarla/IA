package IA;

import java.util.*;

public class Node {

	// no eh interno (decisao)
	private String atributo;
	private HashMap<String, Aresta> arestas;
	private Node dad;
	private String valor_dad;

	private boolean visitado;

	public Node(){
		atributo = "";
		visitado = false;
		dad = null;
		valor_dad = "no one";
		arestas = new HashMap<String, Aresta>();
		Main.numNodes++;
	}
	
	public String getAtributoTeste() { return atributo; }

	public void setAtributoTeste(String atributo) { this.atributo = atributo; }

	//public String getClasse() { if (folha) return classe; else return null;}

	public void criaAresta(String valor, ArrayList<Registro> registros, Node seguinte, Node atual) {
		if(seguinte != null) {
			seguinte.dad = atual;
			seguinte.valor_dad = valor;
		}
		Aresta a = new Aresta(valor, registros, seguinte, atual);
		this.arestas.put(valor, a);
	}

	public Aresta getAresta(String valor) { return arestas.get(valor); }

	public Aresta setAresta(Aresta a) { return arestas.put(a.getValor(), a); }

	public HashMap<String, Aresta> getSetArestas() { return arestas; }

	public boolean isVisitado() {return visitado;}

	public void setVisitado(boolean visitado) {this.visitado = visitado;}

	public Node getDad() {
		return dad;
	}

	public void setDad(Node dad) {
		this.dad = dad;
	}

	public String getValor_dad() {
		return valor_dad;
	}

	public void setValor_dad(String valor_dad) {
		this.valor_dad = valor_dad;
	}
}