package ru.java.creational.prototype;

import java.util.Objects;


class CloneableSheep implements Cloneable {
  private String name;

  CloneableSheep(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  @Override
  public CloneableSheep clone() throws CloneNotSupportedException {
    CloneableSheep sheep = (CloneableSheep)super.clone();
    // ...
    return sheep;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CloneableSheep sheep = (CloneableSheep) o;
    return Objects.equals(name, sheep.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
