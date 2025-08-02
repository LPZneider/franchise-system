package com.nequi.franchise.franchise.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void constructor_WithValidStock_ShouldCreateStock() {
        int validStock = 10;

        Stock stock = new Stock(validStock);

        assertEquals(validStock, stock.getQuantity());
    }

    @Test
    void constructor_WithZeroStock_ShouldCreateStock() {
        int zeroStock = 0;

        Stock stock = new Stock(zeroStock);

        assertEquals(zeroStock, stock.getQuantity());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void constructor_WithNegativeStock_ShouldThrowException(int negativeStock) {
        assertThrows(IllegalArgumentException.class, () -> new Stock(negativeStock));
    }

    @Test
    void increase_ShouldReturnNewStockWithIncreasedQuantity() {
        Stock stock = new Stock(10);

        Stock increasedStock = stock.increase(5);

        assertEquals(15, increasedStock.getQuantity());
        assertEquals(10, stock.getQuantity()); // Original unchanged
    }

    @Test
    void decrease_ShouldReturnNewStockWithDecreasedQuantity() {
        Stock stock = new Stock(10);

        Stock decreasedStock = stock.decrease(3);

        assertEquals(7, decreasedStock.getQuantity());
        assertEquals(10, stock.getQuantity()); // Original unchanged
    }

    @Test
    void decrease_WhenResultWouldBeNegative_ShouldThrowException() {
        Stock stock = new Stock(5);

        assertThrows(IllegalArgumentException.class, () -> stock.decrease(10));
    }

    @Test
    void equals_WithSameValue_ShouldReturnTrue() {
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(10);

        assertEquals(stock1, stock2);
    }

    @Test
    void equals_WithDifferentValue_ShouldReturnFalse() {
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(20);

        assertNotEquals(stock1, stock2);
    }

    @Test
    void hashCode_WithSameValue_ShouldBeSame() {
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(10);

        assertEquals(stock1.hashCode(), stock2.hashCode());
    }
}
