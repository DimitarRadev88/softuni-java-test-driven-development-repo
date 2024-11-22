package com.softuni.lab;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

public class InstockTest {

    private static final Product FIRST_PRODUCT = new Product("First", 1.00, 1);
    private static final Product SECOND_PRODUCT = new Product("Second", 2.00, 2);
    private static final Product THIRD_PRODUCT = new Product("Third", 3.00, 3);
    private static final Product[] PRODUCTS = {FIRST_PRODUCT, SECOND_PRODUCT, THIRD_PRODUCT};
    private static final int FIRST_PRODUCT_ADD_QUANTITY = 5;
    private static final int FIRST_PRODUCT_NEW_QUANTITY = FIRST_PRODUCT.getQuantity() + FIRST_PRODUCT_ADD_QUANTITY;
    private static final double SAME_PRICE_FOR_PRODUCT = 10.00;
    private static final int EXPECTED_COUNT_AFTER_FIRST_ADD = 1;
    private static final int EXPECTED_COUNT_AFTER_SECOND_ADD = 2;
    private static final String MISSING_PRODUCT_LABEL = "Missing label";
    private static final int SAME_QUANTITY = 10;
    private Instock instock;


    @Before
    public void init() {
        instock = new Instock();
    }

    @Test
    public void testGetCountShouldReturnCount() {
        assertEquals(0, instock.getCount());
        addProducts(PRODUCTS);
        assertEquals(PRODUCTS.length, instock.getCount());
    }

    @Test
    public void addShouldIncreaseProductCount() {
        instock.add(FIRST_PRODUCT);
        assertEquals("wrong product count", EXPECTED_COUNT_AFTER_FIRST_ADD, instock.getCount());
        instock.add(SECOND_PRODUCT);
        assertEquals("wrong product count", EXPECTED_COUNT_AFTER_SECOND_ADD, instock.getCount());
    }

    @Test
    public void testContainsShouldReturnTrueIfProductInStock() {
        instock.add(FIRST_PRODUCT);
        assertTrue(instock.contains(FIRST_PRODUCT));
    }

    @Test
    public void testContainsShouldReturnFalseIfProductNotInStock() {
        assertFalse(instock.contains(FIRST_PRODUCT));
    }

