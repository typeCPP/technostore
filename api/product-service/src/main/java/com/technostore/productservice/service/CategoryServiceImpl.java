package com.technostore.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.technostore.productservice.model.Category;
import com.technostore.productservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static Set<String> POPULAR_CATEGORIES
            = Set.of("Ноутбуки", "Планшеты", "Фототехника",
            "Смартфоны", "Наушники", "Телевизоры",
            "Аксессуары", "Периферия", "Мониторы");

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getPopularCategories() {
        List<Category> result = new ArrayList<>();

        for (String name : POPULAR_CATEGORIES) {
            Optional<Category> categoryOptional = categoryRepository.findByName(name);
            categoryOptional.ifPresent(result::add);
        }

        return result;
    }
}
