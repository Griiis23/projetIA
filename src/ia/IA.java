package ia;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class IA {
	
	Reseau Reseau;

	public double[][] charger_train(String path) {

		ArrayList<double[]> list = new ArrayList<double[]>();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 1000; j++) {
				double[] entree = charger_image(path + "/"+ i + "/" + j + ".png");
				if (entree != null) list.add(entree);	
			}
		} 

		return null;
	}


	public double[] charger_image(String path) {
		try {
			File file = new File(path);
			
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
			System.err.println(e.getMessage());
			return null;
		}
	}

	public static void main(String[] args) {
		
	}
}
