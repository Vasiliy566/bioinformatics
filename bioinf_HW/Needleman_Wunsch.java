/**
 * 
 * @author Vasily Isaev
 * @version 7.20
 * @since 10.10.2018
 */
/*
 * The Needleman–Wunsch algorithm is an algorithm used in bioinformatics to
 * align protein or nucleotide sequences. It was one of the first applications
 * of dynamic programming to compare biological sequences. The algorithm was
 * developed by Saul B. Needleman and Christian D. Wunsch and published in 1970.
 * The algorithm essentially divides a large problem (e.g. the full sequence)
 * into a series of smaller problems and uses the solutions to the smaller
 * problems to reconstruct a solution to the larger problem. It is also
 * sometimes referred to as the optimal matching algorithm and the global
 * alignment technique. The Needleman–Wunsch algorithm is still widely used for
 * optimal global alignment, particularly when the quality of the global
 * alignment is of the utmost importance.
 * 
 * input format
 * sequence_1
 * sequence_2
 */
public class Needleman_Wunsch {
	static int match = 1;
	static int mismatch = -1;
	static int gap = -1;

	static int matchScore(char a, char b, int match, int mismatch) {
		if (a == b) {
			return match;
		} else {
			return mismatch;
		}

	}

	static void fillingMatrix(int[][] weight_matrix, String s1, String s2) {
		// filling border with (-1*i)
		for (int i = 0; i < weight_matrix.length; i++) {
			weight_matrix[i][0] = -1 * i;
		}
		for (int i = 0; i < weight_matrix[0].length; i++) {
			weight_matrix[0][i] = -1 * i;
		}
		// fillint weights table
		for (int i = 1; i < weight_matrix.length; i++) {
			for (int j = 1; j < weight_matrix[0].length; j++) {
				int rMatch = weight_matrix[i - 1][j - 1]
						+ matchScore(s1.charAt(i - 1), s2.charAt(j - 1), match, mismatch);
				weight_matrix[i][j] = Math.max(Math.max(weight_matrix[i][j - 1] + gap, weight_matrix[i - 1][j] + gap),
						rMatch);
			}
		}
	}

	static void findTheWay(int[][] weight_matrix, String s1, String s2, String alignedSeq1, String alignedSeq2) {
		int i = s1.length();
		int j = s2.length();

		while ((i > 0) && (j > 0)) {
			int current = weight_matrix[i][j];
			int diagonal = weight_matrix[i - 1][j - 1];
			int up = weight_matrix[i - 1][j];
			int left = weight_matrix[i][j - 1];
			if (current == up + gap) {
				alignedSeq2 += s2.charAt(i - 1);
				alignedSeq1 += '-';
				i--;

			} else if (current == diagonal + matchScore(s1.charAt(j - 1), s2.charAt(i - 1), match, mismatch)) {
				alignedSeq1 += s1.charAt(j - 1);
				alignedSeq2 += s2.charAt(i - 1);
				i--;
				j--;

			} else if (current == left + gap) {
				alignedSeq1 += s1.charAt(j - 1);
				alignedSeq2 += '-';
				j--;

			}
		}

		while (i > 0) {
			
			alignedSeq2 += s2.charAt(i - 1);
			alignedSeq1 += '-';
			i--;
		}
		while (j > 0) {
			alignedSeq1 += s1.charAt(j - 1);
			alignedSeq2 += '-';
			j--;
		}
	}

	public static void main(String[] args) {
		String s1 = args[0];
		String s2 = args[1];

		int weight_matrix[][] = new int[s1.length() + 1][s2.length() + 1];

		fillingMatrix(weight_matrix, s1, s2);

		String alignedSeq1 = "";
		String alignedSeq2 = "";
		findTheWay(weight_matrix, s1, s2, alignedSeq1, alignedSeq1);

		/// out
		System.out.println("first sequence : " + s1);
		System.out.println("first sequence : " + s2);
		System.out.println("Gap = " + gap);
		System.out.println("Match = " + match);
		System.out.println("Mismatch = " + mismatch);
		System.out.println("-------------------------------");
		System.out.println(alignedSeq1);
		System.out.println(alignedSeq2);
	}

}
