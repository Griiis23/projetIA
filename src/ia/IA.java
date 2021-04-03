package ia;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.*;

public class IA {
	
	private Reseau reseau;

	public IA () {
		this.reseau = new Reseau(1024);
		this.reseau.addCouche(64);
		this.reseau.addCouche(26);
	}

	/**
	 * Convertit une image 32x32 en un tableau de double représentant le % de blanc de chaque pixel
	 * 
	 * @param chemin	Chemin de l'image
	 * @return			Tableau de double ou null si erreur
	 */
	public double[] charger_image(String chemin) {
		try {
			File file = new File(chemin);
			BufferedImage image = ImageIO.read(file);
			int w = 32;
			int h = 32;
			double entree[] = new double[w*h];

			// Pour chaque pixels
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					int pixel = image.getRGB(j, i);
	
					int red = (pixel >> 16) & 0xff;
    				int green = (pixel >> 8) & 0xff;
    				int blue = (pixel) & 0xff;
    				
    				// On calcule la moyenne des 3 sous pixels
    				entree[i*w + j] = ((double)red/255 + (double)green/255 + (double)blue/255)/3;
				}
			}
			return entree;
		} catch (IOException e) {
			System.err.println("Impossible de charger " + chemin );
			return null;
		}
		
	}
	
	/**
	 *  Charge les images des dossier A à Z du dossier pointé par chemin
	 *  
	 * @param chemin	Chemin du dossier contenant des dossier A à Z
	 * @param entrees	Liste des entrées des exemples
	 * @param sorties	Liste des sorties des exemples
	 */
	public void charger_donnees(String chemin, ArrayList<double[]> entrees, ArrayList<double[]> sorties) {
		// Pour chaque sous dossier
		for(char lettre = 'A'; lettre <= 'Z'; lettre++) {
			File file = new File(chemin + "/" + lettre);
			String[] contenu = file.list();
			
			// Si le sous dossier n'est pas vide
			if (contenu != null) {
				for(String nom : contenu) {
					// On charge son contenu
					double entree[] = charger_image(chemin + "/"  + lettre + "/" + nom);
					if(entree != null) {
						// On ajoute l'entrée et la sortie aux listes
						double sortie[] = new double[26];
						Arrays.fill(sortie, 0);
						sortie[lettre - 65] = 1;
						entrees.add(entree);
						sorties.add(sortie);
					}
				}
			}
		}
	}
	
	/**
	 * Fonction d'apprentissage
	 * 
	 * @param chemin	Chemin du dossier data
	 * @param seuil		Seuil de validation
	 * @param taux		Taux d'apprentissage
	 */
	public void apprendre(String chemin, double seuil, double taux) {
		
		// Charger les données pour l'apprentissage
		System.out.println("Chargement des données");
		ArrayList<double[]> entrees_app = new ArrayList<double[]>();
		ArrayList<double[]> sorties_app = new ArrayList<double[]>();
		charger_donnees(chemin + "/Train", entrees_app, sorties_app);
		
		// Charger les données pour la validation
		ArrayList<double[]> entrees_valid = new ArrayList<double[]>();
		ArrayList<double[]> sorties_valid = new ArrayList<double[]>();
		charger_donnees(chemin + "/Validation", entrees_valid, sorties_valid);
		
		// Faire apprendre les données
		System.out.println("Début apprentissage");
		double erreur;
		do {
			reseau.retropropagation(entrees_app, sorties_app, 0.01);
			// Tester le réseau
			erreur = reseau.validation(entrees_valid, sorties_valid);
			System.out.println("Erreur quadratique moyenne : " + erreur);
		} while(erreur > seuil);
		
		System.out.println("Fin apprentissage");
	}
	
	
	
	/**
	 * Teste le réseau sur une image et affiche le résultat
	 * 
	 * @param chemin	Chemin de l'image
	 */
	public void tester(String chemin) {
		// Charger l'image à tester
		double entree[] = charger_image(chemin);
		if(entree == null) return;
		
		// Donner au réseau
		double sortie[] = reseau.prediction(entree);
		
		// Afficher sortie
		for (int i = 0; i < sortie.length; i++) {
			System.out.printf("%c : %1.2f%n", (char) (i+65), sortie[i]);
		}
	}

	public static void main(String[] args) {
		IA ia = new IA();
		Scanner sc = new Scanner(System.in);
		sc.useLocale(Locale.US);
		String rep = "";
		do {
			try {
				System.out.println("=== MENU ===\n1. Faire apprendre\n2. Tester\n3. Quitter");
				rep = sc.next().trim();
				if (rep.equals("1")) {
					System.out.println("Seuil ?");
					double seuil = sc.nextDouble();
					System.out.println("Chemin du dossier de données ?");
					String chemin = sc.next();
					ia.apprendre(chemin, seuil, 0.01);
				}
				else if (rep.equals("2")) {
					System.out.println("Chemin de l'image ?");
					String chemin = sc.next();
					ia.tester(chemin);
				}
			}
			catch (Exception e) {
				System.err.println(e);
			}
		} while (!rep.equals("4"));
	}
}
