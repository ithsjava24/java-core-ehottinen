package org.example.warehouse;

import java.util.ArrayList;

public class Category {
    private final String name;
    private static final ArrayList<Category> categories = new ArrayList<>();

    private Category(String name) {
        this.name = name;
        categories.add(this);
    }

    public String getName() {
        return name;
    }

    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1);;
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return new Category(name);
    }
}
