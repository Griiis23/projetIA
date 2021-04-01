package ia;

import java.util.ArrayList;

public class Reseau {

	private int taille_entree;

	private ArrayList<Couche> couches;

	public Reseau(int taille_entree) {
		this.taille_entree = taille_entree;
		this.couches = new ArrayList<Couche>();
	}

	public void addCouche(int nombre_neurones) {
		int taille_entree;

		if (couches.isEmpty()) taille_entree = this.taille_entree;
		else taille_entree = couches.get(couches.size()-1).taille_neurones;

		couches.add(new Couche(taille_entree, nombre_neurones));
	}

	public double[] prediction(double entree[]) {
		double donnee[] = entree;

		for (Couche couche : couches) {
			donnee = couche.propager(donnee);
		}
		return donnee;
	}


	public void apprentissage(double entrees[][], double sorties_attendues[][], double taux) {
		
		for (int ex = 0; ex < entrees.length; ex++) {

			// On calcule la sortie
			double sortie_obtenue[] = prediction(entrees[ex]);

			// On calcule l'erreur de la derniere couche
			Couche derniere = couches.get(couches.size() - 1);
			double erreur[] = new double[derniere.taille_neurones];

			for (int j = 0; j < derniere.taille_neurones; j++ ) {
				erreur[j] = (sorties_attendues[ex][j] - sortie_obtenue[j]) * derniere.neurones[j].activation_prime(derniere.neurones[j].agregation(derniere.entree));

				for(int i = 0; i < taille_entree; i++) {
					derniere.neurones[j].poids[i] += taux * erreur[j] * derniere.entree[i];
				}
			}

			// On retropropage l'erreur sur les couches restante
			for (int n = couches.size() - 2; n >= 0; n--) {
				erreur = couches.get(n).retropropager(erreur, couches.get(n+1), taux);
			}
		}
			
	}


	public static void main(String[] args) {
		double entrees[][] = {
			{1,1},
			{0,1},
			{1,0},
			{0,0}
		};

		double sorties[][] = {
			{0},
			{1},
			{1},
			{0}
		};


		Reseau r = new Reseau(2);

		
		r.addCouche(4);
		r.addCouche(1);



		
		for(int i=0; i < 1000000; i++) r.apprentissage(entrees,sorties, 0.1);

	

		for (int i=0; i<entrees.length; i++) {
			double result[] = r.prediction(entrees[i]);
			System.out.println(result[0]);
		}
	}


}