package com.nequi.franchise.franchise.domain.model;

import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Franchise {
    @Getter
    private final String id;
    @Getter
    private Name name;
    private final List<Branch> branches;

    public Franchise(String id, Name name) {
        this.id = id;
        this.name = name;
        this.branches = new ArrayList<>();
    }

    public void rename(Name newName) {
        this.name = newName;
    }

    public void addBranch(Branch branch) {
        branches.add(branch);
    }

    public Optional<Branch> findBranchById(String branchId) {
        return branches.stream().filter(b -> b.getId().equals(branchId)).findFirst();
    }

    public void removeBranch(String branchId) {
        branches.removeIf(b -> b.getId().equals(branchId));
    }

    public List<Branch> getBranches() {
        return Collections.unmodifiableList(branches);
    }

}
