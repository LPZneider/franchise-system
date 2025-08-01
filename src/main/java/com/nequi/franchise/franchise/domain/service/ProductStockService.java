package com.nequi.franchise.franchise.domain.service;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductStockService {
    public Map<Branch, Product> findTopStockPerBranch(Franchise franchise) {
        Map<Branch, Product> result = new HashMap<>();

        for (Branch branch : franchise.getBranches()) {
            branch.getProducts().stream()
                    .max(Comparator.comparingInt(p -> p.getStock().getQuantity()))
                    .ifPresent(product -> result.put(branch, product));
        }

        return result;
    }
}
