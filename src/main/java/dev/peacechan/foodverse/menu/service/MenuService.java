package dev.peacechan.foodverse.menu.service;

import dev.peacechan.foodverse.common.exception.ResourceNotFoundException;
import dev.peacechan.foodverse.entity.Menu;
import dev.peacechan.foodverse.entity.Restaurant;
import dev.peacechan.foodverse.menu.dto.CreateMenuRequest;
import dev.peacechan.foodverse.menu.dto.MenuResponse;
import dev.peacechan.foodverse.menu.dto.UpdateMenuRequest;
import dev.peacechan.foodverse.menu.mapper.MenuMapper;
import dev.peacechan.foodverse.repository.MenuRepository;
import dev.peacechan.foodverse.repository.RestaurantRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public MenuResponse createMenu(Long restaurantId, CreateMenuRequest request) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        Menu menu = menuMapper.toEntity(request, restaurant);
        return menuMapper.toResponse(menuRepository.save(menu));
    }

    @Transactional
    public MenuResponse updateMenu(Long restaurantId, Long menuId, UpdateMenuRequest request) {
        getRestaurantById(restaurantId);
        Menu menu = getMenuByIdAndRestaurantId(menuId, restaurantId);
        menuMapper.updateEntity(menu, request);
        return menuMapper.toResponse(menuRepository.save(menu));
    }

    @Transactional
    public void deleteMenu(Long restaurantId, Long menuId) {
        getRestaurantById(restaurantId);
        menuRepository.delete(getMenuByIdAndRestaurantId(menuId, restaurantId));
    }

    @Transactional(readOnly = true)
    public MenuResponse getMenu(Long restaurantId, Long menuId) {
        getRestaurantById(restaurantId);
        return menuMapper.toResponse(getMenuByIdAndRestaurantId(menuId, restaurantId));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long restaurantId) {
        getRestaurantById(restaurantId);
        return menuRepository.findAllByRestaurantId(restaurantId).stream()
                .map(menuMapper::toResponse)
                .toList();
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
    }

    private Menu getMenuByIdAndRestaurantId(Long menuId, Long restaurantId) {
        return menuRepository.findByIdAndRestaurantId(menuId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu not found with id: " + menuId + " in restaurant: " + restaurantId
                ));
    }
}
