package com.pluralsight.cmdline.bean;

import java.util.Date;

public class Category {
    int categoryId;
    String code, name, description, addedBy;
    Date dateAdded;

    public Category(){}

    public Category(int categoryId, String code, String name, String description, String addedBy) {
        this.categoryId = categoryId;
        this.code = code;
        this.name = name;
        this.description = description;
        this.addedBy = addedBy;
    }

    public Category(int categoryId, String code, String name, String description, String addedBy, Date dateAdded) {
        this(categoryId, code, name, description, addedBy);
        this.dateAdded = dateAdded;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public Date getDateAdded() {
        return dateAdded;
    }
}
