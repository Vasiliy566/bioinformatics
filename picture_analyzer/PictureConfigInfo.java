import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import java.util.Queue;


import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PictureConfigInfo {
	public static int fX = 0;
	public static int fY = 0;
	public static int sX = 0;
	public static int sY = 0;
	public static int rangeX = 0;
	public static int rangeY = 0;
	public static int range = 0;
	public static int centreX = 0;
	public static int centreY = 0;
	public static int Y = 0;
	public static ArrayList<Point> detectedPoints = new ArrayList<>();

	public static String arc(boolean arr[][]) { // arc or line detection

		ArrayList<Point> pts = new ArrayList<>();
		Point point1 = new Point(-1, -1);// один конец
		
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		// находим точки с ОДНИМ выделяющимся значением
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j]) {
					minX = i;
					pts.add(new Point(i, j));
					i = arr.length;
					j = arr[0].length;
				}
			}
		}
		for (int i = arr.length - 1; i > 0; i--) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j]) {
					maxX = i;
					pts.add(new Point(i, j));
					j = arr[0].length;
					i = -1;

				}
			}
		}
		for (int j = 0; j < arr[0].length; j++) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i][j]) {
					maxY = j;
					pts.add(new Point(i, j));
					j = arr[0].length;
					i = arr.length;
				}
			}
		}
		for (int j = arr[0].length - 1; j > 0; j--) {
			for (int i = 0; i < arr.length; i++) {

				if (arr[i][j]) {
					minY = j;
					pts.add(new Point(i, j));
					j = -10;
					i = arr.length;
					j = -1;
				}
			}
		}

		// находим один конец
		if (arr[minX][minY]) {

			point1 = new Point(minX, minY);

		} else if (arr[maxX][maxY] && point1.isEmpty()) {

			point1 = new Point(maxX, maxY);

		} else if (arr[maxX][minY] && point1.isEmpty()) {

			point1 = new Point(maxX, minY);

		} else if (arr[minX][maxY] && point1.isEmpty()) {

			point1 = new Point(minX, maxY);

		}
		detectedPoints = pts;
		// после этой опреации в pts сотанутся только две "подозрительные" точки
		// и один из концов
		for (int i = 0; i < pts.size(); i++) {
			if (point1.x == pts.get(i).x && point1.y == pts.get(i).y) {

				pts.remove(i);
				i--;
			}
		}
		pts.add(point1);

		// если хотя бы для одной точки окажется, что она является концом
		// чего-либо,то выводим это в ответ
		for (int i = 0; i < pts.size(); i++) {
			for (int j = i + 1; j < pts.size(); j++) {
				if (check(arr, pts.get(j), pts.get(i)) != null) {

					return check(arr, point1, pts.get(i));
				}
			}
		}

		return null;

	}

	public static String check(boolean arr[][], Point point1, Point point2) {
		boolean arc1 = true;// concave(вогнутый)
		boolean arc2 = true;// convex (выпуклый)
		boolean line = true;
		int ll = Math
				.abs(lineValue(point1, point2, point1.x, point1.y) - lineValue(point1, point2, point2.x, point2.y));
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {

				if (arr[i][j]) {

					if (arc1 || arc2 || line) {

						if (lineValue(point1, point2, i, j) > ll / 10 || lineValue(point1, point2, i, j) < -(ll / 10)) {
							line = false;
						}
						if (lineValue(point1, point2, i, j) > ll / 10) {
							arc1 = false;
						}
						if (lineValue(point1, point2, i, j) < -(ll / 10)) {
							arc2 = false;
						}
					}

				}
			}
		}
		if ((arc1 && arc2 && line)) {
			System.out.println("Error");
		}
		if (line) {
			return "line";
		} else if (arc1 || arc2) {
			return "arc";
		}

		else {
			return null;
		}
	}

	public static int lineValue(Point point1, Point point2, int x, int y) {
		return (point1.y - point2.y) * x + (point2.x - point1.x) * y + (point1.x * point2.y - point2.x * point1.y);
	}

	public static String Circle(boolean arr[][]) {
		double truePix = 0;

		int xSum = 0;
		int ySum = 0;

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j]) {
					xSum += i;
					ySum += j;
					truePix++;
				}
			}
		}
		centreX = xSum / (int) truePix;
		centreY = ySum / (int) truePix;
		// Cirecle detection
		double R = 0;
		boolean Circle = true;

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j]) {

					if (R < Math.sqrt(Math.pow(centreX - i, 2) + Math.pow(centreY - j, 2))) { // first
																								// R
																								// counting

						R = Math.sqrt(Math.pow(centreX - i, 2) + Math.pow(centreY - j, 2)); // R
						// sqrt((x-x1)^2
						// +
						// (y-y1)^2

					}
					if (Math.sqrt(Math.pow(centreX - i, 2) + Math.pow(centreY - j, 2)) >= R + 3
							|| Math.sqrt(Math.pow(centreX - i, 2) + Math.pow(centreY - j, 2)) <= R - 3) {

						Circle = false;
						break;
					}

					j = arr[0].length + 1;
				}

			}
		} // detection high half
		int k = 0;
		for (int m = -1; m <= 1; m += 2) {
			for (int n = -1; n <= 1; n += 2) {
				for (int i = centreX; i * m < (1 + m) * arr.length / 2; i += m) {
					for (int j = centreY; j * n < (1 + n) * arr[0].length / 2; j += n) {

						if (arr[i][j]) {
							k++;
							i = arr.length * m;
							j = arr[0].length * n;

						}
					}
				}
			}
		}

		if (k < 3) {
			return null;
		}
		for (int i = 0; i < arr.length; i++) {
			for (int j = arr[0].length - 1; j > 0; j--) {
				if (arr[i][j]) {
					if (Math.sqrt(Math.pow(centreX - i, 2) + Math.pow(centreY - j, 2)) >= R + R / 3
							|| Math.sqrt(Math.pow(centreX - i, 2) + Math.pow(centreY - j, 2)) <= R - R / 3) {

						Circle = false;
						break;
					}

					j = -1;

				}
			}
		} // detection low half

		boolean fill = false;
		for (int i = centreX - (int) (R / 3); i < centreX + (R / 3); i++) {
			for (int j = centreY - (int) (R / 3); j < centreY + (R / 3); j++) {
				if (i > -1 && j > -1 && arr[i][j]) {
					fill = true;
					break;
				}

			}
			if (fill) {
				break;
			}
		}
		// circle detection ended
		if (Circle && fill) {
			detectedPoints.add(new Point(centreX, centreY));
			return "fill circle";
		} else if (Circle && !fill) {
			detectedPoints.add(new Point(centreX, centreY));
			return "empty circle";
		} else {
			return null;
		}
	}

	public static void movePoint(Point point, boolean src[][], boolean dst[][]) {
		src[point.x][point.y] = false;
		dst[point.x][point.y] = true;
	}

	public static ArrayList<boolean[][]> figureDetector(boolean arr[][]) {

		ArrayList<boolean[][]> figures = new ArrayList<boolean[][]>();
		while (true) {
			Point start = new Point(-1, -1);

			for (int i1 = 0; i1 < arr.length; i1++) {
				for (int j1 = 0; j1 < arr[0].length; j1++) {
					if (arr[i1][j1]) {
						start.x = i1;
						start.y = j1;
						break;
					}
				}
				if (!start.isEmpty()) {
					break;
				}
			}
			if (start.isEmpty())
				break;

			boolean[][] arrCopy = new boolean[arr.length][arr[0].length];
			movePoint(start, arr, arrCopy);
			Queue<Point> q = new LinkedList<>();
			q.add(start);
			int numPoints = 0;
			while (!q.isEmpty()) {
				Point current = q.remove();
				numPoints++;
				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						try {
							if (arr[current.x + dx][current.y + dy]) {
								Point neighbour = new Point(current.x + dx, current.y + dy);
								q.add(neighbour);
								movePoint(neighbour, arr, arrCopy);
							}
						} catch (IndexOutOfBoundsException e) {
						}
					}
				}
			}
			if (numPoints > 10) {
				figures.add(arrCopy);
			}
		}
		return figures;
	}

	public static boolean[][] imageToPixelArray(BufferedImage image) {
		boolean[][] pixArr = new boolean[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				int argb = image.getRGB(i, j);

				int red = (argb >> 16) & 0xff;
				int green = (argb >> 8) & 0xff;
				int blue = (argb) & 0xff;
				if (red + green + blue > 700) {
					pixArr[i][j] = false;
				} else {
					pixArr[i][j] = true;
				}
			}
		}
		return pixArr;
	}

	public static void main(String[] args) throws IOException {
		BufferedImage image = ImageIO.read(new File("10points.jpg"));
		PictureColorsInfo.pixSet(image);

		ArrayList<boolean[][]> colorsLevels = PictureColorsInfo.levelsColors(image);
		boolean[][] pixArr = imageToPixelArray(image);
		System.out.println(arc(pixArr));
		ArrayList<boolean[][]> figuresList = new ArrayList<boolean[][]>();
		figuresList = figureDetector(pixArr);
		for (int i = 0; i < figuresList.size(); i++) {
			System.out.println(arc(figuresList.get(i)));
		}
		System.out.println(figuresList.size() + " - different figures amount ");
		JFrame myWindow = new JFrame();
		myWindow.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final int size = figuresList.size();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (PictureColorsInfo.pixColorDetector(image.getRGB(i, j)) != 2) {
					image.setRGB(i, j, 16777215);
				}
			}
		}
		JPanel pane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {

				AffineTransform at = new AffineTransform();

				Graphics2D g2d = (Graphics2D) g;

				g2d.drawImage(image, at, null);

				g2d.setColor(Color.YELLOW);

				try {
					// retrieve image
					BufferedImage bi = image;
					File outputfile = new File("saved1.png");
					ImageIO.write(bi, "png", outputfile);
				} catch (IOException e) {

				}

			}
		};

		myWindow.setContentPane(pane);
		myWindow.setVisible(true);
	}

}
