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

	public double[] charger_image(String chemin) {
		try {
			File file = new File(chemin);
			BufferedImage image = ImageIO.read(file);
			int w = 32;
			int h = 32;
			double entree[] = new double[w*h];

			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					int pixel = image.getRGB(j, i);
	
					int red = (pixel >> 16) & 0xff;
    				int green = (pixel >> 8) & 0xff;
    				int blue = (pixel) & 0xff;

    				entree[i*w + j] = ((double)red/255 + (double)green/255 + (double)blue/255)/3;
				}
			}
			return entree;
		} catch (IOException e) {
			System.err.println("Impossible de charger " + chemin );
			return null;
		}
		
	}
	
	public void charger_donnees(String chemin, ArrayList<double[]> entrees, ArrayList<double[]> sorties) {

		for(char lettre = 'A'; lettre <= 'Z'; lettre++) {
			File file = new File(chemin + "/" + lettre);
			String[] contenu = file.list();
			
			if (contenu != null) {
				for(String nom : contenu) {
					double entree[] = charger_image(chemin + "/"  + lettre + "/" + nom);

					if(entree != null) {
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
		System.out.println("Debut apprentissage");
		
		double erreur;
		do {
			reseau.apprentissage(entrees_app, sorties_app, 0.01);
			erreur = reseau.validation(entrees_valid, sorties_valid);
			System.out.println("Erreur quadratique moyenne : " + erreur);
		} while(erreur > seuil);
		
		System.out.println("Fin apprentissage");
	}
	
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
				System.out.println("1. Faire apprendre\n2. Tester\n3. Quitter");
				rep = sc.next().trim();
				if (rep.equals("1")) {
					System.out.println("Seuil ?");
					double seuil = sc.nextDouble();
					System.out.println("Chemin des données ?");
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
