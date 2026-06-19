package dev.peacechan.foodverse.restaurant.service;

import dev.peacechan.foodverse.common.exception.ResourceNotFoundException;
import dev.peacechan.foodverse.entity.Restaurant;
import dev.peacechan.foodverse.repository.RestaurantRepository;
import dev.peacechan.foodverse.restaurant.dto.CreateRestaurantRequest;
import dev.peacechan.foodverse.restaurant.dto.RestaurantResponse;
import dev.peacechan.foodverse.restaurant.dto.UpdateRestaurantRequest;
import dev.peacechan.foodverse.restaurant.mapper.RestaurantMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Transactional
    public RestaurantResponse updateRestaurant(Long restaurantId, UpdateRestaurantRequest request) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurantMapper.updateEntity(restaurant, request);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurant(Long restaurantId) {
        return restaurantMapper.toResponse(getRestaurantById(restaurantId));
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
    }
}
