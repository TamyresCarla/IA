package InteligenciaArtificial;

public class No {

	private int valor; // resultado da busca
	private int custo;
	private No noPai;
	private No noEsquerda;
	private No noDireita;

	public No(int valor) {
		this.valor = valor;
		this.custo = 0;
	}

	public No(int valor, int custo) {
		this.valor = valor;
		this.custo = custo;
	}

	public int getCusto() {
		return custo;
	}

	public void setCusto(int custo) {
		this.custo = custo;
	}

	public No getNoPai() {
		return noPai;
	}

	public void setNoPai(No noPai) {
		this.noPai = noPai;
	}

	public No getNoEsquerda() {
		return noEsquerda;
	}

	public void setNoEsquerda(No noEsquerda) {
		this.noEsquerda = noEsquerda;
		this.noEsquerda.setNoPai(this);
	}

	public No getNoDireita() {
		return noDireita;
	}

	public void setNoDireita(No noDireita) {
		this.noDireita = noDireita;
		this.noDireita.setNoDireita(this);
	}

	public int getValor() {
		return valor;
	}

}
