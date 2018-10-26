package IA;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Apoda {

	ArrayList<Registro> test;
	ArrayList<Registro> validation;
	ArrayList<Registro> training;

    public Apoda() {
    	//Divide os três conjuntos: teste, validação e treinamento
		Main.kFold(3, true);
		test = new ArrayList<>();
		validation = new ArrayList<>();
		training = new ArrayList<>();

		test.addAll(Main.testRegs);
		validation.addAll(Main.foldedExamples.get(0));
		training.addAll(Main.foldedExamples.get(1));

    }
    
    //Método que cria a árvore com o conjunto de treinamento atribuído na construção do objeto
    public Node criaArvore(ID3 id3) {
		Node root = id3.generateTree(training, Main.atributos);
		return root;
	}

    //Método para testar os exemplos do conjunto de teste na nova árvore
    public double treinamentoArvore(Node root, int sizeTree) {
    	boolean classificaResult = false;
    	double countErros = 0, n = training.size();
		System.out.println("MEDIDAS COM TREINAMENTO");
    	for(int i = 1; i <= 50; i++) {
	    	for (int j = 0; j < training.size(); j++) {
	    		classificaResult = Main.classifica(root, training.get(j), i);
	    		if (!classificaResult) countErros++;
	    	}
	    	double aux = 1 - (countErros / n);
	    	System.out.println(i + " " + aux);
	    	countErros = 0;
    	}
    	return 1 - (countErros / n);
    }

    //Método para testar os exemplos do conjunto de teste na nova árvore
	public double testArvore(Node root) {
    	boolean classificaResult = false;
    	double countErros = 0, n = test.size();
		for (int j = 0; j < test.size(); j++) {
			classificaResult = Main.classifica(root, test.get(j), Integer.MAX_VALUE);
			if (!classificaResult) countErros++;
		}
		return 1 - (countErros / n);
	}

	//Método para testar os exemplos do conjunto de teste na nova árvore
    public double testArvoreItera(Node root, int sizeTree) {
    	boolean classificaResult = false;
    	double countErros = 0, n = test.size();
		System.out.println("MEDIDAS COM TESTE");
    	for(int i = 1; i <= 100; i++) {
	    	for (int j = 0; j < test.size(); j++) {
	    		classificaResult = Main.classifica(root, test.get(j), i);
	    		if (!classificaResult) countErros++;
	    	}
	    	double aux = 1 - (countErros / n);
	    	System.out.println(i + " " + aux);
	    	countErros = 0;
    	}
    	return 1 - (countErros / n);
    }

	//Método para testar os exemplos do conjunto de teste na nova árvore
	public double validaArvore(Node root) {
		boolean classificaResult = false;
		double countErros = 0, n = validation.size();
		for (int j = 0; j < validation.size(); j++) {
			classificaResult = Main.classifica(root, validation.get(j), Integer.MAX_VALUE);
			if (!classificaResult) countErros++;
		}
		return 1 - (countErros / n);
	}

	//Método para a poda da árvore
	public void podaArvore(Node root, double accur) {
		//Primeiro, rodamos uma busca em largura na árvore já montada para coletarmos os nós.
		//Os nós são colocados em uma pilha, que será usada para a poda em si
		
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);
		Stack<Node> rayovac = new Stack<Node>();
		Stack<Node> duracell = new Stack<Node>();
		int arestas = 0;
		int nos = 1;
		while(!queue.isEmpty()) {
			rayovac.push(queue.peek());
			duracell.push(queue.peek());
			Node node = queue.remove();
			Node child=null;
			Aresta a = null;
			for(String s:node.getSetArestas().keySet()) {
				
				arestas++;
				a = node.getAresta(s);
				if(a.getChild() == null) {
					continue;
				}
				
				child = a.getChild();
				nos++;
				queue.add(child);
			}
		}
		System.out.println("\nARESTAS "+arestas+ " NOS " + nos);
		int tamanhoArvore = rayovac.size();
		treinamentoArvore(root, tamanhoArvore);
		testArvoreItera(root, tamanhoArvore);
		System.out.println("Árvore lida. Começando poda...");
		System.out.println("MEDIDAS COM TESTE NA PODA");
		
		//No fim desse processo, a pilha vai ter todos os nós em ordem das folhas para a raiz
		//O próximo passo é pegar esses nós, na ordem, e:
		//1 - Tirar nó da árvore
		//2 - Passar árvore pelo teste
		//3 - Checar se a acurácia aumenta
		//4 - Deixar o nó, caso não aumente. Tirar caso aumente.

		int counter = 0;
		while(!rayovac.empty()) {
			
			counter++;
//			System.out.print(".");
//			if(counter % 100 == 0) System.out.println();
			
			//Pega o último nó folha (de baixo pra cima, da direita pra esquerda
			Node excluded = rayovac.pop();
			//Pega o pai desse nó folha, para consertar a exclusão
			Node pai = excluded.getDad();
			
			if(pai == null) break;
			
			//Aresta auxiliar que vai ser a aresta que o pai vai usar para esse filho
			Aresta theTraitor = (Aresta) excluded.getSetArestas().values().toArray()[0];
			Aresta backup = new Aresta();
			
			//Encontra, dentre as arestas, qual a mais relevante (pelo número de registros em cada um)
			//(por ser nó folha, assume-se que todos os registros no nó são da mesma classe)
			for(Aresta s: excluded.getSetArestas().values()) {
				if(theTraitor.getRegistros().size() < s.getRegistros().size()) {
					theTraitor = s;
				}
			}
			
			//Seta os registros da aresta mais importante 
			pai.getAresta(excluded.getValor_dad()).setRegistros(theTraitor.getRegistros());
			pai.getAresta(excluded.getValor_dad()).setChild(null);
			pai.getAresta(excluded.getValor_dad()).setOtosan(pai);
			
			//Neste ponto, o nó foi podado. Resta ver se a acurácia mudou.
			double newAccur = validaArvore(root);
			
			if(newAccur > accur) {
				accur = newAccur;
			}
			else {
				pai.getAresta(excluded.getValor_dad()).setChild(excluded);
			}
			System.out.println(counter + "," + accur);
		}
		
		//Impressão dos nós, arestas aos quais estão ligadas 
//		int i = 0;
//		int size = duracell.size();
//		System.out.println("\nNós da árvore: ");
//		for(i = 0; i < size; i++) {
//			Node aux = duracell.pop();
//			String pai = ((aux.getDad()) != null ? aux.getDad().getAtributoTeste() : "NINGUÉM");
//			System.out.println("Nó: " + aux.getAtributoTeste() + ", FILHO DE: " + pai + ", LIGADO POR: " + aux.getValor_dad());
//		}
	}
}
