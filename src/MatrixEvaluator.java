/**
 * MatrixEvaluator
 * @author Garrett W. Sangala
 * @version 0.1
 * @since 2024-03-25
 *
 * This Java program prompts a user for a file containing a boolean matrix, and then for a sequence of vertices.
 * The program then determines if and which of the following the given sequence of vertices represents:
 * -Open Walk
 * -Closed walk
 * -Trail
 * -Circuit
 * -Path
 * -Cycle
 *
 * The program will also generate the reflexive and symmetric closures of the input matrix and output the
 * matrix closures into a file for each.
 */

import java.io.*;
import java.util.Scanner;

public class MatrixEvaluator {

    public static void main(String args[]) {

        // Read the matrix from the file into a 2D array
        int matrixRows = 10;
        int matrixColumns = 10;
        int[][] matrix = new int[matrixRows][matrixColumns];
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the name of the file that has the matrix: ");
        String fileName = userInput.nextLine();
        File matrixFile = new File(fileName);
        String matrixString = null;
        StringBuilder buildMatrixString = new StringBuilder();

        BufferedReader matrixBuffered = null;


        try {
            matrixBuffered = new BufferedReader(new FileReader(matrixFile));

            String fileLine;
            while ((fileLine = matrixBuffered.readLine()) != null) {
                buildMatrixString.append(fileLine);
            }
            matrixString = buildMatrixString.toString(); // Convert the StringBuilder to a String
            matrixString = matrixString.replaceAll("\\s",""); // Remove all whitespace from the String

        // Should probably update the error catching a bit, but this will work for now
        } catch (IOException error) {
            error.printStackTrace();
        } finally {
            try {
                matrixBuffered.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        //Debug
        if (!matrixString.isEmpty()) {
            System.out.println(matrixString);
        }

        // Convert the String into a 2D int array
        int strIdx = 0;
        for (int i = 0; i < matrixRows; i++) {
            for (int j = 0; j < matrixColumns; j++) {
                matrix[i][j] = matrixString.charAt(strIdx) - '0';
                strIdx++;
            }
        }

        //Debug, print array
        for (int i = 0; i < matrixRows; i++) {
            for (int j = 0; j < matrixColumns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }




    }
}