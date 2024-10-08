package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse{

    private static Warehouse instance;
    private final String name;
    private final List<ProductRecord> productRecords;
    private final Set<ProductRecord> changedProducts;
    private java.util.Collections Collections;

    private Warehouse(String name) {
        this.name = name;
        this.productRecords = new ArrayList<>();
        this.changedProducts = new HashSet<>();
    }
    public boolean isEmpty() {
        return productRecords.isEmpty();
    }

    public static Warehouse getInstance(String myStore) {
        if (instance == null) {
            instance = new Warehouse(myStore);
        }else if (!Objects.equals(instance.getName(), myStore)) {
            instance = new Warehouse(myStore);
        }
        return instance;
    }
    public static Warehouse getInstance() {
        return instance = new Warehouse("myStore");
    }

    public String getName() {
        return name;
    }

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (id== null) {
            id = UUID.randomUUID();
        }else {
            for (ProductRecord product : productRecords) {
                if (product.uuid().equals(id)) {
                    throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
                }
            }
        }
        if (price == null) {
            price = BigDecimal.ZERO;
        }

        ProductRecord product = new ProductRecord(id, name, category, price);
        productRecords.add(product);
        return product;
    }
    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(productRecords);
    }
    public ProductRecord getProducts(UUID uuid, String name, Category category, BigDecimal price) {
        ProductRecord product = new ProductRecord(uuid, name, category, price);
        productRecords.add(product);
        return product;
    }

    public List<ProductRecord> getProductsBy(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null");
        }
        ArrayList<ProductRecord> products = new ArrayList<>();
        for (ProductRecord product : productRecords) {
            if (product.category().equals(category)) {
                products.add(product);
            }
        }
        return Collections.unmodifiableList(products);
    }

    public Optional<ProductRecord> getProductById(UUID id) {
        Optional<ProductRecord> productOptional;
        for (ProductRecord product : productRecords) {
            if (product.uuid().equals(id)) {
                return productOptional = Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public Set<ProductRecord> getChangedProducts() {
        return Collections.unmodifiableSet(changedProducts);
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        Map<Category, List<ProductRecord>> groupedProducts = new HashMap<>();

        for (ProductRecord product : getProducts()) {

            groupedProducts.computeIfAbsent(product.category(), k -> new ArrayList<>()).add(product);
        }

        return groupedProducts;
    }

    public ProductRecord updateProductPrice(UUID uuid, BigDecimal newPrice) {
        Optional<ProductRecord> productOptional = getProductById(uuid);
        if (productOptional.isEmpty()){
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }
        ProductRecord product = productOptional.get();
        productRecords.remove(product);
        changedProducts.add(product);
        product=new ProductRecord(product.uuid(), product.name(), product.category(), newPrice);
        productRecords.add(product);
        return product;
    }

}