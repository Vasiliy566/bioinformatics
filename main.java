import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class main {

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		String impImage = scan.nextLine();

		BufferedImage image = ImageIO.read(new File(impImage));

		scan.close();

		PictureColorsInfo.pixSet(image);
		ArrayList<boolean[][]> colorsLevels = PictureColorsInfo.levelsColors(image);

		System.out.println("Colors: " + PictureColorsInfo.DifferentColorsAmount(image));
		System.out.println(
				"Figures: " + PictureConfigInfo.figureDetector(PictureConfigInfo.imageToPixelArray(image)).size());
		System.out.println();
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				System.out.println("Black color:");
			}
			if (i == 1) {
				System.out.println("Red color:");
			}
			if (i == 2) {
				System.out.println("Blue color:");
			}
			if (i == 3) {
				System.out.println("Green color:");
			}
			if (i == 4) {
				System.out.println("Yellow color:");
			}
			if (i == 5) {
				System.out.println("Other colors:");
			}

			int circles = 0;
			int lines = 0;
			int arcs = 0;
			int emptyCircles = 0;
			ArrayList<boolean[][]> figures = PictureConfigInfo.figureDetector(colorsLevels.get(i));
			System.out.println(figures.size() + " - objects amount");
			for (int j = 0; j < figures.size(); j++) {

				if (PictureConfigInfo.Circle(figures.get(j)) == "fill circle") {
					circles++;
				} else if (PictureConfigInfo.Circle(figures.get(j)) == "empty circle") {
					emptyCircles++;
				} else if (PictureConfigInfo.arc(figures.get(j)) == "line") {
					lines++;
				}

				else if (PictureConfigInfo.arc(figures.get(j)) == "arc") {
					arcs++;
				}

			}
			System.out.println(circles + " - fill circles amount");
			System.out.println(emptyCircles + " - empty circles amount");
			System.out.println(arcs + " - arcs amount");
			System.out.println(lines + " - lines amount");
			System.out.println();

		}

	}

}
