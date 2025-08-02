package com.nequi.franchise.franchise.domain.model;

import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;
    private Name productName;
    private Stock stock;

    @BeforeEach
    void setUp() {
        productName = new Name("Test Product");
        stock = new Stock(10);
        product = new Product("product-1", productName, stock);
    }

    @Test
    void constructor_ShouldCreateProductWithCorrectValues() {
        assertEquals("product-1", product.getId());
        assertEquals(productName, product.getName());
        assertEquals(stock, product.getStock());
    }

    @Test
    void rename_ShouldUpdateProductName() {
        Name newName = new Name("Updated Product");
        
        product.rename(newName);
        
        assertEquals(newName, product.getName());
    }

    @Test
    void updateStock_ShouldUpdateProductStock() {
        Stock newStock = new Stock(25);
        
        product.updateStock(newStock);
        
        assertEquals(newStock, product.getStock());
    }

    @Test
    void updateStock_WithZeroStock_ShouldUpdate() {
        Stock zeroStock = new Stock(0);
        
        product.updateStock(zeroStock);
        
        assertEquals(zeroStock, product.getStock());
    }

    @Test
    void toString_ShouldReturnStringRepresentation() {
        String result = product.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("product-1"));
        assertTrue(result.contains("Test Product"));
    }
}
