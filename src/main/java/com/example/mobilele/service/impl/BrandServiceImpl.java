package com.example.mobilele.service.impl;

import com.example.mobilele.model.entity.BrandEntity;
import com.example.mobilele.repository.BrandRepository;
import com.example.mobilele.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
  private final BrandRepository brandRepository;

  public BrandServiceImpl(BrandRepository brandRepository) {
    this.brandRepository = brandRepository;
  }

  @Override
  public void initBrands() {
    if (this.brandRepository.count() == 0) {
      BrandEntity bmw = new BrandEntity();
      bmw.setName("bmw");

      this.brandRepository.save(bmw);
    }
  }

  @Override
  public Optional<BrandEntity> findBrandByName(String name) {
    return this.brandRepository.findBrandByNameIgnoreCase(name);
  }
}
