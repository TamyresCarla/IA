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
    	//Divide os tr�s conjuntos: teste, valida��o e treinamento
		Main.kFold(3, true);
		test = new ArrayList<>();
		validation = new ArrayList<>();
		training = new ArrayList<>();

		test.addAll(Main.testRegs);
		validation.addAll(Main.foldedExamples.get(0));
		training.addAll(Main.foldedExamples.get(1));

    }
    
    //M�todo que cria a �rvore com o conjunto de treinamento atribu�do na constru��o do objeto
    public Node criaArvore(ID3 id3) {
		Node root = id3.generateTree(training, Main.atributos);
		return root;
	}

    //M�todo para testar os exemplos do conjunto de teste na nova �rvore
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

    //M�todo para testar os exemplos do conjunto de teste na nova �rvore
	public double testArvore(Node root) {
    	boolean classificaResult = false;
    	double countErros = 0, n = test.size();
		for (int j = 0; j < test.size(); j++) {
			classificaResult = Main.classifica(root, test.get(j), Integer.MAX_VALUE);
			if (!classificaResult) countErros++;
		}
		return 1 - (countErros / n);
	}

	//M�todo para testar os exemplos do conjunto de teste na nova �rvore
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

	//M�todo para testar os exemplos do conjunto de teste na nova �rvore
	public double validaArvore(Node root) {
		boolean classificaResult = false;
		double countErros = 0, n = validation.size();
		for (int j = 0; j < validation.size(); j++) {
			classificaResult = Main.classifica(root, validation.get(j), Integer.MAX_VALUE);
			if (!classificaResult) countErros++;
		}
		return 1 - (countErros / n);
	}

	//M�todo para a poda da �rvore
	public void podaArvore(Node root, double accur) {
		//Primeiro, rodamos uma busca em largura na �rvore j� montada para coletarmos os n�s.
		//Os n�s s�o colocados em uma pilha, que ser� usada para a poda em si
		
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
		System.out.println("�rvore lida. Come�ando poda...");
		System.out.println("MEDIDAS COM TESTE NA PODA");
		
		//No fim desse processo, a pilha vai ter todos os n�s em ordem das folhas para a raiz
		//O pr�ximo passo � pegar esses n�s, na ordem, e:
		//1 - Tirar n� da �rvore
		//2 - Passar �rvore pelo teste
		//3 - Checar se a acur�cia aumenta
		//4 - Deixar o n�, caso n�o aumente. Tirar caso aumente.

		int counter = 0;
		while(!rayovac.empty()) {
			
			counter++;
//			System.out.print(".");
//			if(counter % 100 == 0) System.out.println();
			
			//Pega o �ltimo n� folha (de baixo pra cima, da direita pra esquerda
			Node excluded = rayovac.pop();
			//Pega o pai desse n� folha, para consertar a exclus�o
			Node pai = excluded.getDad();
			
			if(pai == null) break;
			
			//Aresta auxiliar que vai ser a aresta que o pai vai usar para esse filho
			Aresta theTraitor = (Aresta) excluded.getSetArestas().values().toArray()[0];
			Aresta backup = new Aresta();
			
			//Encontra, dentre as arestas, qual a mais relevante (pelo n�mero de registros em cada um)
			//(por ser n� folha, assume-se que todos os registros no n� s�o da mesma classe)
			for(Aresta s: excluded.getSetArestas().values()) {
				if(theTraitor.getRegistros().size() < s.getRegistros().size()) {
					theTraitor = s;
				}
			}
			
			//Seta os registros da aresta mais importante 
			pai.getAresta(excluded.getValor_dad()).setRegistros(theTraitor.getRegistros());
			pai.getAresta(excluded.getValor_dad()).setChild(null);
			pai.getAresta(excluded.getValor_dad()).setOtosan(pai);
			
			//Neste ponto, o n� foi podado. Resta ver se a acur�cia mudou.
			double newAccur = validaArvore(root);
			
			if(newAccur > accur) {
				accur = newAccur;
			}
			else {
				pai.getAresta(excluded.getValor_dad()).setChild(excluded);
			}
			System.out.println(counter + "," + accur);
		}
		
		//Impress�o dos n�s, arestas aos quais est�o ligadas 
//		int i = 0;
//		int size = duracell.size();
//		System.out.println("\nN�s da �rvore: ");
//		for(i = 0; i < size; i++) {
//			Node aux = duracell.pop();
//			String pai = ((aux.getDad()) != null ? aux.getDad().getAtributoTeste() : "NINGU�M");
//			System.out.println("N�: " + aux.getAtributoTeste() + ", FILHO DE: " + pai + ", LIGADO POR: " + aux.getValor_dad());
//		}
	}
}
