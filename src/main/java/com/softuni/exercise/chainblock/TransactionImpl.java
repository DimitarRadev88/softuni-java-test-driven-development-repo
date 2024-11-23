package com.softuni.exercise.chainblock;

import java.util.Comparator;
import java.util.Objects;

public class TransactionImpl implements Transaction, Comparable<Transaction> {

    private int id;
    private TransactionStatus status;
    private String from;
    private String to;
    private double amount;

    public TransactionImpl(int id, TransactionStatus status, String from, String to, double amount) {
        this.id = id;
        setStatus(status);
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TransactionStatus getStatus() {
        return status;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionImpl that = (TransactionImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public int compareTo(Transaction o) {
        int result = Double.compare(amount, o.getAmount());
        if (result == 0) {
            result = Integer.compare(id, o.getId());
        }
        return result;
    }
}
