import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AssignmentOne {
    public static void main(String args[]) {
        try {
            File file = new File(args[0]);
            Scanner scanner = new Scanner(file);
            int n = scanner.nextInt();
            int k = scanner.nextInt();
            int[][] numberTable = new int[3][n];
            int[] finalCut = new int[n];
            for (int i = 0; i < n; i++) {
                numberTable[0][i] = scanner.nextInt();
                finalCut[i] = numberTable[0][i];
            }
            scanner.close();
            populateFirstK(numberTable, k);
            calculateMaximumTotal(numberTable, k, n, finalCut);
            printResults(numberTable, finalCut, n);
            //Prove the results
            verifyFinalCut(finalCut);
            verifyMatrix(n, numberTable);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void populateFirstK(int[][] matrix, int k) {
        matrix[1][0] = matrix[0][0];
        for (int i = 1; i < k; i++) {
            matrix[1][i] = matrix[0][i] + matrix[1][i - 1];
            matrix[2][i] = matrix[1][i - 1];
        }
    }

    private static void calculateMaximumTotal(int[][] matrix, int k, int n, int[] finalCut) {
        int current = k;
        ArrayList<Integer> maximums = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            maximums.add(0);
        }
        maximums.add(0);
        while (current < n) {
            matrix[2][current] = matrix[1][current - 1];
            populateMaximums(matrix, k, maximums, current);
            //if previous is larger than all three at this current
            if (maximums.indexOf(maximums.get(k)) == maximums.indexOf(Collections.max(maximums))) {
                matrix[1][current] = matrix[1][current - 1];
                finalCut[current] = 0;
            } else {
                int index;
                matrix[1][current] = Collections.max(maximums);
                int occurrences = 0;
                for (int i = 0; i < k + 1; i++) {
                    occurrences = Collections.frequency(maximums, Collections.max(maximums));
                }
                if (occurrences == 1) {
                    index = maximums.indexOf(Collections.max(maximums));
                    finalCut[current - (k - index)] = 0;
                }
            }
            current++;
        }
    }


    private static void populateMaximums(int[][] matrix, int k, ArrayList<Integer> maximums, int current) {
        int z = 0;
        int r = k - 1;
        while (z < k) {
            int total = 0;
            total = recursivelyCalculateOptimals(matrix, k, total, current, r - z, z);
            maximums.set(z, total);
            z++;
        }
        maximums.set(k, matrix[1][current - 1]);
    }

    private static int recursivelyCalculateOptimals(int[][] matrix, int k, int total, int current, int r, int z) {
        if (r == 0) {
            total += matrix[2][current - (k - z)] + matrix[0][current];
            return total;
        }
        total += matrix[0][current - (k - (k - r))];
        return recursivelyCalculateOptimals(matrix, k, total, current, r - 1, z);
    }

    private static void printResults(int[][] numberTable, int[] finalCut, int n) {
        System.out.println(numberTable[1][n - 1]);
        for (int i = 0; i < finalCut.length; i++) {
            if (finalCut[i] > 0) {
                System.out.print(finalCut[i] + " ");
            }
        }
        System.out.println();
    }

    private static void verifyFinalCut(int[] finalCut) {
        System.out.println("FinalCut");
        System.out.print("|");
        for (int j = 0; j < finalCut.length; j++) {
            System.out.print(finalCut[j] + "|");
        }
        System.out.println();
    }

    private static void verifyMatrix(int n, int[][] numberTable) {
        System.out.println("Matrix");
        System.out.print("|");
        for (int j = 0; j < n; j++) {
            System.out.print(numberTable[0][j] + "|");
        }
        System.out.println();
        System.out.print("|");
        for (int j = 0; j < n; j++) {
            System.out.print(numberTable[1][j] + "|");
        }
        System.out.println();
        System.out.print("|");
        for (int j = 0; j < n; j++) {
            System.out.print(numberTable[2][j] + "|");
        }
        System.out.println();
    }
}