    @Test
    public void testChangeQuantityShouldChangeProductQuantity() {
        instock.add(FIRST_PRODUCT);
        instock.changeQuantity(FIRST_PRODUCT.label, FIRST_PRODUCT_ADD_QUANTITY);
        assertEquals(FIRST_PRODUCT_NEW_QUANTITY, FIRST_PRODUCT.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeQuantityThrowsWhenProductNotInStock() {
        instock.changeQuantity(FIRST_PRODUCT.getLabel(), FIRST_PRODUCT_NEW_QUANTITY);
    }

    @Test
    public void testFindShouldReturnNthAddedProduct() {
        addProducts(PRODUCTS);
        assertEquals(FIRST_PRODUCT, instock.find(0));
        assertEquals(SECOND_PRODUCT, instock.find(1));
        assertEquals(THIRD_PRODUCT, instock.find(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindShouldThrowIfIndexIsNegative() {
        instock.find(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindShouldThrowIfIndexIsLargerThanOrEqualToCount() {
        addProducts();
        instock.find(instock.getCount());
    }

    @Test
    public void testFindByLabelShouldReturnProduct() {
        addProducts(PRODUCTS);
        assertEquals(FIRST_PRODUCT, instock.findByLabel(FIRST_PRODUCT.getLabel()));
        assertEquals(SECOND_PRODUCT, instock.findByLabel(SECOND_PRODUCT.getLabel()));
        assertEquals(THIRD_PRODUCT, instock.findByLabel(THIRD_PRODUCT.getLabel()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByLabelShouldThrowIfNoProductWithLabelInStock() {
        instock.findByLabel(MISSING_PRODUCT_LABEL);
    }

    @Test
    public void testFindFirstByAlphabeticalOrderReturnsCollectionInCorrectOrder() {
        String[] expected = {FIRST_PRODUCT.getLabel(), SECOND_PRODUCT.label, THIRD_PRODUCT.getLabel()};
        Arrays.sort(expected);

        addProducts(PRODUCTS);
        List<Product> result = (List<Product>) instock.findFirstByAlphabeticalOrder(expected.length);

        assertEquals("wrong count", expected.length, result.size());

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.get(i).getLabel());
        }

    }

    @Test
    public void testFindFirstShouldReturnEmptyCollectionIfInputOverInstockProductCount() {
        addProducts(PRODUCTS);
        List<Product> firstByAlphabeticalOrder = (List<Product>) instock.findFirstByAlphabeticalOrder(instock.getCount() + 1);
        assertTrue(firstByAlphabeticalOrder.isEmpty());
    }

    @Test
    public void testFindAllInRangeShouldReturnCollectionInPriceRangeInReverseOrder() {
        addProducts(PRODUCTS);
        List<Product> inRange = (List<Product>) instock.findAllInRange(FIRST_PRODUCT.getPrice(), THIRD_PRODUCT.getPrice());

        Product[] productsByPriceDesc = Arrays.stream(new Product[]{FIRST_PRODUCT, SECOND_PRODUCT, THIRD_PRODUCT})
                .filter(p -> p.getPrice() > FIRST_PRODUCT.getPrice() && p.getPrice() <= THIRD_PRODUCT.getPrice())
                .sorted(Comparator.comparing(Product::getPrice).reversed()).toArray(Product[]::new);

        assertEquals("wrong count", productsByPriceDesc.length, inRange.size());

        for (int i = 0; i < productsByPriceDesc.length; i++) {
            assertEquals(productsByPriceDesc[i], inRange.get(i));
        }

    }

    @Test
    public void testFindAllByPriceShouldReturnProductsWithInputPrice() {
        Product firstMockedProduct = Mockito.mock(Product.class);
        Mockito.when(firstMockedProduct.getPrice()).thenReturn(SAME_PRICE_FOR_PRODUCT);
        Product secondMockedProduct = Mockito.mock(Product.class);
        Mockito.when(secondMockedProduct.getPrice()).thenReturn(SAME_PRICE_FOR_PRODUCT);
        Product thirdMockedProduct = Mockito.mock(Product.class);
        Mockito.when(thirdMockedProduct.getPrice()).thenReturn(SAME_PRICE_FOR_PRODUCT);

        addProducts(PRODUCTS);
        addProducts(firstMockedProduct, secondMockedProduct, thirdMockedProduct);

        List<Product> instockAllByPrice = (List<Product>) instock.findAllByPrice(SAME_PRICE_FOR_PRODUCT);

        assertEquals("wrong count", 3, instockAllByPrice.size());

        assertEquals(firstMockedProduct, instockAllByPrice.get(0));
        assertEquals(secondMockedProduct, instockAllByPrice.get(1));
        assertEquals(thirdMockedProduct, instockAllByPrice.get(2));
    }

    @Test
    public void testFindAllByPriceShouldReturnEmptyCollectionIfNoProductWithInputPriceInStock() {
        List<Product> byPrice = (List<Product>) instock.findAllByPrice(SAME_PRICE_FOR_PRODUCT);
        assertTrue(byPrice.isEmpty());
    }

    @Test
    public void testFindFirstMostExpensiveProductsReturnsCorrectProductsInOrder() {
        addProducts(PRODUCTS);
        int count = 2;
        Product[] mostExpensive = Arrays.stream(PRODUCTS)
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(count)
                .toArray(Product[]::new);

        List<Product> products = (List<Product>) instock.findFirstMostExpensiveProducts(count);

        assertEquals(mostExpensive.length, products.size());

        for (int i = 0; i < count; i++) {
            assertEquals(mostExpensive[i], products.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstMostExpensiveProductsThrowsWhenInputCountOverInstockCount() {
        addProducts(PRODUCTS);
        instock.findFirstMostExpensiveProducts(PRODUCTS.length + 1);
    }

    @Test
    public void testAllByQuantityShouldReturnCorrectProductsWith() {
        Product firstQuantityMock = Mockito.mock(Product.class);
        Mockito.when(firstQuantityMock.getQuantity()).thenReturn(SAME_QUANTITY);
        Product secondQuantityMock = Mockito.mock(Product.class);
        Mockito.when(secondQuantityMock.getQuantity()).thenReturn(SAME_QUANTITY);
        Product thirdQuantityMock = Mockito.mock(Product.class);
        Mockito.when(thirdQuantityMock.getQuantity()).thenReturn(SAME_QUANTITY);

        addProducts(PRODUCTS);
        addProducts(firstQuantityMock, secondQuantityMock, thirdQuantityMock);

        List<Product> allByQuantity = (List<Product>) instock.findAllByQuantity(SAME_QUANTITY);

        assertEquals("wrong count", 3, allByQuantity.size());

        assertEquals(firstQuantityMock, allByQuantity.get(0));
        assertEquals(secondQuantityMock, allByQuantity.get(1));
        assertEquals(thirdQuantityMock, allByQuantity.get(2));
    }

    @Test
    public void testAllByQuantityShouldReturnEmptyCollectionIfNoProductWithInputQuantityFound() {
        addProducts(PRODUCTS);
        List<Product> allByQuantity =  (List<Product>) instock.findAllByQuantity(SAME_QUANTITY);
        assertTrue(allByQuantity.isEmpty());
    }

    @Test
    public void testGetProductsShouldReturnAllProducts() {
        addProducts(PRODUCTS);

        List<Product> products = (List<Product>) instock.getProducts();

        assertEquals("wrong count", PRODUCTS.length, products.size());

        for (int i = 0; i < PRODUCTS.length; i++) {
            assertEquals(PRODUCTS[i], products.get(i));
        }
    }

    @Test
    public void testIteratorReturnsProductIterator() {
        addProducts(PRODUCTS);
        Iterator<Product> iterator = instock.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(FIRST_PRODUCT, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(SECOND_PRODUCT, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(THIRD_PRODUCT, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorShouldThrowIfNextIsCalledOnListIndex() {
        Iterator<Product> iterator = instock.iterator();
        iterator.next();
    }


    private void addProducts(Product... products) {
        for (Product product : products) {
            instock.add(product);
        }
    }

}
