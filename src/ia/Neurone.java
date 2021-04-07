package ia;

/**
 * Classe représentant un neurone d'une couche utilisant la fonction sigmoide
 *
 */
public class Neurone {

	/**
	 * Constante lambda de la fonction sigmoide
	 */
	private static final int lambda = 2;

	/**
	 * Tableau des poids pour chaque entrée du neurone
	 */
	double poids[];


	/**
	 * Constructeur
	 * @param taille_entree Nombre d'entrees du neurone
	 */
	public Neurone(int taille_entree) {
		this.poids = new double[taille_entree];

		//On initialise des poids entre -0.1 et 0.1
		for (int i = 0; i < taille_entree; i++) this.poids[i] = (2 * Math.random() - 1) * 0.01;
	}


	/**
	 * Fonction d'activation
	 */
	public double activation(double entree) {
		return sigmoide(entree);
	}

	/**
	 * Dérivée de la fonction d'activation
	 */
	public double activation_prime(double entree) {
		return sigmoide_prime(entree);
	}

	/**
	 * Fonction d'agrégation
	 */
	public double agregation(double entree[]) {
		double somme = 0;

		// Somme pondérée des entrées
		for (int i = 0; i < entree.length; i++) somme += poids[i] * entree[i];

		return somme;
	}

	private double sigmoide(double x) {
		return 1 / ( 1 + Math.pow ( Math.E, (-lambda*x) ));
	}

	private double sigmoide_prime(double x) {
		return lambda * sigmoide(x) * ( 1-sigmoide(x) );
	}

}
