package gift.service;

import gift.domain.category.Category;
import gift.domain.category.CategoryRepository;
import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void saveCategory(CategoryRequestDto request) {
        categoryRepository.save(new Category(request.name(), request.color(), request.description(), request.imageUrl()));
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryResponseDto::new);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getSingleCategory(long id) {
        Category category = getCategory(id);
        return new CategoryResponseDto(category);
    }

    @Transactional
    public CategoryResponseDto editCategory(long id, CategoryRequestDto request) {
        Category category = getCategory(id);
        category.update(request.name(), request.color(), request.description(), request.imageUrl());
        return new CategoryResponseDto(category);
    }

    @Transactional
    public void deleteCategory(long id) {
        Category category = getCategory(id);
        categoryRepository.delete(category);
    }

    private Category getCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CATEGORY, id));
    }
}
