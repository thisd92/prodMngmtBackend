package br.com.devth.productmanagement.repositories;

import br.com.devth.productmanagement.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
