/** 
 * @author Vasily Isaev
 * @version 7.20
 * @since 05.11.2018 
*/
/*	
 				Nussinov Algorithm
 	One of standart bioinformatic's algorithm that used to find optimal 2nd structure of RNA
 	
 	Time complexity - O(n^3) , n - RNA length
 	
 	input format :
 		RND_sequence
 		min. nucleotides pairs than can form a loop
 		 
 */
class Nussinov {

	static int WeightMatrix[][];
	static String sequence;

	// check for complexity nucleotids
	static boolean isPair(char x, char y) { 
		return ((x == 'A' && y == 'U') || (x == 'U' && y == 'A') || (x == 'C' && y == 'G') || (x == 'G' && y == 'C'));
	}

	static int NussAlg(int i, int j) {

		if (WeightMatrix[i][j] != -1)
			return WeightMatrix[i][j];

		if (i >= j) {
			WeightMatrix[i][j] = 0;
			return 0;
		}

		int max = 0;

		max = Math.max(max, Math.max(NussAlg(i + 1, j), NussAlg(i, j - 1)));

		if (isPair(sequence.charAt(i), sequence.charAt(j)))
			max = Math.max(max, NussAlg(i + 1, j - 1) + 1);

		for (int k = i + 1; k < j; k++)
			max = Math.max(max, NussAlg(i, k) + NussAlg(k + 1, j));

		WeightMatrix[i][j] = max;

		return max;
	}

	//trace back when calculate all the matrix
	private static String comeBack(int i, int j) {

		if (i == j)
			return Character.toString(sequence.charAt(i));

		if (i > j)
			return "";

		if (NussAlg(i, j) == NussAlg(i + 1, j))
			return sequence.charAt(i) + comeBack(i + 1, j);

		if (NussAlg(i, j) == NussAlg(i, j - 1))
			return comeBack(i, j - 1) + sequence.charAt(j);

		if (isPair(sequence.charAt(i), sequence.charAt(j)) && NussAlg(i, j) == NussAlg(i + 1, j - 1) + 1)
			return "(" + sequence.charAt(i) + comeBack(i + 1, j - 1) + sequence.charAt(j) + ")";

		for (int k = i + 1; k < j; k++) 
			if (NussAlg(i, j) == NussAlg(i, k) + NussAlg(k + 1, j))
				return comeBack(i, k) + comeBack(k + 1, j);
		

		return "comeBacking failed";
	}
	
	static void matrixInit(int minLength) {
		WeightMatrix = new int[sequence.toCharArray().length][sequence.toCharArray().length];
		//filling matrix with [-1]
		for (int i = 0; i < sequence.toCharArray().length; i++)
			for (int j = 0; j < sequence.toCharArray().length; j++)
				WeightMatrix[i][j] = -1;
		//filling diagonals elements with [0]
		for (int k = 0; k < minLength; k++)
			for (int i = 0; i < sequence.toCharArray().length - k; i++)
				WeightMatrix[i][i + k] = 0;
	}

	public static void main(String args[]) {
		sequence = args[0];
		matrixInit(Integer.valueOf(args[1]));
		System.out.println("for string " + sequence);
		System.out.println("Matched pairs: " + NussAlg(0, sequence.toCharArray().length - 1));
		System.out.println("Secondary Structure after folding: " + comeBack(0, sequence.toCharArray().length - 1));

		for (int i = 0; i < WeightMatrix.length; i++) {
			for (int j = 0; j < WeightMatrix.length; j++)
				System.out.print(WeightMatrix[i][j] + " ");
			System.out.println();
		}
	}
}