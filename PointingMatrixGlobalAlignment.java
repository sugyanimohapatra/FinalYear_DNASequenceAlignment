import java.util.Scanner;

public class PointingMatrixGlobalAlignment {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input sequences from the user
        System.out.print("Enter the first sequence: ");
        String seq1 = scanner.nextLine();
        System.out.print("Enter the second sequence: ");
        String seq2 = scanner.nextLine();

        // Define scoring values
        int match = 1;
        int mismatch = -1;
        int gap = -2;
        int diagonal = 2;
        int vertical = 3;
        int horizontal = 1;

        // Create two matrices to store alignment scores and pointing directions
        int[][] alignmentMatrix = new int[seq1.length() + 1][seq2.length() + 1];
        int[][] pointingMatrix = new int[seq1.length() + 1][seq2.length() + 1];

        // Initialize the first row and first column of the alignment matrix and pointing matrix
        for (int i = 0; i <= seq1.length(); i++) {
            alignmentMatrix[i][0] = i * gap;
            pointingMatrix[i][0] = vertical;  // Vertical direction
        }
        for (int j = 0; j <= seq2.length(); j++) {
            alignmentMatrix[0][j] = j * gap;
            pointingMatrix[0][j] = horizontal;  // Horizontal direction
        }

        // Fill in the alignment matrix and pointing matrix
        for (int i = 1; i <= seq1.length(); i++) {
            for (int j = 1; j <= seq2.length(); j++) {
                // Calculate scores for diagonal, vertical, and horizontal movements
                int matchOrMismatch = seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch;
                int scoreDiagonal = alignmentMatrix[i - 1][j - 1] + matchOrMismatch;
                int scoreVertical = alignmentMatrix[i - 1][j] + gap;
                int scoreHorizontal = alignmentMatrix[i][j - 1] + gap;

                // Determine the direction (diagonal, vertical, or horizontal) that maximizes the score
                if (scoreDiagonal >= scoreVertical && scoreDiagonal >= scoreHorizontal) {
                    alignmentMatrix[i][j] = scoreDiagonal;
                    pointingMatrix[i][j] = diagonal;  // Diagonal direction
                } else if (scoreVertical >= scoreDiagonal && scoreVertical >= scoreHorizontal) {
                    alignmentMatrix[i][j] = scoreVertical;
                    pointingMatrix[i][j] = vertical;  // Vertical direction
                } else {
                    alignmentMatrix[i][j] = scoreHorizontal;
                    pointingMatrix[i][j] = horizontal;  // Horizontal direction
                }
            }
        }

        // Traceback to find aligned sequences
        StringBuilder alignedSeq1 = new StringBuilder();
        StringBuilder alignedSeq2 = new StringBuilder();
        int i = seq1.length();
        int j = seq2.length();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && pointingMatrix[i][j] == diagonal) {
                // Diagonal movement
                alignedSeq1.insert(0, seq1.charAt(i - 1));
                alignedSeq2.insert(0, seq2.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && pointingMatrix[i][j] == vertical) {
                // Vertical movement
                alignedSeq1.insert(0, seq1.charAt(i - 1));
                alignedSeq2.insert(0, '-');
                i--;
            } else {
                // Horizontal movement
                alignedSeq1.insert(0, '-');
                alignedSeq2.insert(0, seq2.charAt(j - 1));
                j--;
            }
        }

        // Print pointing matrix
        System.out.println("\nPointing Matrix:");
        for (int row = 0; row <= seq1.length(); row++) {
            for (int col = 0; col <= seq2.length(); col++) {
                System.out.printf("%2d  ", pointingMatrix[row][col]);
            }
            System.out.println();
        }

        // Print aligned sequences
        System.out.println("\nAligned Sequence 1: " + alignedSeq1.toString());
        System.out.println("Aligned Sequence 2: " + alignedSeq2.toString());

        // Calculate and display the alignment score
        int alignmentScore = alignmentMatrix[seq1.length()][seq2.length()];
        System.out.println("\nAlignment Score: " + alignmentScore);
    }
}
