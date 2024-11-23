package com.softuni.exercise.chainblock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Objects;

import static org.junit.Assert.*;

public class TransactionImplTest {

    private static final int TRANSACTION_ID = 111;
    private static final TransactionStatus TRANSACTION_STATUS = TransactionStatus.SUCCESSFUL;
    private static final String TRANSACTION_FROM = "Sender";
    private static final String TRANSACTION_TO = "Receiver";
    private static final double TRANSACTION_AMOUNT = 100.00;
    public static final double DELTA = 0.0000000001;

    private TransactionImpl transaction;

    @Before
    public void init() {
        transaction = new TransactionImpl(
                TRANSACTION_ID, TRANSACTION_STATUS, TRANSACTION_FROM, TRANSACTION_TO, TRANSACTION_AMOUNT
        );
    }

    @Test
    public void testConstructorCreates() {
        assertEquals(TRANSACTION_ID, transaction.getId());
        assertEquals(TRANSACTION_STATUS, transaction.getStatus());
        assertEquals(TRANSACTION_FROM, transaction.getFrom());
        assertEquals(TRANSACTION_TO, transaction.getTo());
        assertEquals(TRANSACTION_AMOUNT, transaction.getAmount(), DELTA);
    }

    @Test
    public void testCompareTo() {
        TransactionImpl left = new TransactionImpl(
                TRANSACTION_ID, TRANSACTION_STATUS, TRANSACTION_FROM, TRANSACTION_TO, TRANSACTION_AMOUNT
        );
        TransactionImpl right = new TransactionImpl(
                TRANSACTION_ID + 1, TRANSACTION_STATUS, TRANSACTION_FROM, TRANSACTION_TO, TRANSACTION_AMOUNT - 1
        );

        assertEquals(1, left.compareTo(right));

        right = new TransactionImpl(
                TRANSACTION_ID - 1, TRANSACTION_STATUS, TRANSACTION_FROM, TRANSACTION_TO, TRANSACTION_AMOUNT + 1
        );

        assertEquals(-1, left.compareTo(right));

        right = new TransactionImpl(
                TRANSACTION_ID, TRANSACTION_STATUS, TRANSACTION_FROM, TRANSACTION_TO, TRANSACTION_AMOUNT
        );

        assertEquals(0, left.compareTo(right));
    }

    @Test
    public void testTransactionEquals() {
        Transaction notEqual = Mockito.mock(Transaction.class);
        Mockito.when(notEqual.getId()).thenReturn(TRANSACTION_ID);
        assertNotEquals(transaction, notEqual);
        Transaction equal = new TransactionImpl(TRANSACTION_ID, null, null, null, -1);
        assertEquals(transaction, equal);
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(TRANSACTION_ID), transaction.hashCode());
    }

}
