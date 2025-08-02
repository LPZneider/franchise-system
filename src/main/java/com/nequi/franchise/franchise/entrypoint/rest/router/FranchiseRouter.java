package com.nequi.franchise.franchise.entrypoint.rest.router;

import com.nequi.franchise.franchise.entrypoint.rest.handler.BranchHandler;
import com.nequi.franchise.franchise.entrypoint.rest.handler.FranchiseHandler;
import com.nequi.franchise.franchise.entrypoint.rest.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FranchiseRouter {

    private static final String API_BASE_PATH = "/api/v1";

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions.route()
                // Franchise operations
                .GET(API_BASE_PATH + "/franchises", handler::getAllFranchises)
                .GET(API_BASE_PATH + "/franchises/{id}", handler::getFranchise)
                .POST(API_BASE_PATH + "/franchises", handler::createFranchise)
                .PATCH(API_BASE_PATH + "/franchises/{id}", handler::updateFranchiseName)

                // Branch operations
                .POST(API_BASE_PATH + "/franchises/{id}/branches", handler::addBranch)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions.route()
                // Branch operations
                .PATCH(API_BASE_PATH + "/franchises/{franchiseId}/branches/{branchId}", handler::updateBranchName)

                // Product operations within branches
                .POST(API_BASE_PATH + "/franchises/{franchiseId}/branches/{branchId}/products", handler::addProduct)
                .PATCH(API_BASE_PATH + "/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock", handler::updateProductStock)
                .DELETE(API_BASE_PATH + "/franchises/{franchiseId}/branches/{branchId}/products/{productId}", handler::removeProduct)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
                // Product operations
                .PATCH(API_BASE_PATH + "/franchises/{franchiseId}/branches/{branchId}/products/{productId}", handler::updateProductName)

                // Product queries
                .GET(API_BASE_PATH + "/franchises/{franchiseId}/top-stock-products", handler::getTopStockProductsByBranch)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> allRoutes(RouterFunction<ServerResponse> franchiseRoutes,
                                                    RouterFunction<ServerResponse> branchRoutes,
                                                    RouterFunction<ServerResponse> productRoutes) {
        return franchiseRoutes
                .and(branchRoutes)
                .and(productRoutes);
    }
}
