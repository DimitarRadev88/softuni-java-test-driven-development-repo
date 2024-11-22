package com.softuni.lab;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductTest {

    private static final String PRODUCT_LABEL = "Label";
    private static final double PRODUCT_PRICE = 10.00;
    private static final int PRODUCT_QUANTITY = 5;
    private static final String PRODUCT_LABEL_SET = "New label set";
    private static final double PRODUCT_PRICE_SET = 20.00;
    private static final double PRICE_DELTA = 0.001;
    public static final int PRODUCT_SET_QUANTITY = 10;
    private Product product;

    @Before
    public void init() {
        this.product = new Product(PRODUCT_LABEL, PRODUCT_PRICE, PRODUCT_QUANTITY);
    }

    @Test
    public void testConstructorCreates() {
        Product product = new Product(PRODUCT_LABEL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        assertEquals("wrong label", PRODUCT_LABEL, product.getLabel());
        assertEquals("wrong price", PRODUCT_PRICE, product.getPrice(), PRICE_DELTA);
        assertEquals("wrong quantity", PRODUCT_QUANTITY, product.getQuantity());
    }

    @Test
    public void testSetLabelShouldSet() {
        product.setLabel(PRODUCT_LABEL_SET);
        assertEquals(PRODUCT_LABEL_SET, product.getLabel());
    }

    @Test
    public void testSetPriceShouldSet() {
        product.setPrice(PRODUCT_PRICE_SET);
        assertEquals(PRODUCT_PRICE_SET, product.getPrice(), PRICE_DELTA);
    }


    @Test
    public void testSetQuantityShouldSet() {
        product.setQuantity(PRODUCT_SET_QUANTITY);
        assertEquals(PRODUCT_SET_QUANTITY, product.getQuantity());
    }

    @Test
    public void testCompareTo() {
        Product leftProduct = new Product("Abc", 5.00, 5);
        Product rightProduct = new Product("Bcd", 5.00, 5);

        assertEquals(-1, leftProduct.compareTo(rightProduct));
        assertEquals(1, rightProduct.compareTo(leftProduct));

        rightProduct = new Product("Abc", 6.00, 5);

        assertEquals(-1, leftProduct.compareTo(rightProduct));
        assertEquals(1, rightProduct.compareTo(leftProduct));

        rightProduct = new Product("Abc", 5.00, 10);

        assertEquals(-1, leftProduct.compareTo(rightProduct));
        assertEquals(1, rightProduct.compareTo(leftProduct));

        rightProduct = new Product("Abc", 5.00, 5);

        assertEquals(0, leftProduct.compareTo(rightProduct));
    }

    @Test
    public void testEqualsReturnsTrueForProductsWithSameFieldValues() {
        Product sameFieldsProduct = new Product(PRODUCT_LABEL, PRODUCT_PRICE, PRODUCT_QUANTITY);

        assertTrue(product.equals(sameFieldsProduct));
    }

    @Test
    public void testHashCodeReturnsCorrectHash() {
        int hash = Objects.hash(PRODUCT_LABEL, PRODUCT_PRICE, PRODUCT_QUANTITY);

        assertEquals(hash, product.hashCode());
    }


}
