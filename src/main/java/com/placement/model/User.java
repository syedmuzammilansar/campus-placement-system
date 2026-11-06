package com.placement.model;

import java.io.Serializable;

/**
 * UNIT-I & UNIT-II: Demonstrates Abstraction, Encapsulation,
 * Access Modifiers, and class hierarchy foundation.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Encapsulated private fields (Unit-I)
    private int id;
    private String name;
    private String email;

    // Default constructor
    public User() {
    }

    // Overloaded Constructor (Unit-I: Constructor Overloading, 'this' keyword)
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Abstract method (Unit-II: Abstraction - must be implemented by subclasses)
    public abstract void displayProfile();

    // Getters and Setters (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Email: " + email;
    }
}
