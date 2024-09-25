package com.example.mobilele.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {
  private String name;

  public Brand() {
  }

  @Column(unique = true, nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
