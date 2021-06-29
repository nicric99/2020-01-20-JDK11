package it.polito.tdp.artsmia.model;


public class testModel {

	public static void main(String[] args) {
		Model model= new Model();
		model.creaGrafo("Artist");
		System.out.println(model.getNVertici()+"\n");
		System.out.println(model.getNArchi()+"\n");

	}

}
