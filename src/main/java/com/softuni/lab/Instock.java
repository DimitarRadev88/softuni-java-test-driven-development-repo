package com.softuni.lab;

import jdk.jshell.spi.ExecutionControl;

import java.util.*;
import java.util.stream.Collectors;

public class Instock implements ProductStock {

    private List<Product> products;

    public Instock() {
        this.products = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public boolean contains(Product product) {
        return products.contains(product);
    }

    @Override
    public void add(Product product) {
        products.add(product);
    }

    @Override
    public void changeQuantity(String label, int quantity) {
        Product product = products.stream()
                .filter(p -> p.getLabel().equals(label))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        product.setQuantity(product.getQuantity() + quantity);
    }

    @Override
    public Product find(int index) {
        return products.get(index);
    }

    @Override
    public Product findByLabel(String label) {
       return products.stream()
               .filter(p -> p.getLabel().equals(label))
               .findFirst()
               .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Iterable<Product> findFirstByAlphabeticalOrder(int count) {
        if (count > products.size()) {
            return new ArrayList<>();
        }

        return products.stream().sorted(Comparator.comparing(Product::getLabel))
                .limit(count)
                .toList();
    }

    @Override
    public Iterable<Product> findAllInRange(double lo, double hi) {
        return products.stream()
                .filter(p -> p.getPrice() > lo && p.getPrice() <= hi)
                .sorted(Comparator.comparing(Product::getPrice)
                        .reversed())
                .toList();
    }

    @Override
    public Iterable<Product> findAllByPrice(double price) {
        return products.stream()
                .filter(p -> p.getPrice() == price)
                .toList();
    }

    @Override
    public Iterable<Product> findFirstMostExpensiveProducts(int count) {
        validateCount(count);
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice)
                        .reversed())
                .limit(count).toList();
    }

    private void validateCount(int count) {
        if (count < 0 || count > products.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Iterable<Product> findAllByQuantity(int quantity) {
        return products.stream()
                .filter(p -> p.getQuantity() == quantity)
                .toList();
    }

    @Override
    public Iterable<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public Iterator<Product> iterator() {
        return new Iterator<Product>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < products.size();
            }

            @Override
            public Product next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return products.get(index++);
            }
        };
    }
}
