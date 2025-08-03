package com.nequi.franchise.franchise;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.infrastructure.mapper.FranchiseMapper;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.BranchEntity;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.FranchiseEntity;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class FranchiseSystemApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(FranchiseSystemApplication.class);

    private final ReactiveMongoTemplate mongoTemplate;
    private final FranchiseRepository franchiseRepository;

    public FranchiseSystemApplication(ReactiveMongoTemplate mongoTemplate, FranchiseRepository franchiseRepository) {
        this.mongoTemplate = mongoTemplate;
        this.franchiseRepository = franchiseRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(FranchiseSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        mongoTemplate.dropCollection("franchise").subscribe();

        Flux.range(1, 10)
                .map(i -> {
                    List<ProductEntity> products = List.of(
                            new ProductEntity(UUID.randomUUID().toString(), "Product A" + i, 10 * i),
                            new ProductEntity(UUID.randomUUID().toString(), "Product B" + i, 5 * i)
                    );

                    BranchEntity branch1 = new BranchEntity(UUID.randomUUID().toString(), "Branch " + i + "-1", products);
                    BranchEntity branch2 = new BranchEntity(UUID.randomUUID().toString(), "Branch " + i + "-2", products);

                    return new FranchiseEntity(
                            UUID.randomUUID().toString(),
                            "Franchise " + i,
                            List.of(branch1, branch2)
                    );
                })
                .map(FranchiseMapper::toDomain)
                .flatMap(franchiseRepository::save)
                .subscribe(franchise ->
                        log.info("Franchise Inserted: {} with {} branches",
                                franchise.getName(),
                                franchise.getBranches().size()));
    }

}
