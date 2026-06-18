package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
