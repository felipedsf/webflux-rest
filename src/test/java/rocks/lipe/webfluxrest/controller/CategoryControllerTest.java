package rocks.lipe.webfluxrest.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rocks.lipe.webfluxrest.domain.Category;
import rocks.lipe.webfluxrest.repository.CategoryRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CategoryControllerTest {

    public static final String API_V1_CATEGORIES = "/api/v1/categories";
    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;


    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                                      Category.builder().description("Cat2").build()));

        webTestClient.get()
                    .uri(API_V1_CATEGORIES)
                    .exchange()
                    .expectBodyList(Category.class)
                    .hasSize(2);
    }

    @Test
    public void getById() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Cat1").build()));

        webTestClient.get()
                .uri(API_V1_CATEGORIES + "/1")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void create() {
        given(categoryRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("Category").build());

        webTestClient.post()
                    .uri(API_V1_CATEGORIES)
                    .body(categoryMono, Category.class)
                    .exchange()
                    .expectStatus()
                        .isCreated();
    }

    @Test
    public void update() {
        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("Category").build());

        webTestClient.put()
                .uri(API_V1_CATEGORIES + "/1")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                    .isOk();
    }

    @Test
    public void patchWithChange() {
        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Categories").build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("Category").build());

        webTestClient.patch()
                .uri(API_V1_CATEGORIES + "/1")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void patchNoChenge() {
        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Category").build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("Category").build());

        webTestClient.patch()
                .uri(API_V1_CATEGORIES + "/1")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any(Category.class));
    }

}