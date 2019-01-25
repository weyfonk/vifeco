package org.laeq.model;

import java.util.Objects;

public class Category {
    private String name;
    private String icon;
    private String shortcut;

    public Category() {
    }

    public Category(String name, String icon, String shortcut) {
        this.name = name;
        this.icon = icon;
        this.shortcut = shortcut;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getShortcut() {
        return shortcut;
    }
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) &&
                Objects.equals(icon, category.icon) &&
                Objects.equals(shortcut, category.shortcut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, icon, shortcut);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}