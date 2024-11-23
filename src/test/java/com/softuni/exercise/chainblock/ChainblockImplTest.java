package com.softuni.exercise.chainblock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class ChainblockImplTest {


    private static final int TRANSACTION_ID = 1111;
    private static final TransactionStatus CHANGED_TRANSACTION_STATUS = TransactionStatus.UNAUTHORIZED;
    private static final TransactionStatus EXPECTED_TRANSACTION_STATUS = TransactionStatus.FAILED;
    private static final TransactionStatus NOT_EXPECTED_TRANSACTION_STATUS = TransactionStatus.ABORTED;
    private static final String EXPECTED_SENDER = "Expected";
    private static final String NOT_EXPECTED_SENDER = "Not Expected";
    private static final String EXPECTED_RECEIVER = "Expected";
    private static final String NOT_EXPECTED_RECEIVER = "Not Expected";
    private static final int MISSING_TRANSACTION_ID = 1234;
    private static final String MISSING_SENDER = "Missing";
    private static final String MISSING_RECEIVER = "Missing";
    private static final double MAXIMUM_AMOUNT = 50.0;
    private static final double MINIMUM_AMOUNT = 50.0;
    private static final double TOO_BIG_AMOUNT = 99999999999999999.99;
    private static final double LOW_AMOUNT = 30.0;
    private static final double HIGH_AMOUNT = 70.0;


    private ChainblockImpl chainblock;
    private Transaction transaction;
    private List<Transaction> transactions;

    @Before
    public void init() {
        chainblock = new ChainblockImpl();
        transaction = new TransactionImpl(TRANSACTION_ID, EXPECTED_TRANSACTION_STATUS, EXPECTED_SENDER, EXPECTED_RECEIVER, LOW_AMOUNT);
        transactions = getTransactions();
        transactions.add(transaction);
    }

    @Test
    public void testGetCountReturnsCorrectCount() {
        ChainblockImpl chainblock = new ChainblockImpl();
        assertEquals("wrong count", 0, chainblock.getCount());
        chainblock.add(transaction);
        assertEquals("wrong count", 1, chainblock.getCount());
    }

    @Test
    public void testAddAddsTransaction() {
        chainblock.add(transaction);
        assertTrue("missing transaction", chainblock.contains(TRANSACTION_ID));
    }

    @Test
    public void testAddDoesNotAddTransactionWithExistingId() {
        chainblock.add(transaction);
        chainblock.add(transaction);
        assertEquals("wrong count", 1, chainblock.getCount());
    }

    @Test
    public void testContainsTransaction() {
        chainblock.add(transaction);
        assertFalse("wrong result", chainblock.contains(Mockito.mock(Transaction.class)));
        assertTrue("wrong result", chainblock.contains(transaction));
    }

    @Test
    public void testChangeTransactionStatusChangesStatus() {
        chainblock.add(transaction);
        chainblock.changeTransactionStatus(transaction.getId(), CHANGED_TRANSACTION_STATUS);
        assertEquals("wrong transaction status", CHANGED_TRANSACTION_STATUS, transaction.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeTransactionStatusThrowsWhenIdNotContained() {
        chainblock.changeTransactionStatus(MISSING_TRANSACTION_ID, CHANGED_TRANSACTION_STATUS);
    }

    @Test
    public void testRemoveTransactionByIdRemovesTransaction() {
        chainblock.add(transaction);
        assertTrue(chainblock.contains(TRANSACTION_ID));
        chainblock.removeTransactionById(TRANSACTION_ID);
        assertFalse("wrong result", chainblock.contains(TRANSACTION_ID));
        assertFalse("wrong result", chainblock.contains(transaction));
        assertEquals("wrong count", 0, chainblock.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveTransactionByIdThrowsWhenIdNotContained() {
        chainblock.removeTransactionById(MISSING_TRANSACTION_ID);
    }

    @Test
    public void testGetByIdReturnsCorrectTransaction() {
        transactions.forEach(chainblock::add);
        assertEquals("wrong transaction", transaction, chainblock.getById(TRANSACTION_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByIdThrowsWhenTransactionIdNotContained() {
        chainblock.getById(MISSING_TRANSACTION_ID);
    }

    @Test
    public void testGetByTransactionStatusReturnsTransactionsWithInputStatusOrderedByAmount() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = filterByExpectedStatusAndSortByAmountDesc(transactions);

        List<Transaction> result = (List<Transaction>) chainblock.getByTransactionStatus(EXPECTED_TRANSACTION_STATUS);
        assertListEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByTransactionStatusThrowsWhenTransactionsWithStatusNotContained() {
        chainblock.getByTransactionStatus(CHANGED_TRANSACTION_STATUS);
    }

    @Test
    public void testGetAllSendersWithTransactionStatusReturnsAllSendersWithInputStatusOrderedByAmount() {
        transactions.forEach(chainblock::add);

        List<String> expected = filterByExpectedStatusAndSortByAmountDesc(transactions)
                .stream()
                .map(Transaction::getFrom)
                .toList();

        List<String> result = (List<String>) chainblock.getAllSendersWithTransactionStatus(EXPECTED_TRANSACTION_STATUS);

        assertListEquals(expected, result);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetAllSendersWithTransactionStatusThrowsWhenTransactionsWithInputStatusNotContained() {
        transactions.forEach(chainblock::add);

        chainblock.getAllSendersWithTransactionStatus(CHANGED_TRANSACTION_STATUS);
    }

    @Test
    public void testGetAllReceiversWithTransactionStatusReturnsAllSendersWithInputStatusOrderedByAmount() {
        transactions.forEach(chainblock::add);

        List<String> expected = filterByExpectedStatusAndSortByAmountDesc(transactions)
                .stream()
                .map(Transaction::getTo)
                .toList();

        List<String> result = (List<String>) chainblock.getAllReceiversWithTransactionStatus(EXPECTED_TRANSACTION_STATUS);

        assertListEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllReceiversWithTransactionStatusThrowsWhenTransactionsWithInputStatusNotContained() {
        transactions.forEach(chainblock::add);

        chainblock.getAllReceiversWithTransactionStatus(CHANGED_TRANSACTION_STATUS);
    }

    @Test
    public void testGetAllOrderedByAmountDescendingThenByIdReturnsInCorrectOrder() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed()
                        .thenComparing(Transaction::getId))
                .toList();

        List<Transaction> result = (List<Transaction>) chainblock.getAllOrderedByAmountDescendingThenById();

        assertListEquals(expected, result);
    }

    @Test
    public void testGetBySenderOrderedByAmountDescendingReturnsTransactionsInCorrectOrder() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = transactions.stream()
                .filter(t -> t.getFrom().equals(EXPECTED_SENDER))
                .sorted(Comparator.comparingDouble(Transaction::getAmount)
                        .reversed())
                .toList();

        List<Transaction> result = (List<Transaction>) chainblock.getBySenderOrderedByAmountDescending(EXPECTED_SENDER);

        assertListEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBySenderOrderedByAmountDescendingThrowsWhenTransactionWithInputSenderIsNotContained() {
        transactions.forEach(chainblock::add);

        chainblock.getBySenderOrderedByAmountDescending(MISSING_SENDER);
    }

    @Test
    public void testGetByReceiverOrderedByAmountDescendingReturnsTransactionsInCorrectOrder() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = transactions.stream()
                .filter(t -> t.getFrom().equals(EXPECTED_RECEIVER))
                .sorted(Comparator.comparingDouble(Transaction::getAmount)
                        .reversed()
                        .thenComparing(Transaction::getId))
                .toList();

        List<Transaction> result = (List<Transaction>) chainblock.getByReceiverOrderedByAmountThenById(EXPECTED_RECEIVER);

        assertListEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByReceiverOrderedByAmountDescendingThrowsWhenTransactionWithInputSenderIsNotContained() {
        transactions.forEach(chainblock::add);

        chainblock.getByReceiverOrderedByAmountThenById(MISSING_RECEIVER);
    }

    @Test
    public void testGetByTransactionStatusAndMaximumAmountReturnsTransactionsInCorrectOrder() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = transactions.stream()
                .filter(t -> t.getStatus().equals(EXPECTED_TRANSACTION_STATUS))
                .filter(t -> t.getAmount() <= MAXIMUM_AMOUNT)
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed()).toList();

        List<Transaction> result = (List<Transaction>) chainblock
                .getByTransactionStatusAndMaximumAmount(EXPECTED_TRANSACTION_STATUS, MAXIMUM_AMOUNT);

        assertListEquals(expected, result);
    }


    @Test
    public void testGetBySenderAndMinimumAmountDescendingReturnsTransactionsInCorrectOrder() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = transactions.stream()
                .filter(t -> t.getFrom().equals(EXPECTED_SENDER))
                .filter(t -> t.getAmount() > MINIMUM_AMOUNT)
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed())
                .toList();

        List<Transaction> result = (List<Transaction>) chainblock.getBySenderAndMinimumAmountDescending(EXPECTED_SENDER, MINIMUM_AMOUNT);

        assertListEquals(expected, result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBySenderAndMinimumAmountDescendingThrowsWhenTransactionWithSenderNotContained() {
        transactions.forEach(chainblock::add);

        chainblock.getBySenderAndMinimumAmountDescending(MISSING_SENDER, MINIMUM_AMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBySenderAndMinimumAmountDescendingThrowsWhenTransactionWithMinimumAmountNotContained() {
        transactions.forEach(chainblock::add);

        chainblock.getBySenderAndMinimumAmountDescending(EXPECTED_SENDER, TOO_BIG_AMOUNT);
    }

    @Test
    public void testGetByReceiverAndAmountRangeReturnsCorrectTransactionsInOrder() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = transactions.stream().filter(t -> t.getTo().equals(EXPECTED_RECEIVER))
                .filter(t -> t.getAmount() >= LOW_AMOUNT && t.getAmount() < HIGH_AMOUNT)
                .sorted(Comparator.comparing(Transaction::getAmount)
                        .reversed()
                        .thenComparing(Transaction::getId))
                .toList();

        List<Transaction> result = (List<Transaction>) chainblock.getByReceiverAndAmountRange(EXPECTED_RECEIVER, LOW_AMOUNT, HIGH_AMOUNT);

        assertListEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByReceiverAndAmountRangeThrowsWhenTransactionWithReceiverNotContained() {
        transactions.forEach(chainblock::add);
        chainblock.getByReceiverAndAmountRange(MISSING_RECEIVER, LOW_AMOUNT, HIGH_AMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByReceiverAndAmountRangeThrowsWhenTransactionWithRangeAmountNotContained() {
        transactions.forEach(chainblock::add);
        chainblock.getByReceiverAndAmountRange(EXPECTED_RECEIVER, LOW_AMOUNT + TOO_BIG_AMOUNT, HIGH_AMOUNT + TOO_BIG_AMOUNT);
    }

    @Test
    public void testGetAllInAmountRangeReturnsCorrectTransactionsInOrder() {
        transactions.forEach(chainblock::add);

        List<Transaction> expected = transactions
                .stream()
                .filter(t -> t.getAmount() >= LOW_AMOUNT && t.getAmount() <= HIGH_AMOUNT)
                .toList();

        List<Transaction> result = (List<Transaction>) chainblock.getAllInAmountRange(LOW_AMOUNT, HIGH_AMOUNT);

        assertListEquals(expected, result);
    }

    @Test
    public void testIteratorReturnsChainblockIterator() {
        assertNotNull(chainblock.iterator());
    }

    private static <T> void assertListEquals(List<T> expected, List<T> actual) {
        assertNotNull("null result is not expected", actual);
        assertEquals("wrong transaction count", expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals("wrong list item", expected.get(i), actual.get(i));
        }
    }


    private static List<Transaction> filterByExpectedStatusAndSortByAmountDesc(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus().equals(EXPECTED_TRANSACTION_STATUS))
                .sorted(Comparator.comparingDouble(Transaction::getAmount)
                        .reversed())
                .toList();
    }

    private List<Transaction> getTransactions() {
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Transaction transaction = Mockito.mock(Transaction.class);
            Mockito.when(transaction.getId()).thenReturn(i);
            Mockito.when(transaction.getAmount()).thenReturn((i + 1) * 10.0);
            if (i % 2 == 0) {
                Mockito.when(transaction.getStatus()).thenReturn(EXPECTED_TRANSACTION_STATUS);
                Mockito.when(transaction.getFrom()).thenReturn(EXPECTED_SENDER);
                Mockito.when(transaction.getTo()).thenReturn(EXPECTED_RECEIVER);
            } else {
                Mockito.when(transaction.getStatus()).thenReturn(NOT_EXPECTED_TRANSACTION_STATUS);
                Mockito.when(transaction.getFrom()).thenReturn(NOT_EXPECTED_SENDER);
                Mockito.when(transaction.getTo()).thenReturn(NOT_EXPECTED_RECEIVER);
            }

            list.add(transaction);
        }

        return list;
    }

}
