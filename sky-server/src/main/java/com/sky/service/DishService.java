package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.stereotype.Service;

/**
 * 新增菜品和对应的口味
 */
@Service
public interface DishService {
    public void saveWithFlavor(DishDTO dishDTO);
}
