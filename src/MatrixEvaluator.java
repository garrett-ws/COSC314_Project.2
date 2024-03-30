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

        BufferedReader matrixInput = null;
        try {
            matrixInput = new BufferedReader(new FileReader(matrixFile));

            int fileChar;
            int row = 0;
            int col = 0;
            while ((fileChar = matrixInput.read()) != -1) {
                char matrixChar = (char) fileChar;
                if (matrixChar != '\n' && matrixChar != '\r') {
                    matrix[row][col] = Character.getNumericValue(matrixChar);
                    col++;
                    if (col >= matrixColumns) {
                        col = 0;
                        row++;
                        // Check if the row exceeds the matrixRows after incrementing
                        if (row >= matrixRows) {
                            // Handle the case where the matrix is larger than expected
                            System.out.println("Matrix size exceeds expected dimensions.");
                            break;
                        }
                    }
                }
            }

        // Should probably update the error catching a bit, but this will work for now
        } catch (IOException error) {
            error.printStackTrace();
        } finally {
            try {
                matrixInput.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        //Debug
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();

        }



    }
}