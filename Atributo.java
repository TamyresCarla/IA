package IA;

//Classe que define um atributo
//
public class Atributo {
	private String nome;
	private double ganho;

	public Atributo() { }

	public Atributo(String nome) { this.nome = nome; }


	public void setNome(String nome) { this.nome = nome; }
	public String getNome() { return nome; }

	public double getGanho() { return ganho; }
	public void setGanho(double ganho) { this.ganho = ganho; }
}
