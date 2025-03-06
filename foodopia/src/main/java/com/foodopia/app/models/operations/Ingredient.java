package com.foodopia.app.models.operations;

import com.foodopia.app.domain.ICostCalculable;
import com.foodopia.app.security.OperatorAccess;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "ingredients")
public class Ingredient implements ICostCalculable {
    @Id
    private String id;
    private String name;
    private String category;
    private double unitPrice;
    private String unitMeasure;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    @Override
    public double calculateCost() {
        return unitPrice;
    }

    // Getters - accessible to all users
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public boolean isActive() {
        return isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    // Setters - accessible only to ADMIN and OPERATOR roles
    @OperatorAccess
    public void setId(String id) {
        this.id = id;
    }

    @OperatorAccess
    public void setName(String name) {
        this.name = name;
    }

    @OperatorAccess
    public void setCategory(String category) {
        this.category = category;
    }

    @OperatorAccess
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @OperatorAccess
    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    @OperatorAccess
    public void setActive(boolean active) {
        isActive = active;
    }

    @OperatorAccess
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @OperatorAccess
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}