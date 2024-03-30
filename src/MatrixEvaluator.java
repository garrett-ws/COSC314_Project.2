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
import java.util.LinkedList;
import java.util.Scanner;

public class MatrixEvaluator {

    /**
     * readMatrixFile - Reads a matrix from a file and returns it as a single string.
     *
     * Prompt the user to input the name of the file containing the matrix, read the
     * matrix into a String, then remove all whitespace from the String.
     *
     * @param matrixString A string to store the matrix read from the file
     * @return The matrix as a  string with whitespace removed
     */
    public static String readMatrixFile(String matrixString) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the name of the file that has the matrix ");
        String fileName = userInput.nextLine();
        File matrixFile = new File(fileName);

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
        return matrixString;
    }

    /**
     * convertStringTo2DIntArray - Converts a string representation of a matrix into a 2D integer array.
     *
     * @param matrixRows    The number of rows in the matrix.
     * @param matrixColumns The number of columns in the matrix.
     * @param matrix        The 2D integer array to store the converted matrix.
     * @param matrixString  The string representation of the matrix.
     */
    public static void convertStringTo2DIntArray(int matrixRows, int matrixColumns, int[][] matrix, String matrixString) {
        int strIdx = 0;
        for (int i = 0; i < matrixRows; i++) {
            for (int j = 0; j < matrixColumns; j++) {
                matrix[i][j] = matrixString.charAt(strIdx) - '0';
                strIdx++;
            }
        }
    }

    /**
     * getVertexSequence - Get the sequence of vertices from the user.
     *
     * @return The sequence of vertices entered by the user
     */
    public static String getVertexSequence() {
        // Ask the user to input a sequence of vertices
        System.out.println("Enter a sequence of vertices using the letters 'a' through 'j' " +
                "separated by commas (no space in between)");
        Scanner vertexScanner = new Scanner(System.in);
        return vertexScanner.nextLine();
    }

    public static void isWalk(String vertexSequence, int[][] matrix) {

        boolean edgeExists = false;
        for (int i = 0; i < vertexSequence.length(); i++) {
            char c = vertexSequence.charAt(i);
            if (c == ',') {
                char leftHandVertex = vertexSequence.charAt(i - 1);
                char rightHandVertex = vertexSequence.charAt(i + 1);
                int leftVertexIndex = vertexToIntIndex(leftHandVertex);
                int rightVertexIndex = vertexToIntIndex(rightHandVertex);
                edgeExists = matrix[leftVertexIndex][rightVertexIndex] == 1;

                // If a single edge doesn't exist in the vertex sequence then
                // it is not a walk and we can break out of the for loop
                if (edgeExists == false) {
                    System.out.println("This is not a walk");
                    break;
                }
            }
        }

        if (edgeExists) {
            int lengthOfWalk = 0;
            for (int i = 0; i < vertexSequence.length(); i++) {
                char c = vertexSequence.charAt(i);
                if (c == ',') {
                    lengthOfWalk++;
                }
            }
            System.out.println("The length of the walk is " + lengthOfWalk);
        }





    }

    /**
     * vertexToIntIndex - Converts a vertex char ('a' through 'j') to its corresponding int
     * index (0 through 9). If the given vertex character is not within the valid range then
     * return 0. The int index will be used to see if an edge exists between two vertices.
     *
     * @param vertex The vertex character to convert.
     * @return The integer index corresponding to the vertex character.
     */
    public static int vertexToIntIndex(char vertex) {
        int index;
        switch (vertex) {
            case 'a':
                index = 0;
                break;
            case 'b':
                index = 1;
                break;
            case 'c':
                index = 2;
                break;
            case 'd':
                index = 3;
                break;
            case 'e':
                index = 4;
                break;
            case 'f':
                index = 5;
                break;
            case 'g':
                index = 6;
                break;
            case 'h':
                index = 7;
                break;
            case 'i':
                index = 8;
                break;
            case 'j':
                index = 9;
                break;
            default:
                index = 0;
        }
        return index;
    }

    public static void main(String args[]) {

        // Variables with properties of the matrix
        int matrixRows = 10;
        int matrixColumns = 10;
        int[][] matrix = new int[matrixRows][matrixColumns];
        String matrixString = null; // Used to convert the .txt file to a String

        // Read the file containing the matrix into a String
        matrixString = readMatrixFile(matrixString);

        // Convert the String into a 2D int array
        convertStringTo2DIntArray(matrixRows, matrixColumns, matrix, matrixString);

        // Ask the user to input a sequence of vertices
        String vertexSequence = null;
        vertexSequence = getVertexSequence();
        System.out.println(vertexSequence); //Debug line

        // Determine if the sequence of vertices is a walk, and if so the length of the
        // walk, and what type of walk (open, closed, trail, path, circuit, cycle)
        isWalk(vertexSequence, matrix);

        // Find the various closures of the graph

    }
}
