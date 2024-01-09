import java.util.Scanner;

public class ScoringMatrixGlobalAlignment{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input sequences from the user
        System.out.print("Enter seq 1: ");
        String sequence1 = sc.nextLine();

        System.out.print("Enter seq 2: ");
        String sequence2 = sc.nextLine();

        // Define scoring values
        int match = 1;
        int mismatch = -1;
        int gap = -2;

        // Perform global alignment
        AlignmentResult alignmentResult = globalAlignment(sequence1, sequence2, match, mismatch, gap);

        // Display alignment results
        System.out.println("Alignment Score: " + alignmentResult.score);
        System.out.println("Alignment 1: " + alignmentResult.alignment1);
        System.out.println("Alignment 2: " + alignmentResult.alignment2);

        // Close the scanner
        sc.close();
    }

    // Inner class to store alignment results
    public static class AlignmentResult {
        int score;
        String alignment1;
        String alignment2;

        public AlignmentResult(int score, String alignment1, String alignment2) {
            this.score = score;
            this.alignment1 = alignment1;
            this.alignment2 = alignment2;
        }
    }

    // Function for global sequence alignment
    public static AlignmentResult globalAlignment(String sequence1, String sequence2, int match, int mismatch, int gap) {
        // Initialize the dynamic programming matrix
        int[][] dp = new int[sequence1.length() + 1][sequence2.length() + 1];

        // Initialize the first row and column of the matrix
        for (int i = 0; i <= sequence1.length(); i++) {
            dp[i][0] = i * gap;
        }
        for (int j = 0; j <= sequence2.length(); j++) {
            dp[0][j] = j * gap;
        }

        // Fill in the dynamic programming matrix
        for (int i = 1; i <= sequence1.length(); i++) {
            for (int j = 1; j <= sequence2.length(); j++) {
                // Calculate scores for diagonal, vertical, and horizontal movements
                int matchOrMismatch = sequence1.charAt(i - 1) == sequence2.charAt(j - 1) ? match : mismatch;
                dp[i][j] = Math.max(
                    dp[i - 1][j - 1] + matchOrMismatch,
                    Math.max(
                        dp[i - 1][j] + gap,
                        dp[i][j - 1] + gap
                    )
                );
            }
        }

        // Display the alignment matrix
        System.out.println("Matrix:");
        for (int i = 0; i <= sequence1.length(); i++) {
            for (int j = 0; j <= sequence2.length(); j++) {
                System.out.print(dp[i][j] + "\t");
            }
            System.out.println();
        }

        // Perform traceback to get aligned sequences
        return traceback(sequence1, sequence2, dp, match, mismatch, gap);
    }

    // Function for traceback to get aligned sequences
    public static AlignmentResult traceback(String sequence1, String sequence2, int[][] dp, int match, int mismatch, int gap) {
        int i = sequence1.length();
        int j = sequence2.length();
        StringBuilder alignment1 = new StringBuilder();
        StringBuilder alignment2 = new StringBuilder();

        while (i > 0 || j > 0) {
            int diagonalScore = (i > 0 && j > 0) ? dp[i - 1][j - 1] : Integer.MIN_VALUE;
            int upScore = (i > 0) ? dp[i - 1][j] : Integer.MIN_VALUE;
            int leftScore = (j > 0) ? dp[i][j - 1] : Integer.MIN_VALUE;

            if (i > 0 && j > 0 && sequence1.charAt(i - 1) == sequence2.charAt(j - 1)) {
                alignment1.insert(0, sequence1.charAt(i - 1));
                alignment2.insert(0, sequence2.charAt(j - 1));
                i--;
                j--;
            } else if (diagonalScore >= upScore && diagonalScore >= leftScore) {
                alignment1.insert(0, sequence1.charAt(i - 1));
                alignment2.insert(0, sequence2.charAt(j - 1));
                i--;
                j--;
            } else if (upScore >= leftScore) {
                alignment1.insert(0, sequence1.charAt(i - 1));
                alignment2.insert(0, '-');
                i--;
            } else {
                alignment1.insert(0, '-');
                alignment2.insert(0, sequence2.charAt(j - 1));
                j--;
            }
        }

        // Return the alignment result
        return new AlignmentResult(dp[sequence1.length()][sequence2.length()], alignment1.toString(), alignment2.toString());
    }
}
