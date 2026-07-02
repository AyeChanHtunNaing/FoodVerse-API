package dev.peacechan.foodverse.restaurant.service;

import dev.peacechan.foodverse.common.exception.ConflictException;
import dev.peacechan.foodverse.common.exception.ResourceNotFoundException;
import dev.peacechan.foodverse.config.CacheNames;
import dev.peacechan.foodverse.entity.Restaurant;
import dev.peacechan.foodverse.repository.OrderRepository;
import dev.peacechan.foodverse.repository.RestaurantRepository;
import dev.peacechan.foodverse.restaurant.dto.CreateRestaurantRequest;
import dev.peacechan.foodverse.restaurant.dto.RestaurantResponse;
import dev.peacechan.foodverse.restaurant.dto.UpdateRestaurantRequest;
import dev.peacechan.foodverse.restaurant.mapper.RestaurantMapper;
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
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final RestaurantMapper restaurantMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @CacheEvict(cacheNames = CacheNames.RESTAURANTS, key = "'all'")
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Transactional
    @CacheEvict(cacheNames = CacheNames.RESTAURANT, key = "#restaurantId")
    public RestaurantResponse updateRestaurant(Long restaurantId, UpdateRestaurantRequest request) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurantMapper.updateEntity(restaurant, request);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        evictRestaurantRelatedCaches(restaurantId);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Transactional
    @CacheEvict(cacheNames = CacheNames.RESTAURANT, key = "#restaurantId")
    public void deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (orderRepository.existsByRestaurantId(restaurantId)) {
            throw new ConflictException("Restaurant cannot be deleted because it has existing orders");
        }
        restaurantRepository.delete(restaurant);
        evictRestaurantRelatedCaches(restaurantId);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.RESTAURANT, key = "#restaurantId")
    public RestaurantResponse getRestaurant(Long restaurantId) {
        return restaurantMapper.toResponse(getRestaurantById(restaurantId));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.RESTAURANTS, key = "'all'")
    public List<RestaurantResponse> getRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    private void evictRestaurantRelatedCaches(Long restaurantId) {
        redisTemplate.delete(Set.of(
                CacheNames.RESTAURANTS + "::all",
                CacheNames.MENUS + "::" + restaurantId
        ));
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
    }
}
