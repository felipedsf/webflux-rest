package rocks.lipe.webfluxrest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import rocks.lipe.webfluxrest.domain.Vendor;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
