package rocks.lipe.webfluxrest.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rocks.lipe.webfluxrest.domain.Category;
import rocks.lipe.webfluxrest.domain.Vendor;
import rocks.lipe.webfluxrest.repository.CategoryRepository;
import rocks.lipe.webfluxrest.repository.VendorRepository;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;

    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) {

        if(categoryRepository.count().block() == 0) {
            System.out.println("##### LOADING DATA #####");

            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Beads").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();

            System.out.println("##### LOADED "+categoryRepository.count().block()+" CATEGORIES #####");

            vendorRepository.save(Vendor.builder().firstName("Paula").lastName("Yamada").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Ricardo").lastName("Paiva").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Thiago").lastName("Azevedo").build()).block();

            System.out.println("##### LOADED "+vendorRepository.count().block()+" VENDORS #####");


        }
    }

}
