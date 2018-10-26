package IA;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

// Classe que implementa um manipulador de entrada e saida
// assume-se que o conjunto de dados esteja discretizado
// e que a primeira linha do arquivo contenha os nomes dos atributos
// o separador dos valores pode ser alterado via setSeparador

public class IOManager {

	private static String SEPARADOR = ";";

	public static void setSeparador(String newSep){ SEPARADOR = newSep; }


	// todo: WIP CONSTRUCAO DA SAIDA
	public static void writeArvore(String path, Node raiz, int option) {
		try {
			
			String rule = "IF " + raiz.getAtributoTeste(); 
			if(option == 1) {
				PrintWriter wr = new PrintWriter(path, "UTF-8");
				writePathsRecur(raiz, rule, wr);
				wr.close();
			}
			else printPathsRecur(raiz, rule);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e3) {
			e3.printStackTrace();
		}
	}

	// leitura do arquivo de entrada
	public static void readDataset(String path, ArrayList<Atributo> atributos, ArrayList<Registro> registros) {
		String linha;
		String[] atribs;
		try {
			BufferedReader rd = new BufferedReader(new FileReader(path));

			linha = rd.readLine();
			atribs = linha.split(SEPARADOR);

			for (int i = 0; i < atribs.length - 1; i++) atributos.add(new Atributo(atribs[i]));

			while((linha = rd.readLine()) != null) {
				registros.add(new Registro(linha.split(SEPARADOR)));
			}

		} catch (IOException e1) {
			System.out.println("Erro ao ler arquivo de entrada");
		}
	}
	
    static void writePathsRecur(Node raiz, String rule, PrintWriter wr) {
    	
    	Iterator<String> raizIT = raiz.getSetArestas().keySet().iterator();
    	String valorAtrib;
    	String newRule;
    	while(raizIT.hasNext()) {
    		valorAtrib = raizIT.next();
    		newRule = rule + " IS " + valorAtrib;
    		if(raiz.getAresta(valorAtrib).getChild() == null) {
				newRule = newRule + " THEN " + raiz.getAresta(valorAtrib).getClasseMajor();
				// System.out.println(newRule);
    			wr.write(newRule);
    			wr.println();
    			//System.out.println(newRule);
    		} else {
    			newRule = newRule + " AND " + raiz.getAresta(valorAtrib).getChild().getAtributoTeste();
    			writePathsRecur(raiz.getAresta(valorAtrib).getChild(), newRule, wr);
    		}
    	}
    }
    
    static void printPathsRecur(Node raiz, String rule) {
    	
    	Iterator<String> raizIT = raiz.getSetArestas().keySet().iterator();
    	String valorAtrib;
    	String newRule;
    	while(raizIT.hasNext()) {
    		valorAtrib = raizIT.next();
    		newRule = rule + " IS " + valorAtrib;
    		if(raiz.getAresta(valorAtrib).getChild() == null) {
    			newRule = newRule + " THEN " + raiz.getAresta(valorAtrib).getClasseMajor();
    			System.out.println(newRule);
//    			wr.write(newRule);
//    			wr.println();
    			//System.out.println(newRule);
    		} else {
    			newRule = newRule + " AND " + raiz.getAresta(valorAtrib).getChild().getAtributoTeste();
    			printPathsRecur(raiz.getAresta(valorAtrib).getChild(), newRule);
    		}
    	}
    }
}