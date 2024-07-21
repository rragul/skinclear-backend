package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.ProductDTO;
import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.repository.ProductRepository;
import com.skinclear.skinclearbackend.resource.SimilarProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientService ingredientService;
    private final BrandService brandService;
    private final S3Service s3Service;

    public ProductService(ProductRepository productRepository, IngredientService ingredientService, BrandService brandService, S3Service s3Service) {
        this.productRepository = productRepository;
        this.ingredientService = ingredientService;
        this.brandService = brandService;
        this.s3Service = s3Service;
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void addProduct(ProductDTO productDTO, MultipartFile image) {
        Product product = createProductFromProductDTO(productDTO);
        String fileName = productDTO.getName() + "." + image.getOriginalFilename().split("\\.")[1];
        String imageUrl = s3Service.uploadFile(image,fileName);
        product.setImage(imageUrl);
        productRepository.save(product);
    }

    private Product createProductFromProductDTO(ProductDTO productDTO) {
        List<Long> ingredientIds = Arrays.stream(productDTO.getIngredientsIDS()).toList();

        List<Ingredient> ingredients = ingredientService.getIngredientsByIds(ingredientIds);
        Brand brand = brandService.getBrandById(productDTO.getBrandId());

        Product product = initializeProductWithDefaults(productDTO, brand, ingredients);

        ingredients.forEach(ingredient -> {
            updateProductBasedOnIngredient(product, ingredient);
        });

        return product;
    }

    private Product initializeProductWithDefaults(ProductDTO productDTO, Brand brand, List<Ingredient> ingredients) {
        Product product = new Product();

        product.setAlcoholFree(true);
        product.setSiliconeFree(true);
        product.setFragranceFree(true);
        product.setSulfateFree(true);
        product.setParabenFree(true);
        product.setOilFree(true);
        product.setFungalAcneSafe(true);
        product.setEuAllergenFree(true);
        product.setReefSafe(true);

        product.setName(productDTO.getName());
        product.setType(productDTO.getType());
        product.setLikeCount(productDTO.getLikeCount());
        product.setDislikeCount(productDTO.getDislikeCount());
        product.setWhatItIs(productDTO.getWhatItIs());
        product.setSpfRating(productDTO.getSpfRating());
        product.setVegan(productDTO.isVegan());
        product.setCategory(productDTO.getCategory());
        product.setSubcategory(productDTO.getSubcategory());
        product.setBrand(brand);
        product.setIngredients(ingredients);

        return product;
    }

    private void updateProductBasedOnIngredient(Product product, Ingredient ingredient) {
        ingredient.getWhatItIs().forEach(whatItIs -> {
            if (whatItIs.getName().equals("Alcohol") || whatItIs.getName().equals("Fatty Alcohol")) {
                product.setAlcoholFree(false);
            }
            if (whatItIs.getName().equals("Silicon")) {
                product.setSiliconeFree(false);
            }
            if (whatItIs.getName().equals("Fragrance")) {
                product.setFragranceFree(false);
            }
            if (whatItIs.getName().equals("Sulfate")) {
                product.setSulfateFree(false);
            }
            if (whatItIs.getName().equals("Paraben")) {
                product.setParabenFree(false);
            }
            if (whatItIs.getName().equals("Oil")) {
                product.setOilFree(false);
            }
        });

        ingredient.getConcern().forEach(concern -> {
            if (concern.getName().equals("Fungal Acne")) {
                product.setFungalAcneSafe(false);
            }
            if (concern.getName().equals("Coral Reefs")) {
                product.setReefSafe(false);
            }
        });

        ingredient.getWhatItIs().forEach(whatItIs -> {
            if (whatItIs.getName().equals("Preservative") || whatItIs.getName().equals("Paraben") || whatItIs.getName().equals("Fragrance")) {
                product.setEuAllergenFree(false);
            }
        });
    }

    public void updateProduct(Long id, ProductDTO productDTO, MultipartFile image) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        String imageUrl = existingProduct.getImage();
        if (image != null) {
            String fileName = productDTO.getName() + "." + image.getOriginalFilename().split("\\.")[1];
            imageUrl = s3Service.uploadFile(image,fileName);
        }
        Product product = createProductFromProductDTO(productDTO);
        product.setId(id);
        product.setImage(imageUrl);
        productRepository.save(product);
    }

    public void deleteProduct(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    public List<Product> getRecommendation(Long ingredientId) {
        Ingredient ingredient = ingredientService.getIngredientById(ingredientId);
        return productRepository.findProductByIngredients(ingredient);

    }

    public Object getAllProductWithPagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC,  "name");
        return productRepository.findAll(PageRequest.of(page, size, sort));
    }

    public Object getProductsBySubCategoryNameWithPagination(String subCategory, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC,  "name");
        return productRepository.findProductBySubcategory(subCategory, PageRequest.of(page, size, sort));
    }

    public Page<Product> getProductsByFilter(String subCategory, String preference, String benefits, String type, String ingredient, String brand, int page, int size) {
        if (subCategory.isEmpty() && preference.isEmpty() && benefits.isEmpty() && type.isEmpty() && ingredient.isEmpty() && brand.isEmpty()) {
            return productRepository.findAll(PageRequest.of(page, size));
        }
        List<String> preferenceList = preference != null && !preference.isEmpty() ? Arrays.asList(preference.split("\\s*,\\s*")) : new ArrayList<>();
        List<String> ingredientList = ingredient != null && !ingredient.isEmpty() ? Arrays.asList(ingredient.split("\\s*,\\s*")) : new ArrayList<>();
       // List<String> benefitList = benefits != null && !benefits.isEmpty() ? Arrays.asList(benefits.split("\\s*,\\s*")) : new ArrayList<>();
        return productRepository.findProductsByFilter(subCategory, preferenceList, benefits, type, ingredientList, brand, PageRequest.of(page, size));
    }

    public List<Product> searchProduct(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return productRepository.findFirst10();
        }
        return productRepository.findTop10ByNameStartingWithIgnoreCase(keyword);
    }

    public Product getProductByName(String name) {
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + name));
    }

    public List<SimilarProductResponse> getSimilarProducts(Long productId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Product> similarProducts = productRepository.findSimilarProducts(productId, pageable);

        Product targetProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Extract target product attributes and ingredients
        Set<Ingredient> targetIngredients = new HashSet<>(targetProduct.getIngredients());

        // Define Boolean attributes
        boolean targetVegan = targetProduct.isVegan();
        boolean targetAlcoholFree = targetProduct.isAlcoholFree();
        boolean targetFragranceFree = targetProduct.isFragranceFree();
        boolean targetSiliconeFree = targetProduct.isSiliconeFree();
        boolean targetSulfateFree = targetProduct.isSulfateFree();
        boolean targetParabenFree = targetProduct.isParabenFree();
        boolean targetOilFree = targetProduct.isOilFree();
        boolean targetFungalAcneSafe = targetProduct.isFungalAcneSafe();
        boolean targetEuAllergenFree = targetProduct.isEuAllergenFree();
        boolean targetReefSafe = targetProduct.isReefSafe();

        // Total number of Boolean attributes
        int totalBooleanAttributes = 10;

        return similarProducts.stream().map(product -> {
            // Calculate ingredient similarity using Jacquard coefficient
            Set<Ingredient> productIngredients = new HashSet<>(product.getIngredients());
            int intersectionSize = (int) targetIngredients.stream()
                    .filter(productIngredients::contains)
                    .count();
            int unionSize = (int) (targetIngredients.size() + productIngredients.size() - intersectionSize);
            double ingredientMatchPercentage = unionSize > 0 ? (double) intersectionSize / unionSize * 100 : 0;

            // Calculate Boolean attribute similarity
            int booleanAttributeMatchCount = 0;
            if (targetVegan == product.isVegan()) booleanAttributeMatchCount++;
            if (targetAlcoholFree == product.isAlcoholFree()) booleanAttributeMatchCount++;
            if (targetFragranceFree == product.isFragranceFree()) booleanAttributeMatchCount++;
            if (targetSiliconeFree == product.isSiliconeFree()) booleanAttributeMatchCount++;
            if (targetSulfateFree == product.isSulfateFree()) booleanAttributeMatchCount++;
            if (targetParabenFree == product.isParabenFree()) booleanAttributeMatchCount++;
            if (targetOilFree == product.isOilFree()) booleanAttributeMatchCount++;
            if (targetFungalAcneSafe == product.isFungalAcneSafe()) booleanAttributeMatchCount++;
            if (targetEuAllergenFree == product.isEuAllergenFree()) booleanAttributeMatchCount++;
            if (targetReefSafe == product.isReefSafe()) booleanAttributeMatchCount++;

            double attributeMatchPercentage = (double) booleanAttributeMatchCount / totalBooleanAttributes * 100;

            // Combine the ingredient and attribute match percentages (weighted if necessary)
            double combinedMatchPercentage = (ingredientMatchPercentage + attributeMatchPercentage) / 2;

            return new SimilarProductResponse(product, ingredientMatchPercentage,attributeMatchPercentage,combinedMatchPercentage);
        }).collect(Collectors.toList());
    }

}
