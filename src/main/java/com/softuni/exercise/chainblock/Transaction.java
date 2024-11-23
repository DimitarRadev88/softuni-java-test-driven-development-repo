package com.softuni.exercise.chainblock;

public interface Transaction {

    int getId();

    TransactionStatus getStatus();

    double getAmount();

    String getFrom();

    String getTo();

    void setStatus(TransactionStatus status);

    int compareTo(Transaction transaction);

}
