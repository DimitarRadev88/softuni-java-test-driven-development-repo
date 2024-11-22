package com.softuni.lab;

import java.util.Objects;

public class Product implements Comparable<Product> {

    public String label;

    public double price;

    public int quantity;

    public Product(String label, double price, int quantity) {
        this.label = label;
        this.price = price;
        this.quantity = quantity;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(Product o) {
        int result = this.getLabel().compareTo(o.getLabel());

        if (result == 0) {
            result = Double.compare(this.getPrice(), o.getPrice());
        }

        if (result == 0) {
            result = Integer.compare(this.getQuantity(), o.getQuantity());
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && quantity == product.quantity && Objects.equals(label, product.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, price, quantity);
    }
}
