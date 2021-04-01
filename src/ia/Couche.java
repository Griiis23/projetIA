package ia;

class Couche {

	double entree[];
	double sortie[];

	int taille_entree;
	int taille_neurones;

	Neurone neurones[];

	public Couche(int taille_entree, int taille_neurones) {
		this.taille_entree = taille_entree;
		this.taille_neurones = taille_neurones;

		this.neurones = new Neurone[taille_neurones];

		for (int i = 0; i < taille_neurones ; i++) {
			this.neurones[i] = new Neurone(taille_entree);
		}
	}

	public double[] propager(double entree[]) {
		this.entree = entree;

		this.sortie = new double[taille_neurones];
		
		for (int i = 0; i < taille_neurones; i++) {
			sortie[i] = neurones[i].activation(neurones[i].agregation(entree));
		}

		return sortie;
	}

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
