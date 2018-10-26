package IA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Ganho {

	/**
	 * Calcula o ganho de informacao de determinado atributo
	 *
	 * @param dados  			- Conjunto de dados
	 * @param atributos 		- Conjunto de atributos
	 * @param atributoChave     - Atributo chave para calculo do ganho
	 * @return
	 */
	public static double calcula(ArrayList<Registro> dados, ArrayList<Atributo> atributos, String atributoChave) {
		double gain = Entropia.calcula(dados);
		HashMap<String, ArrayList<Registro>> subconj = montaSubconj(dados, atributos, atributoChave);

		// variaveis para o calculo das subentropías
		String atribValAtu;
		ArrayList<Registro> subconjAtu;

		Iterator<String> subconjIterator = subconj.keySet().iterator();
		while (subconjIterator.hasNext()) {
			atribValAtu = subconjIterator.next();
			subconjAtu = subconj.get(atribValAtu);

			double gainSubAtu = ((new Integer(subconjAtu.size())).doubleValue() / (new Integer(dados.size())).doubleValue()) * Entropia.calcula(subconjAtu);
			gain = gain - gainSubAtu;
		}
		
//		System.out.println(gain);
		
		return gain;
	}

	public static HashMap<String, ArrayList<Registro>> montaSubconj(ArrayList<Registro> dados, ArrayList<Atributo> atributos, String atributoChave) {

		HashMap<String, ArrayList<Registro>> subconj = new HashMap<>();
		String valorAtribAtu;
		Registro regAtu;
		for (int i = 0; i < dados.size(); i++) {
			regAtu = dados.get(i);
			valorAtribAtu = regAtu.getDado(atributoChave, atributos);
			if (!subconj.containsKey(valorAtribAtu)) {
				subconj.put(valorAtribAtu, new ArrayList<Registro>());
			}
			subconj.get(valorAtribAtu).add(regAtu);

		}
		return subconj;
	}

}