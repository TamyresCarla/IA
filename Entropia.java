package IA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Entropia {

	/**
	 * Metodo que dado um conjunto de registros e os atributos a serem calculados, calcula a entropia
	 * do conjunto
	 *
	 * @param dados   - registros do qual sera calculado a entropia
	 * @return
	 */

	private static final double LOG2 = Math.log(2.0);

	public static double calcula(ArrayList<Registro> dados) {
		double entropy = 0;
		HashMap<String, Integer> frequencias = new HashMap<>();
		Iterator<String> iteradorFreq;

		if(dados.size() == 0) return 0;

		for (int i=0; i < dados.size(); i++) {
			Registro regAtual = dados.get(i);
			String classeAtual = regAtual.getClasse();
			if (!frequencias.containsKey(classeAtual)) frequencias.put(classeAtual, 0);
			frequencias.put(
					classeAtual,
					frequencias.get(classeAtual) + 1);
		}

		iteradorFreq = frequencias.keySet().iterator();

		while (iteradorFreq.hasNext()) {
			String classeAtu = iteradorFreq.next();
			double freqAtu = frequencias.get(classeAtu).doubleValue() / (new Integer(dados.size())).doubleValue();
			entropy -= freqAtu * (Math.log(freqAtu) / LOG2);

		}
		return entropy;
	}
}