package ia;

public class Neurone {

	private static final int lambda = 2;

	double poids[];


	public Neurone(int taille_entree) {
		this.poids = new double[taille_entree];

		for (int i = 0; i < taille_entree; i++) this.poids[i] = (2 * Math.random() - 1) * 0.01;
	}


	public double activation(double entree) {
		return sigmoide(entree);
	}

	public double activation_prime(double entree) {
		return sigmoide_prime(entree);
	}

	public double agregation(double entree[]) {
		double somme = 0;

		for (int i = 0; i < entree.length; i++) {
			somme += poids[i] * entree[i];
		}

		return somme;
	}

	private double sigmoide(double x) {
		return 1 / ( 1 + Math.pow ( Math.E, (-lambda*x) ));
	}

	private double sigmoide_prime(double x) {
		return lambda * sigmoide(x) * ( 1-sigmoide(x) );
	}

}
