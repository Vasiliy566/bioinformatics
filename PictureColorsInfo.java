
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PictureColorsInfo {

	public static double min(double r, double g, double b) {
		return Math.min(r, Math.min(g, b));
	}

	public static double max(double r, double g, double b) {

		return Math.max(r, Math.max(g, b));
	}

	public static double RGBlightness(double r, double g, double b) {
		return (max(r, g, b) + min(r, g, b)) / 2;
	}

	public static double RGBtoHSV(double r, double g, double b) {

		double h, s, v;

		double min, max, delta;

		min = Math.min(Math.min(r, g), b);
		max = Math.max(Math.max(r, g), b);

		// V
		v = max;

		delta = max - min;

		// S
		if (max != 0)
			s = delta / max;
		else {
			s = 0;
			h = -1;
			return h;
		}

		// H
		if (r == max)
			h = (g - b) / delta; // between yellow & magenta
		else if (g == max)
			h = 2 + (b - r) / delta; // between cyan & yellow
		else
			h = 4 + (r - g) / delta; // between magenta & cyan

		h *= 60; // degrees

		if (h < 0)
			h += 360;

		return h;
	}

	public static int pixColorDetector(int argb) {

		// by rgb - black(0,0,0) ; whie(255,255,255)
		// by hsv red = 255 0 0; blue = 0 0 255; 
		//green = 0 255 0; yellow = 255 255 0;

		int r = (argb >> 16) & 0xff;
		int g = (argb >> 8) & 0xff;
		int b = (argb) & 0xff;
		double l = RGBlightness(r, g, b);
		double h = RGBtoHSV(r, g, b);
		double min = min(r, g, b);
		double max = max(r, g, b);
		if (min > 220) {
			return 0;// Color.white;

		} else if (max - min < 40) {
			return 1;// Color.black;
		} else if (h >= 300 || h < 9) {
			return 2;// Color.red;
		} else if (h <= 263 && h > 226) {
			return 3;// Color.blue;
		} else if (h < 143 && h > 85) {
			return 4;// Color.green;
		} else if (h < 69 && h > 40) {
			return 5;// Color.yellow;
		} else
			return 6;
	}

	public static ArrayList<boolean[][]> levelsColors(BufferedImage image) {
		ArrayList<boolean[][]> levels = new ArrayList<boolean[][]>();
		for (int k = 1; k < 7; k++) {
			boolean[][] arrCopy = new boolean[image.getWidth()][image.getHeight()];
			for (int i = 0; i < image.getWidth(); i++) {
				for (int j = 0; j < image.getHeight(); j++) {
					if (pixColorDetector(image.getRGB(i, j)) == k) {
						arrCopy[i][j] = true;
					}
				}
			}
			levels.add(arrCopy);
		}
		return levels;
	}

	public static boolean ArrisEmpty(boolean[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	public static int DifferentColorsAmount(BufferedImage image) {
		ArrayList<boolean[][]> levels = levelsColors(image);
		int k = 0;

		for (int i = 0; i < 6; i++) {
			if (levels.get(i) != null && !ArrisEmpty(levels.get(i))) {
				k++;
			}
		}

		return k;
	}

	public static void pixSet(BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				if (pixColorDetector(image.getRGB(i, j)) == 0) {
					image.setRGB(i, j, 16777215);
				} else if (pixColorDetector(image.getRGB(i, j)) == 1) {
					image.setRGB(i, j, 0);
				} else if (pixColorDetector(image.getRGB(i, j)) == 2) {
					image.setRGB(i, j, 16711680);
				} else if (pixColorDetector(image.getRGB(i, j)) == 3) {
					image.setRGB(i, j, 255);
				} else if (pixColorDetector(image.getRGB(i, j)) == 4) {
					image.setRGB(i, j, 65280);
				} else if (pixColorDetector(image.getRGB(i, j)) == 5) {
					image.setRGB(i, j, 16776960);
				} else if (pixColorDetector(image.getRGB(i, j)) == 6) {
					image.setRGB(i, j, 16777215);
				}
			}
		}
	}

	public static int arrMaxElementIndexSpecific(ArrayList<int[]> colors) {
		int a = 0;
		int place = -1;
		for (int i = 0; i < colors.size(); i++) {
			if (colors.size() != 0) {
				if (a == 0) {
					a = colors.get(i)[1];
					place = i;

				}
				if (colors.get(i)[1] > a) {
					a = colors.get(i)[1];
					place = i;
				}
			}
		}

		return place;
	}

	public static void main(String[] args) throws IOException {
		// BufferedImage image = ImageIO.read(new File("test1.png"));
		BufferedImage image = ImageIO.read(new File("chemElement.gif"));
		// BufferedImage image = ImageIO.read(new File("test.jpg"));

		pixSet(image);

		JFrame myWindow = new JFrame("Window");
		Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
		myWindow.setSize(sSize);
		myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3642256317969682683L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.drawImage(image, 0, 0, null);
			}
		};
		myWindow.setContentPane(pane);
		myWindow.setVisible(true);
	}

}
