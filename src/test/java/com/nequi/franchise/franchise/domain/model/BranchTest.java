package com.nequi.franchise.franchise.domain.model;

import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    private Branch branch;
    private Name branchName;

    @BeforeEach
    void setUp() {
        branchName = new Name("Test Branch");
        branch = new Branch("branch-1", branchName);
    }

    @Test
    void constructor_ShouldCreateBranchWithCorrectValues() {
        assertEquals("branch-1", branch.getId());
        assertEquals(branchName, branch.getName());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    void rename_ShouldUpdateBranchName() {
        Name newName = new Name("Updated Branch");
        
        branch.rename(newName);
        
        assertEquals(newName, branch.getName());
    }

    @Test
    void addProduct_ShouldAddProductToBranch() {
        Product product = new Product("product-1", new Name("Test Product"), new Stock(10));

        branch.addProduct(product);
        
        assertEquals(1, branch.getProducts().size());
        assertTrue(branch.getProducts().contains(product));
    }

    @Test
    void findProductById_WhenProductExists_ShouldReturnProduct() {
        Product product = new Product("product-1", new Name("Test Product"), new Stock(10));
        branch.addProduct(product);
        
        var result = branch.findProductById("product-1");

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void findProductById_WhenProductDoesNotExist_ShouldReturnEmpty() {
        var result = branch.findProductById("non-existent-product");

        assertTrue(result.isEmpty());
    }

    @Test
    void removeProduct_WhenProductExists_ShouldRemoveProduct() {
        Product product = new Product("product-1", new Name("Test Product"), new Stock(10));
        branch.addProduct(product);
        
        branch.removeProduct("product-1");

        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    void removeProduct_WhenProductDoesNotExist_ShouldNotAffectList() {
        Product product = new Product("product-1", new Name("Test Product"), new Stock(10));
        branch.addProduct(product);

        branch.removeProduct("non-existent-product");

        assertEquals(1, branch.getProducts().size());
    }

    @Test
    void getProducts_ShouldReturnCopyOfProducts() {
        Product product = new Product("product-1", new Name("Test Product"), new Stock(10));
        branch.addProduct(product);

        var products = branch.getProducts();
        products.clear(); // Modify the returned list

        // Original list should be unaffected
        assertEquals(1, branch.getProducts().size());
    }

    @Test
    void toString_ShouldReturnStringRepresentation() {
        String result = branch.toString();

        assertNotNull(result);
        assertTrue(result.contains("branch-1"));
        assertTrue(result.contains("Test Branch"));
    }
}
