package br.unesp.rc.junit5tutorial;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VectorEqualTest {
    private static int[] v1;
    private static int[] v2;

    public VectorEqualTest() { 
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
    void testEqualVectors() {
        System.out.println("Os valores são iguais?");
        boolean expResult = true;
        boolean result = Vector.equal(v1, v2);
        assertEquals(expResult, result);
    }

    @Test
    void testNullParameter() {
        // Arrange
        v1 = null;

        // Act & assert
        assertThrows(IllegalArgumentException.class, () -> {
            Vector.equal(v1, v2);
        });
    }

    @Test
    void testEqual_DifferentVectors() {
        boolean expResult = false;

        int[] diffVector = {1, 2, 4};
        v1 = diffVector;

        boolean result = Vector.equal(v1, v2);
        assertEquals(expResult, result);
    }
}
