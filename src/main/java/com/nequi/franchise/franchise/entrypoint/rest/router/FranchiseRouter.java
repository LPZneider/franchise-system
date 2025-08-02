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

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions.route()
                .POST("/franchises", handler::createFranchise)
                .GET("/franchises/{id}", handler::getFranchise)
                .POST("/franchises/{id}/branches", handler::addBranch)
                .PUT("/franchises/{id}/name", handler::updateFranchiseName)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions.route()
                .POST("/franchises/{franchiseId}/branches/{branchId}/products", handler::addProduct)
                .PUT("/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock", handler::updateProductStock)
                .PUT("/franchises/{franchiseId}/branches/{branchId}/name", handler::updateBranchName)
                .DELETE("/franchises/{franchiseId}/branches/{branchId}/products/{productId}", handler::removeProduct)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> topProductsRoutes(ProductHandler handler) {
        return RouterFunctions.route()
            .GET("/franchises/{franchiseId}/top-products", handler::getTopStockProductsByBranch)
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
            .PUT("/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name", handler::updateProductName)
            .build();
    }
}
