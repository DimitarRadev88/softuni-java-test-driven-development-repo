package com.softuni.exercise.chainblock;

import java.util.*;
import java.util.stream.Collectors;

public class ChainblockImpl implements Chainblock {

    private List<Transaction> transactionList;

    public ChainblockImpl() {
        this.transactionList = new ArrayList<>();
    }

    public int getCount() {
        return transactionList.size();
    }

    public void add(Transaction transaction) {
        if (!contains(transaction.getId())) {
            transactionList.add(transaction);
        }
    }

    public boolean contains(Transaction transaction) {
        return transactionList.contains(transaction);
    }

    public boolean contains(int id) {
        return transactionList.stream().anyMatch(t -> t.getId() == id);
    }

    public void changeTransactionStatus(int id, TransactionStatus newStatus) {
        Transaction transaction = getById(id);
        transaction.setStatus(newStatus);
    }

    public void removeTransactionById(int id) {
        if (!transactionList.removeIf(t -> t.getId() == id)) {
            throw new IllegalArgumentException();
        }
    }

    public Transaction getById(int id) {
        return transactionList.stream()
                .filter(t -> t.getId() == id).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Iterable<Transaction> getByTransactionStatus(TransactionStatus status) {
        List<Transaction> transactions = transactionList.stream()
                .filter(t -> t.getStatus().equals(status))
                .sorted(Comparator.comparingDouble(Transaction::getAmount)
                        .reversed())
                .toList();

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return transactions;
    }

    public Iterable<String> getAllSendersWithTransactionStatus(TransactionStatus status) {
        List<String> senders = transactionList.stream().filter(t -> t.getStatus().equals(status))
                .sorted(Transaction::compareTo)
                .map(Transaction::getFrom)
                .toList();

        if (senders.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return senders;
    }

    public Iterable<String> getAllReceiversWithTransactionStatus(TransactionStatus status) {
        List<String> receivers = transactionList.stream()
                .filter(t -> t.getStatus().equals(status))
                .sorted(Transaction::compareTo)
                .map(Transaction::getTo)
                .toList();

        if (receivers.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return receivers;
    }

    public Iterable<Transaction> getAllOrderedByAmountDescendingThenById() {
        return transactionList.stream()
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed()
                        .thenComparing(Transaction::getId))
                .toList();
    }

    public Iterable<Transaction> getBySenderOrderedByAmountDescending(String sender) {
        List<Transaction> result = transactionList.stream()
                .filter(t -> t.getFrom().equals(sender))
                .sorted(Comparator.comparingDouble(Transaction::getAmount)
                        .reversed())
                .toList();

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    public Iterable<Transaction> getByReceiverOrderedByAmountThenById(String receiver) {
        List<Transaction> result = transactionList.stream().filter(t -> t.getTo().equals(receiver))
                .sorted(Comparator.comparingDouble(Transaction::getAmount)
                        .reversed()
                        .thenComparing(Transaction::getId))
                .toList();

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    public Iterable<Transaction> getByTransactionStatusAndMaximumAmount(TransactionStatus status, double amount) {

        return transactionList.stream()
                .filter(t -> t.getStatus().equals(status))
                .filter(t -> t.getAmount() <= amount)
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed()).toList();

    }

    public Iterable<Transaction> getBySenderAndMinimumAmountDescending(String sender, double amount) {
        List<Transaction> result = transactionList.stream()
                .filter(t -> t.getFrom().equals(sender))
                .filter(t -> t.getAmount() > amount)
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed())
                .toList();

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    public Iterable<Transaction> getByReceiverAndAmountRange(String receiver, double lo, double hi) {
        List<Transaction> result = transactionList.stream().filter(t -> t.getTo().equals(receiver))
                .filter(t -> t.getAmount() >= lo && t.getAmount() < hi)
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed()
                        .thenComparing(Transaction::getId))
                .toList();

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    public Iterable<Transaction> getAllInAmountRange(double lo, double hi) {
        return transactionList
                .stream()
                .filter(t -> t.getAmount() >= lo && t.getAmount() <= hi)
                .toList();
    }

    public Iterator<Transaction> iterator() {
        return transactionList.iterator();
    }
}
