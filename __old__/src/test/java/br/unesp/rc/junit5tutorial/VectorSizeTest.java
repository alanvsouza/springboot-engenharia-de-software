package br.unesp.rc.junit5tutorial;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VectorSizeTest {
    private static int[] v1;
    private static int[] v2;

    public VectorSizeTest() { 
    }

    @BeforeEach
    void init() {
        System.out.println("Inicializando os vetores");
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};

        v1 = new int[3];
        v1 = a;
        v2 = new int[3];
        v2 = b;
    }

    @AfterEach
    void tearDownAll() {
        v1 = null;
        v2 = null;
    }

    @Test
    void testEqualSizes() {
        System.out.println("Os vetores são do mesmo tamanho?");
        boolean expResult = true;
        boolean result = Vector.size(v1.length, v2.length);
        assertEquals(expResult, result);
    }

    @Test
    void testDifferentSizes() {
        boolean expResult = false;
        int[] biggerVector = {1, 2, 3, 4};
        v1 = biggerVector;

        boolean result = Vector.size(v1.length, v2.length);
        assertEquals(expResult, result);
    }

    @Test
    void testNegativeLength() {
        // Arrange
        int a = -1;
        int b = v2.length;

        // Act & assert
        assertThrows(IllegalArgumentException.class, () -> {
            Vector.size(a, b);
        });
    }
}
