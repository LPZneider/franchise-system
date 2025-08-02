package com.nequi.franchise.franchise.domain.service;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductStockServiceTest {

    private ProductStockService productStockService;
    private Franchise franchise;

    @BeforeEach
    void setUp() {
        productStockService = new ProductStockService();
        franchise = new Franchise("franchise-1", new Name("Test Franchise"));
    }

    @Test
    void findTopStockPerBranch_WithMultipleBranchesAndProducts_ShouldReturnTopProductPerBranch() {
        // Given
        Branch branch1 = new Branch("branch-1", new Name("Branch 1"));
        Product product1a = new Product("product-1a", new Name("Product 1A"), new Stock(10));
        Product product1b = new Product("product-1b", new Name("Product 1B"), new Stock(25));
        branch1.addProduct(product1a);
        branch1.addProduct(product1b);

        Branch branch2 = new Branch("branch-2", new Name("Branch 2"));
        Product product2a = new Product("product-2a", new Name("Product 2A"), new Stock(15));
        Product product2b = new Product("product-2b", new Name("Product 2B"), new Stock(5));
        branch2.addProduct(product2a);
        branch2.addProduct(product2b);

        franchise.addBranch(branch1);
        franchise.addBranch(branch2);

        // When
        Map<Branch, Product> result = productStockService.findTopStockPerBranch(franchise);

        // Then
        assertEquals(2, result.size());
        assertEquals(product1b, result.get(branch1)); // Product with stock 25
        assertEquals(product2a, result.get(branch2)); // Product with stock 15
    }

    @Test
    void findTopStockPerBranch_WithEmptyBranches_ShouldReturnEmptyMap() {
        // Given
        Branch emptyBranch = new Branch("branch-1", new Name("Empty Branch"));
        franchise.addBranch(emptyBranch);

        // When
        Map<Branch, Product> result = productStockService.findTopStockPerBranch(franchise);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findTopStockPerBranch_WithSingleProductPerBranch_ShouldReturnThatProduct() {
        // Given
        Branch branch = new Branch("branch-1", new Name("Branch 1"));
        Product product = new Product("product-1", new Name("Product 1"), new Stock(10));
        branch.addProduct(product);
        franchise.addBranch(branch);

        // When
        Map<Branch, Product> result = productStockService.findTopStockPerBranch(franchise);

        // Then
        assertEquals(1, result.size());
        assertEquals(product, result.get(branch));
    }

    @Test
    void findTopStockPerBranch_WithProductsWithSameStock_ShouldReturnOne() {
        // Given
        Branch branch = new Branch("branch-1", new Name("Branch 1"));
        Product product1 = new Product("product-1", new Name("Product 1"), new Stock(10));
        Product product2 = new Product("product-2", new Name("Product 2"), new Stock(10));
        branch.addProduct(product1);
        branch.addProduct(product2);
        franchise.addBranch(branch);

        // When
        Map<Branch, Product> result = productStockService.findTopStockPerBranch(franchise);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.containsValue(product1) || result.containsValue(product2));
    }

    @Test
    void findTopStockPerBranch_WithZeroStockProducts_ShouldReturnTopProduct() {
        // Given
        Branch branch = new Branch("branch-1", new Name("Branch 1"));
        Product product1 = new Product("product-1", new Name("Product 1"), new Stock(0));
        Product product2 = new Product("product-2", new Name("Product 2"), new Stock(5));
        branch.addProduct(product1);
        branch.addProduct(product2);
        franchise.addBranch(branch);

        // When
        Map<Branch, Product> result = productStockService.findTopStockPerBranch(franchise);

        // Then
        assertEquals(1, result.size());
        assertEquals(product2, result.get(branch));
    }

    @Test
    void findTopStockPerBranch_WithNoFranchiseBranches_ShouldReturnEmptyMap() {
        // Given - franchise without branches

        // When
        Map<Branch, Product> result = productStockService.findTopStockPerBranch(franchise);

        // Then
        assertTrue(result.isEmpty());
    }
}
