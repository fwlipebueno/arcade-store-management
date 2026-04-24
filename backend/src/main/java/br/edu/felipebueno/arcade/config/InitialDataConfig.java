package br.edu.felipebueno.arcade.config;

import br.edu.felipebueno.arcade.application.service.CategoryService;
import br.edu.felipebueno.arcade.application.service.CustomerService;
import br.edu.felipebueno.arcade.application.service.ProductService;
import br.edu.felipebueno.arcade.domain.model.Category;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class InitialDataConfig {

    @Bean
    CommandLineRunner loadInitialData(
            CategoryService categoryService,
            ProductService productService,
            CustomerService customerService
    ) {
        return args -> {
            Category games = categoryService.create("Games");
            Category accessories = categoryService.create("Accessories");
            Category collectibles = categoryService.create("Collectibles");

            productService.create(
                    "USB Arcade Controller",
                    "Classic layout controller for fighting games.",
                    new BigDecimal("199.90"),
                    8,
                    accessories.getId()
            );
            productService.create(
                    "Arcade Tokens",
                    "Pack with 20 tokens for arcade machines.",
                    new BigDecimal("35.00"),
                    30,
                    games.getId()
            );
            productService.create(
                    "Retro Mini Cabinet",
                    "Decorative arcade cabinet miniature.",
                    new BigDecimal("149.90"),
                    5,
                    collectibles.getId()
            );

            customerService.create(
                    "Demo Customer",
                    "000.000.000-00",
                    "(43) 99999-0000",
                    "customer@arcade.local"
            );
        };
    }
}
