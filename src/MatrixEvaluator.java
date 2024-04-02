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
import java.nio.file.*;
import java.util.LinkedList;
import java.util.Scanner;

public class MatrixEvaluator {

    /**
     * readMatrixFile - Reads a matrix from a file and returns it as a single string.
     *
     * Prompt the user to input the name of the file containing the matrix, read the
     * matrix into a String, then remove all whitespace from the String.
     *
     * @param matrixString A string to store the matrix read from the file.
     * @param matrixFile The name or path of the file containing the matrix.
     * @return The matrix as a  string with whitespace removed.
     */
    public static String readMatrixFile(String matrixString, String matrixFile) {

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

    /**
     * isWalk - Determines if the given vertex sequence represents a walk, and if so if it is an open or closed walk.
     * If the vertex sequence is a walk, then other functions are called to determine if the walk is a trail, path,
     * circuit, or cycle.
     *
     * @param vertexSequence A string representing a sequence of vertices
     * @param matrix         A 2D array representing the adjacency matrix of the digraph
     */
    public static void isWalk(String vertexSequence, int[][] matrix) {

        // Evaluate if an edge exists for each pair of vertices in the
        // input sequence.
        boolean edgeExists = false;
        for (int i = 0; i < vertexSequence.length(); i++) {
            char comma = vertexSequence.charAt(i);
            if (comma == ',') {
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

        // If an edge exists for each pair of vertices in the input sequence then
        // evaluate if the walk is open or closed. If an open walk evaluate if it
        // is also a trail and path. If closed evaluate if it is also a circuit
        // or a cycle.
        if (edgeExists) {
            boolean openWalk = openOrClosedWalk(vertexSequence);
            lengthOfWalk(vertexSequence);
            boolean trailOrCircuit = isTrailOrCircuit(vertexSequence);
            boolean pathOrCycle = isPathOrCycle(vertexSequence);

            if (openWalk) {
                if (trailOrCircuit) {
                    System.out.println("It is a trail");
                    if (pathOrCycle) {
                        System.out.println("It is a path");
                    }
                }
            } else {
                if (trailOrCircuit) {
                    System.out.println("It is a circuit");
                    if (pathOrCycle) {
                        System.out.println("It is a cycle");
                    }
                }
            }
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

    /**
     * lengthOfWalk - Calculates the length of a walk based on a sequence of vertices.
     *
     * The length of the walk is determined by counting the number of commas in the String
     * representing the vertex sequence that is the walk, as the number of commas will be
     * equal to the number of edges.
     *
     * @param vertexSequence A string representing a sequence of vertices.
     */
    public static void lengthOfWalk(String vertexSequence) {
        int lengthOfWalk = 0;
        for (int i = 0; i < vertexSequence.length(); i++) {
            char c = vertexSequence.charAt(i);
            if (c == ',') {
                lengthOfWalk++;
            }
        }
        System.out.println("The length of the walk is " + lengthOfWalk);
    }

    /**
     * openOrClosedWalk - Determines whether a given sequence of vertices represents an open
     * or closed walk.
     *
     * @param vertexSequence A string representing a sequence of vertices.
     * @return {@code true} if the walk is open, or {@code false} if it is closed.
     */
    public static boolean openOrClosedWalk(String vertexSequence) {
        char firstVertex = vertexSequence.charAt(0);
        char lastVertex = vertexSequence.charAt(0);
        for (int i = 0; i < vertexSequence.length(); i++) {
            char c = vertexSequence.charAt(i);
            lastVertex = vertexSequence.charAt(i);
        }
        if (firstVertex != lastVertex) {
            System.out.println("It is an open walk");
            return true;
        } else {
            System.out.println("It is a closed walk");
            return false;
        }
    }

    /**
     * isTrailOrCircuit - Determines whether the given walk represents a trail or circuit.
     *
     * The function checks if an edge is repeated, if any edge is repeated then the walk is
     * neither a trail nor a circuit. If no edges are repeated then the walk is a trail (if
     * open) or a circuit (if closed).
     *
     * @param vertexSequence A string representing a sequence of vertices.
     * @return {@code true} if the walk does not repeat an edge, {@code false} otherwise
     */
    public static boolean isTrailOrCircuit(String vertexSequence) {
        boolean trailOrCircuit = true;
        LinkedList<String> edgesVisitedList = new LinkedList<>();
        for (int i = 0; i < vertexSequence.length(); i++) {
            char comma = vertexSequence.charAt(i);
            if (comma == ',') {
                char leftHandVertex = vertexSequence.charAt(i - 1);
                char rightHandVertex = vertexSequence.charAt(i + 1);
                String edgeVisited = leftHandVertex + "," + rightHandVertex;
                if (edgesVisitedList.contains(edgeVisited)) {
                    trailOrCircuit = false;
                    break;
                }
                edgesVisitedList.add(edgeVisited);
            }
        }
        return trailOrCircuit;
    }

    /**
     * isPathOrCycle - Determines whether the given vertex sequence represents a path or cycle.
     *
     * The function checks if a vertex is repeated (except for the first and last vertices being
     * the same). If a vertex is repeated then the walk is neither a path nor a cycle. If no
     * vertices are repeated, then the walk is a path (if open) or a cycle (if closed).
     *
     * @param vertexSequence A string representing a sequence of vertices
     * @return {@code true} if the walk does not repeat a vertex, {@code false} otherwise
     */
    public static boolean isPathOrCycle(String vertexSequence) {
        boolean pathOrCycle = true;
        LinkedList<Character> verticesVisitedList = new LinkedList<>();
        for (int i = 0; i < vertexSequence.length(); i++) {
            char vertex = vertexSequence.charAt(i);

            // For closed walks the first and last vertex will always be
            // the same. To avoid giving a false negative for this match
            // we will break the loop early for closed walks. Since an
            // open walk has a different beginning and ending vertex the
            // loop won't terminate early for an open walk.
            if (i == vertexSequence.length() - 1) {
                if (vertexSequence.charAt(0) == vertexSequence.charAt(i)) {
                    break;
                }
            }
            if (verticesVisitedList.contains(vertex)){
                pathOrCycle = false;
                break;
            }
            if (vertex != ',') {
                verticesVisitedList.add(vertex);
            }

        }
        return pathOrCycle;
    }

    /**
     * generateReflexiveClosure - Finds the reflexive closure of a given matrix
     * and returns the reflexive closure matrix as a 2D int array. This is
     * achieved by setting all entries in the main diagonal to "1".
     *
     * @param matrix The input matrix for which to find the reflexive closure
     * @return The reflexive closure of the input matrix as a 2D int array
     */
    public static int[][] generateReflexiveClosure(int[][] matrix) {
        int reflexiveRows = matrix.length;
        int reflexiveCol = matrix[0].length;
        int[][] reflexiveMatrix = new int[reflexiveRows][reflexiveCol];

        // Copy the matrix into the reflexiveMatrix
        for (int i = 0; i < reflexiveRows; i++) {
            for (int j = 0; j < reflexiveCol; j++) {
                reflexiveMatrix[i][j] = matrix[i][j];
            }
        }

        // Change the main diagonal to all 1's
        for (int i = 0; i < reflexiveRows; i++) {
            for (int j = 0; j < reflexiveCol; j++) {
                if (i == j) {
                    reflexiveMatrix[i][j] = 1;
                }
            }
        }
        return reflexiveMatrix;
    }

    /**
     * generateSymmetricMatrix - Finds the symmetric closure of a given matrix,
     * and returns the symmetric closure matrix as a 2D int array. This is done
     * by checking each index in the input matrix (as a 2D int array) such that
     * if index[i][j] == 1, then set index[j][i] = 1
     *
     * @param matrix The input matrix for which to find the symmetric closure
     * @return The symmetric closure of the input matrix as a 2D int array
     */
    public static int[][] generateSymmetricMatrix(int[][] matrix) {
        int symmetricRows = matrix.length;
        int symmetricCol = matrix[0].length;
        int[][] symmetricMatrix = new int[symmetricRows][symmetricCol];

        // Copy the matrix into the symmetricMatrix
        for (int i = 0; i < symmetricRows; i++) {
            for (int j = 0; j < symmetricCol; j++) {
                symmetricMatrix[i][j] = matrix[i][j];
            }
        }

        // Update the matrix so that for every ordered pair (i,j) = (j,i)
        for (int i = 0; i < symmetricRows; i++) {
            for (int j = 0; j < symmetricCol; j++) {
                if (symmetricMatrix[i][j] == 1) {
                    symmetricMatrix[j][i] = 1;
                }
            }
        }
        return symmetricMatrix;
    }

    /**
     * writeMatrixToFile - Writes a matrix to a file.
     *
     * @param matrix   The 2D int array representing the matrix.
     * @param fileName The name of the file to write the matrix to.
     */
    public static void writeMatrixToFile(int[][] matrix, String fileName) {
        int matrixRows = matrix.length;
        int matrixCol = matrix[0].length;

        // Delete the output file if it already exists. This is to prevent
        // the same file being appended to upon subsequent uses of the program
        // when testing the output.
        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        // Write the given matrix to the file
        try (BufferedWriter writeMatrix = new BufferedWriter(new FileWriter(fileName, true))) {
            for (int i = 0; i < matrixRows; i++) {
                for (int j = 0; j < matrixCol; j++) {
                    writeMatrix.write(matrix[i][j] + " ");
                }
                writeMatrix.newLine();
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void main(String args[]) {

        // Variables with properties of the matrix
        int matrixRows = 10;
        int matrixColumns = 10;
        int[][] matrix = new int[matrixRows][matrixColumns];
        String matrixString = null; // Used to convert the .txt file to a String

        // Prompt the user for the file
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the name of the file that has the matrix ");
        String fileName = userInput.nextLine();
        File matrixFile = new File(fileName);

        // Read the file containing the matrix into a String
        matrixString = readMatrixFile(matrixString, fileName);

        // Convert the String into a 2D int array
        convertStringTo2DIntArray(matrixRows, matrixColumns, matrix, matrixString);

        // Ask the user to input a sequence of vertices
        String vertexSequence = null;
        vertexSequence = getVertexSequence();

        // Determine if the sequence of vertices is a walk, and if so the length of the
        // walk, and what type of walk (open, closed, trail, path, circuit, cycle)
        isWalk(vertexSequence, matrix);

        // Find the various closures of the graph
        int[][] reflexiveMatrix = generateReflexiveClosure(matrix);
        int[][] symmetricMatrix = generateSymmetricMatrix(matrix);

        // Write the closures to a file
        String reflexiveFile = "reflexive_" + fileName;
        String symmetricFile = "symmetric_" + fileName;
        writeMatrixToFile(reflexiveMatrix, reflexiveFile);
        writeMatrixToFile(symmetricMatrix, symmetricFile);
        System.out.println("The reflexive closure can be found in " + reflexiveFile);
        System.out.println("The symmetrix closure can be found in " + symmetricFile);

    }


}
