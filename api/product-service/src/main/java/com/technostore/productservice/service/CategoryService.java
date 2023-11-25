package com.technostore.productservice.service;

import com.technostore.productservice.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getPopularCategories();
}
