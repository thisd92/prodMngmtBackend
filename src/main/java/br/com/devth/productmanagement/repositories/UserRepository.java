package br.com.devth.productmanagement.repositories;

import br.com.devth.productmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
