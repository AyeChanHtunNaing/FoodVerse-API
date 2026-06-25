package dev.peacechan.foodverse.menu.service;

import dev.peacechan.foodverse.common.exception.ResourceNotFoundException;
import dev.peacechan.foodverse.config.CacheNames;
import dev.peacechan.foodverse.entity.Menu;
import dev.peacechan.foodverse.entity.Restaurant;
import dev.peacechan.foodverse.menu.dto.CreateMenuRequest;
import dev.peacechan.foodverse.menu.dto.MenuResponse;
import dev.peacechan.foodverse.menu.dto.UpdateMenuRequest;
import dev.peacechan.foodverse.menu.mapper.MenuMapper;
import dev.peacechan.foodverse.repository.MenuRepository;
import dev.peacechan.foodverse.repository.RestaurantRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuMapper menuMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @CacheEvict(cacheNames = CacheNames.MENUS, key = "#restaurantId")
    public MenuResponse createMenu(Long restaurantId, CreateMenuRequest request) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        Menu menu = menuMapper.toEntity(request, restaurant);
        return menuMapper.toResponse(menuRepository.save(menu));
    }

    @Transactional
    @CacheEvict(cacheNames = CacheNames.MENU, key = "#menuId + '::' + #restaurantId")
    public MenuResponse updateMenu(Long restaurantId, Long menuId, UpdateMenuRequest request) {
        getRestaurantById(restaurantId);
        Menu menu = getMenuByIdAndRestaurantId(menuId, restaurantId);
        menuMapper.updateEntity(menu, request);
        MenuResponse response = menuMapper.toResponse(menuRepository.save(menu));
        evictMenuRelatedCaches(restaurantId, menuId);
        return response;
    }

    @Transactional
    @CacheEvict(cacheNames = CacheNames.MENU, key = "#menuId + '::' + #restaurantId")
    public void deleteMenu(Long restaurantId, Long menuId) {
        getRestaurantById(restaurantId);
        menuRepository.delete(getMenuByIdAndRestaurantId(menuId, restaurantId));
        evictMenuRelatedCaches(restaurantId, menuId);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.MENU, key = "#menuId + '::' + #restaurantId")
    public MenuResponse getMenu(Long restaurantId, Long menuId) {
        getRestaurantById(restaurantId);
        return menuMapper.toResponse(getMenuByIdAndRestaurantId(menuId, restaurantId));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.MENUS, key = "#restaurantId")
    public List<MenuResponse> getMenus(Long restaurantId) {
        getRestaurantById(restaurantId);
        return menuRepository.findAllByRestaurantId(restaurantId).stream()
                .map(menuMapper::toResponse)
                .toList();
    }

    private void evictMenuRelatedCaches(Long restaurantId, Long menuId) {
        redisTemplate.delete(Set.of(
                CacheNames.MENUS + "::" + restaurantId,
                CacheNames.MENU + "::" + menuId + "::" + restaurantId
        ));
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
