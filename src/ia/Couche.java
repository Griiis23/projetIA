package ia;

/**
 * Classe représentant une couche du réseau
 *
 */
class Couche {

	/**
	 * On garde une référence de la dernière entrée de la couche pour la rétropropagation
	 */
	double entree[];
	
	/**
	 * On garde une référence de la dernière sortie de la couche le réseau pour la rétropropagation
	 */
	double sortie[];

	/**
	 * Nombre d'entrées de la couche
	 */
	int taille_entree;
	
	
	/**
	 * Nombre de neurones / de sorties de la couche
	 */
	int taille_neurones;

	/**
	 * Tableau de neurone de la couche
	 */
	Neurone neurones[];

	
	/**
	 * Constructeur
	 * @param taille_entree Nombre d'entrées de la couche
	 * @param taille_neurones  Nombre de neurones / de sorties de la couche
	 */
	public Couche(int taille_entree, int taille_neurones) {
		this.taille_entree = taille_entree;
		this.taille_neurones = taille_neurones;

		this.neurones = new Neurone[taille_neurones];

		for (int i = 0; i < taille_neurones ; i++) {
			this.neurones[i] = new Neurone(taille_entree);
		}
	}

	/**
	 * Calcule la sortie de la couche
	 * 
	 * @param entree	Entree de la couche
	 * @return Sortie de la couche
	 */
	public double[] propager(double entree[]) {
		this.entree = entree;

		this.sortie = new double[taille_neurones];
		
		for (int i = 0; i < taille_neurones; i++) {
			sortie[i] = neurones[i].activation(neurones[i].agregation(entree));
		}

		return sortie;
	}

	/**
	 * Calcule l'erreur de la couche à partir de la couche suivante.
	 * Met à jour les poids
	 * 
	 * @param erreur_couche_suiv	Erreur de la couche suivante
	 * @param couche_suiv			Référence de la couche suivant
	 * @param taux					Taux d'apprentissage
	 * @return Erreur la couche
	 */
	public double[] retropropager(double erreur_couche_suiv[], Couche couche_suiv, double taux) {
		double erreur[] = new double[taille_neurones];
		// On calcule l'erreur pour chaque neuronne de cette chouche
		for (int j = 0; j < taille_neurones; j++) {
			double somme = 0;
			for (int k = 0; k < couche_suiv.taille_neurones; k++ ) {
				somme += couche_suiv.neurones[k].poids[j] * erreur_couche_suiv[k];				
			}
			erreur[j] = somme * neurones[j].activation_prime(neurones[j].agregation(entree));

			// On met à jour les poids
			for(int i = 0; i < taille_entree; i++) {
				neurones[j].poids[i] += taux * erreur[j] * entree[i];
			}
		}

		return erreur;
	}
	
}
