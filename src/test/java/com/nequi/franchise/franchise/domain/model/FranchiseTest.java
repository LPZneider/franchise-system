package com.nequi.franchise.franchise.domain.model;

import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {

    private Franchise franchise;
    private Name franchiseName;

    @BeforeEach
    void setUp() {
        franchiseName = new Name("Test Franchise");
        franchise = new Franchise("franchise-1", franchiseName);
    }

    @Test
    void constructor_ShouldCreateFranchiseWithCorrectValues() {
        assertEquals("franchise-1", franchise.getId());
        assertEquals(franchiseName, franchise.getName());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    void rename_ShouldUpdateFranchiseName() {
        Name newName = new Name("Updated Franchise");
        
        franchise.rename(newName);
        
        assertEquals(newName, franchise.getName());
    }

    @Test
    void addBranch_ShouldAddBranchToFranchise() {
        Branch branch = new Branch("branch-1", new Name("Test Branch"));
        
        franchise.addBranch(branch);
        
        assertEquals(1, franchise.getBranches().size());
        assertTrue(franchise.getBranches().contains(branch));
    }

    @Test
    void findBranchById_WhenBranchExists_ShouldReturnBranch() {
        Branch branch = new Branch("branch-1", new Name("Test Branch"));
        franchise.addBranch(branch);
        
        var result = franchise.findBranchById("branch-1");

        assertTrue(result.isPresent());
        assertEquals(branch, result.get());
    }

    @Test
    void findBranchById_WhenBranchDoesNotExist_ShouldReturnEmpty() {
        var result = franchise.findBranchById("non-existent-branch");

        assertTrue(result.isEmpty());
    }

    @Test
    void removeBranch_WhenBranchExists_ShouldRemoveBranch() {
        Branch branch = new Branch("branch-1", new Name("Test Branch"));
        franchise.addBranch(branch);

        franchise.removeBranch("branch-1");

        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    void removeBranch_WhenBranchDoesNotExist_ShouldNotAffectList() {
        Branch branch = new Branch("branch-1", new Name("Test Branch"));
        franchise.addBranch(branch);

        franchise.removeBranch("non-existent-branch");

        assertEquals(1, franchise.getBranches().size());
    }

    @Test
    void getBranches_ShouldReturnCopyOfBranches() {
        Branch branch = new Branch("branch-1", new Name("Test Branch"));
        franchise.addBranch(branch);

        var branches = franchise.getBranches();
        branches.clear(); // Modify the returned list

        // Original list should be unaffected
        assertEquals(1, franchise.getBranches().size());
    }

    @Test
    void toString_ShouldReturnStringRepresentation() {
        String result = franchise.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("franchise-1"));
        assertTrue(result.contains("Test Franchise"));
    }
}
