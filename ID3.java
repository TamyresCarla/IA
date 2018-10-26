package IA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class ID3 {
	/**
	 * Gera a arvore de decisao de forma recursiva.
	 *
	 * @param registros  - Dados a serem classificados pela arvore
	 * @param atributos  - Atributos a serem utilizados pelo classificador
	 * @return     - Arvore de decisao
	 */
	public Node generateTree(ArrayList<Registro> registros, ArrayList<Atributo> atributos) {

	    Node root = null;

        if (atributos.size() <= 0) return root;
        if (hasSingleClass(registros))	return root;

		//Inicializa as variaveis para selecionar o melhor atributo
		String melhorAtributo= null;
        String atributoAtual = "";
		double melhorGanho = 0.0, ganhoAtu;

		//Calcula o melhor ganho para os registros a serem considerados
		for (int i=0; i< atributos.size(); i++) {
            atributoAtual = atributos.get(i).getNome();
            ganhoAtu = Ganho.calcula(registros, atributos, atributoAtual);
            atributos.get(i).setGanho(ganhoAtu);
            if (ganhoAtu > melhorGanho) {
                melhorAtributo = atributoAtual;
                melhorGanho = ganhoAtu;
            }
        }
        
        String atribAtu;

        //Montagem da arvore completa:
        String valorAtrib = "";
        ArrayList<Registro> subconjRegs;
        root = new Node();

        //Armazena uma listagem dos atributos restantes para a subarvore
        ArrayList<Atributo> atribsRestantes = new ArrayList<>();
        Iterator<Atributo> atribIterator = atributos.iterator();
 
        //Caso nenhum dos atributos observados seja melhor, cria-se um nó-folha para cada, com suas respectivas arestas
        if(melhorGanho == 0.0) {
        	for(int i = 0; i < atributos.size(); i++) {
        		melhorAtributo = atributoAtual;
        		
        		root.setAtributoTeste(melhorAtributo);
        		
        		while (atribIterator.hasNext()) {
        			atribAtu = atribIterator.next().getNome();
        			if (!atribAtu.equals(melhorAtributo)) {
        				atribsRestantes.add(new Atributo(atribAtu));
        			}
        		}
        		
        		HashMap<String, ArrayList<Registro>> subconjRoot = Ganho.montaSubconj(registros, atributos, melhorAtributo);
        		Iterator<String> subconjRootIT = subconjRoot.keySet().iterator();
        		
        		while (subconjRootIT.hasNext()) {
        			valorAtrib = subconjRootIT.next();
        			subconjRegs = subconjRoot.get(valorAtrib);
					root.criaAresta(valorAtrib, subconjRegs, null, root);
//        			Main.numArestas++;
        		}
//        		Main.numNodes++;
        	}
        	return root;
        	
        } else {
        	root.setAtributoTeste(melhorAtributo);
        	
        	while (atribIterator.hasNext()) {
        		atribAtu = atribIterator.next().getNome();
        		if (!atribAtu.equals(melhorAtributo)) {
        			atribsRestantes.add(new Atributo(atribAtu));
        		}
        	}
        	
        	HashMap<String, ArrayList<Registro>> subconjRoot = Ganho.montaSubconj(registros, atributos, melhorAtributo);
        	Iterator<String> subconjRootIT = subconjRoot.keySet().iterator();
        	
        	while (subconjRootIT.hasNext()) {
        		valorAtrib = subconjRootIT.next();
        		subconjRegs = subconjRoot.get(valorAtrib);
        		root.criaAresta(valorAtrib, subconjRegs, generateTree(subconjRegs, atribsRestantes), root);
//        		Main.numArestas++;
        	}
//        	Main.numNodes++;
        	return root;
        }
	}

	public boolean hasSingleClass(ArrayList<Registro> regs) {
	    String classe = regs.get(0).getClasse(), classeAtu;
	    for (int i = 1; i<regs.size(); i++) {
	        classeAtu = regs.get(i).getClasse();
	        if (!classe.equals(classeAtu)) return false;
        }
        return true;
    }

}
