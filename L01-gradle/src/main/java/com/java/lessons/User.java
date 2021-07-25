package com.java.lessons;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class User {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("name", name)
                .add("hashCode", hashCode())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
