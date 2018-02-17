package rocks.lipe.webfluxrest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import rocks.lipe.webfluxrest.domain.Category;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
