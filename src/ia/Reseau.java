package ia;

import java.util.*;

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

	/**
	 * Retourne la sortie du réseau pour une entrée donnée
	 * 
	 * @param entree	Entrée du réseau
	 * @return	Sortie du réseau
	 */
	public double[] prediction(double entree[]) {
		double donnee[] = entree;

		for (Couche couche : couches) {
			donnee = couche.propager(donnee);
		}
		return donnee;
	}

	/**
	 * Algorithme de rétropropagation du gradient
	 * 
	 * @param entrees_app	Liste des entrées des exemples d'apprentissage
	 * @param sorties_app	Liste des sorties des exemples d'apprentissage
	 * @param taux			Taux d'apprentissage
	 */
	public void retropropagation(ArrayList<double[]> entrees_app, ArrayList<double[]> sorties_app, double taux) {
		// Pour chaque exemple d'apprentissage
		for (int count = 0; count < entrees_app.size(); count++) {
			
			// On choisis un exemple au hasard
			int ex = (int) (Math.random() * entrees_app.size());
			
			// On calcule la sortie
			double sortie_obtenue[] = prediction(entrees_app.get(ex));

			// On calcule l'erreur de la derniere couche
			Couche derniere = couches.get(couches.size() - 1);
			double erreur[] = new double[derniere.taille_neurones];

			for (int j = 0; j < derniere.taille_neurones; j++ ) {
				erreur[j] = (sorties_app.get(ex)[j] - sortie_obtenue[j]) * derniere.neurones[j].activation_prime(derniere.neurones[j].agregation(derniere.entree));

				for(int i = 0; i < derniere.taille_entree; i++) {
					derniere.neurones[j].poids[i] += taux * erreur[j] * derniere.entree[i];
				}
			}

			// On retropropage l'erreur sur les couches restante
			for (int n = couches.size() - 2; n >= 0; n--) {
				erreur = couches.get(n).retropropager(erreur, couches.get(n+1), taux);
			}
		}
			
	}
	
	/**
	 * Calcule l'erreur quadratique moyenne du réseau sur un set d'exemples
	 * 
	 * @param entrees	Liste des entrées des exemples
	 * @param sorties	Liste des sorties des exemples 
	 * @return	Erreur quadratique moyenne du réseau 
	 */
	public double validation(ArrayList<double[]> entrees, ArrayList<double[]> sorties) {
		
		Couche derniere = couches.get(couches.size() - 1);
		double erreur[] = new double[derniere.taille_neurones];
		
		// Pour chaque chaque exemple
		for (int ex = 0; ex < entrees.size(); ex++) {

			// On calcule la sortie
			double sortie_obtenue[] = prediction(entrees.get(ex));

			// On calcule la moyenne des erreurs quadratique en sortie de chaque neurrone 
			for (int j = 0; j < derniere.taille_neurones; j++ ) {
				erreur[j] += Math.pow(sorties.get(ex)[j] - sortie_obtenue[j],2)/entrees.size();
			}
		}
		
		// On fait la moyenne des moyennes
		double moyenne = 0;
		for(int i = 0; i < derniere.taille_neurones; i++) {
			moyenne += erreur[i]/derniere.taille_neurones;
		}
		
		return moyenne;
	}

}