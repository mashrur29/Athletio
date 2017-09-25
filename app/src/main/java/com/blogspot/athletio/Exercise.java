package com.blogspot.athletio;

/**
 * Created by tanvir on 8/26/17.
 */

public class Exercise {
    String title;
    String description;
    String category;

    public Exercise() {
    }

    public Exercise(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "{" +
                "title=" + title +
                ", description=" + description +
                ", category=" + category +
                '}';
    }
}
