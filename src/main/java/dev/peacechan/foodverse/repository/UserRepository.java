package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.User;
import dev.peacechan.foodverse.enums.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRole(String email, UserRole role);

    Optional<User> findByIdAndRole(Long id, UserRole role);

    List<User> findAllByRole(UserRole role);

    boolean existsByEmail(String email);
}
