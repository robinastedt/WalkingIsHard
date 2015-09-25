package com.astedt.robin.walkingishard.util;

public class BooleanMatrix {
    
    
    public static boolean[][] add(boolean[][] A, boolean[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bColumns) {
            throw new IllegalArgumentException("A:Columns: " + aColumns + " did not match B:Columns " + bColumns + ".");
        }
        if (aRows != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aRows + " did not match B:Rows " + bRows + ".");
        }
        
        boolean[][] C = new boolean[aRows][aColumns];
        
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                C[i][j] = A[i][j] || B[i][j];
            }
        }
        
        return C;
    }

    
    public static boolean[][] multiply(boolean[][] A, boolean[][] B) {

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Columns: " + aColumns + " did not match B:Rows " + bRows + ".");
        }

        boolean[][] C = new boolean[aRows][bColumns];
        

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] = C[i][j] || (A[i][k] && B[k][j]);
                }
            }
        }

        return C;
    }
    
    public static void print(boolean[][] A) {
        int aRows = A.length;
        int aColumns = A[0].length;
        
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                if (A[i][j]) System.out.print(1);
                else System.out.print(0);
                if (j < aColumns-1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }
    }
    
}