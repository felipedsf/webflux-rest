package rocks.lipe.webfluxrest.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rocks.lipe.webfluxrest.domain.Vendor;
import rocks.lipe.webfluxrest.repository.VendorRepository;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository;


    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors")
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendor) {
        return vendorRepository.saveAll(vendor).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor vendorFound = vendorRepository.findById(id).block();
        if(!(vendorFound.getFirstName().equals(vendor.getFirstName()) &&
                vendorFound.getLastName().equals(vendor.getLastName()))) {
            vendorFound.setFirstName(vendor.getFirstName());
            vendorFound.setLastName(vendor.getLastName());
            return vendorRepository.save(vendorFound);
        }
        return Mono.just(vendorFound);
    }

}
