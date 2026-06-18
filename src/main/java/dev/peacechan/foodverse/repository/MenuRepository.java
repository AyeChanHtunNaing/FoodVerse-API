package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
