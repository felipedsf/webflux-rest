package rocks.lipe.webfluxrest.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rocks.lipe.webfluxrest.domain.Vendor;
import rocks.lipe.webfluxrest.repository.VendorRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VendorControllerTest {

    public static final String API_V1_VENDORS = "/api/v1/vendors";
    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);

        webTestClient = WebTestClient.bindToController(vendorController).build();

    }

    @Test
    public void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("First").lastName("Vendor").build(),
                                     Vendor.builder().firstName("Vendor").lastName("Last").build()));
        webTestClient.get()
                     .uri(API_V1_VENDORS)
                     .exchange()
                     .expectBodyList(Vendor.class)
                     .hasSize(2);

    }

    @Test
    public void getById() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("First").lastName("Last").build()));

        webTestClient.get()
                     .uri(API_V1_VENDORS + "/1")
                     .exchange()
                     .expectBody(Vendor.class);

    }

    @Test
    public void create() {
        given(vendorRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Felipe").lastName("Faria").build());

        webTestClient.post()
                    .uri(API_V1_VENDORS)
                    .body(vendorMono, Vendor.class)
                    .exchange()
                    .expectStatus()
                        .isCreated();

    }

    @Test
    public void update() {
        given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Felipe").lastName("Faria").build());

        webTestClient.put()
                    .uri(API_V1_VENDORS + "/1")
                    .body(vendorMono, Vendor.class)
                    .exchange()
                    .expectStatus()
                        .isOk();

    }

    @Test
    public void patchWithChange() {
        given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Felipe").lastName("Faria").build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Felipe").lastName("Farias").build());

        webTestClient.patch()
                .uri(API_V1_VENDORS + "/1")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any(Vendor.class));
    }

    @Test
    public void patchNoChange() {
        given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Felipe").lastName("Faria").build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Felipe").lastName("Faria").build());

        webTestClient.patch()
                .uri(API_V1_VENDORS + "/1")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any(Vendor.class));
    }
}