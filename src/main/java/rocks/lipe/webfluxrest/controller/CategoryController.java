package rocks.lipe.webfluxrest.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rocks.lipe.webfluxrest.domain.Category;
import rocks.lipe.webfluxrest.repository.CategoryRepository;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/v1/categories")
    public Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    public Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/categories")
    public Mono<Void> create(@RequestBody Publisher<Category> category) {
        return categoryRepository.saveAll(category).then();
    }

    @PutMapping("/api/v1/categories/{id}")
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/api/v1/categories/{id}")
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
        Category categoryFound = categoryRepository.findById(id).block();

        if(!categoryFound.getDescription().equals(category.getDescription())) {
            categoryFound.setDescription(category.getDescription());
            return categoryRepository.save(categoryFound);
        }

        return Mono.just(categoryFound);
    }


}
