/** 
 * @author Vasily Isaev
 * @version 7.20
 * @since 09.11.2018 
*/
class UPGMA {
	public static class NumPair {
		NumPair(int x1, int y1) {
			x = x1;
			y = y1;
		}

		int x;
		int y;
	}
	static int lettersAmount(String s) {
		int k = 0;
		for (int i = 0; i < s.length(); i++)
			if (((s.charAt(i) >= 'a') && (s.charAt(i) <= 'z')) || ((s.charAt(i) >= 'A') && (s.charAt(i) <= 'Z')))
				k++;
		return k;

	}
	static double prev = 0;
	static double[][] matrix = { { 0, 16, 16, 10 }, { 0, 0, 8, 8 }, { 0, 0, 0, 4 }, { 0, 0, 0, 0 }, };

	// static double[][] matrix = { { 0, 5, 4, 7, 6, 8 }, { 5, 0, 7, 10, 9, 11 }, {
	 //4, 7, 0, 7, 6, 8 },
	 //{ 7, 10, 7, 0, 5, 9 }, { 6, 9, 6, 5, 0, 8 }, { 8, 11, 8, 9, 8, 0 } };
	static String[] letters;
	static double[] dist ;

	static NumPair minC() {
		NumPair min = new NumPair(0, 1);
		for (int i = 0; i < matrix.length; i++)
			for (int j = i + 1; j < matrix[0].length; j++)
				if (matrix[i][j] < matrix[min.x][min.y])
					min = new NumPair(i, j);
		return min;
	}

	static double[][] columnMerge(NumPair p) {
		double[][] merged = new double[matrix.length - 1][matrix[0].length - 1];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (i > p.y && j > p.y) {
					merged[i - 1][j - 1] = matrix[i][j];
				} else if (i > p.y || j > p.y) {
					if (i > p.y && j != p.y)
						merged[i - 1][j] = matrix[i][j];
					if (j > p.y && i != p.y)
						merged[i][j - 1] = matrix[i][j];
				} else if (i < p.y && j < p.y) {
					merged[i][j] = matrix[i][j];
				}
			}
		}
		
		for (int i = 0; i < merged.length; i++) {
			merged[p.x][i] = (matrix[p.x][i + (i >= p.y ? 1 : 0)] * lettersAmount(letters[p.x])
					+ matrix[p.y][i + (i >= p.y ? 1 : 0)] * lettersAmount(letters[p.y]))
					/ (lettersAmount(letters[p.x]) + lettersAmount(letters[p.y]));
			merged[i][p.x] = (matrix[i + (i >= p.y ? 1 : 0)][p.x] * lettersAmount(letters[p.y])
					+ matrix[i + (i >= p.y ? 1 : 0)][p.y] * lettersAmount(letters[p.x]))
					/ (lettersAmount(letters[p.x]) + lettersAmount(letters[p.y]));
		}
		merged[p.x][p.x] = 0;
		String[] res = new String[letters.length - 1];
		double[] r = new double[letters.length - 1];

		letters[p.x] = "(" + letters[p.x] + ":" + (matrix[p.x][p.y] - dist[p.x]) / 2 + ", " + letters[p.y] + ":"
				+(matrix[p.x][p.y] - dist[p.y]) / 2 + ")";
		dist[p.x] += (matrix[p.x][p.y] - dist[p.x]);
		for (int i = 0; i < p.y; i++) {
			res[i] = letters[i];
			r[i] = dist[i];
		}
		for (int i = p.y; i < res.length; i++) {
			res[i] = letters[i + 1];
			r[i] = dist[i + 1];
		}
		letters = res;
		dist = r;
		return merged;
	}

	static void print(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++)
				System.out.print(matrix[i][j] + (matrix[i][j] % 10 == matrix[i][j] ? " " : "") + " ");
			System.out.println();

		}
	}

	public static void main(String[] args) {
		letters = new String[matrix.length];
		dist = new double[letters.length];
		char l = 'A';
		for (int i = 0; i < letters.length; i++)
			letters[i] = "" + l++;
		while (matrix.length != 1) {
			matrix = columnMerge(minC());
		}
		System.out.println(letters[0]);
			}
}
