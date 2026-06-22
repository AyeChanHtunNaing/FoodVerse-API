package dev.peacechan.foodverse.menu.mapper;

import dev.peacechan.foodverse.entity.Menu;
import dev.peacechan.foodverse.entity.Restaurant;
import dev.peacechan.foodverse.menu.dto.CreateMenuRequest;
import dev.peacechan.foodverse.menu.dto.MenuResponse;
import dev.peacechan.foodverse.menu.dto.UpdateMenuRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    public Menu toEntity(CreateMenuRequest request, Restaurant restaurant) {
        return Menu.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .status(request.status())
                .restaurant(restaurant)
                .build();
    }

    public MenuResponse toResponse(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.getStatus().name(),
                menu.getRestaurant().getId()
        );
    }

    public void updateEntity(Menu menu, UpdateMenuRequest request) {
        menu.setName(request.name());
        menu.setDescription(request.description());
        menu.setPrice(request.price());
        menu.setStatus(request.status());
    }
}
