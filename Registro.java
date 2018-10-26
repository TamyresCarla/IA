package IA;
import java.util.ArrayList;

//Classe que define um registro qualquer
//contem sua lista de dados
public class Registro {

 private String[] dados;
 private String classe;

 public Registro(String[] dados) {
     this.dados = new String[dados.length];
     this.dados = dados;
     this.classe = dados[dados.length - 1];
 }

	public String[] getDados() { return dados; }

	public String getDado(String atributo, ArrayList<Atributo> ListAtributos) {

     for (int i = 0; i < Main.atributos.size(); i++) {
         if (Main.atributos.get(i).getNome() == atributo){
             return dados[i];
         }
     }
     return null;
 }

	public void setValores(String[] dados) {
		this.dados = new String[dados.length];
		this.dados = dados;
	}

 public void setClasse(String classe) { this.classe = classe; }
 public String getClasse() { return classe; }
}