package com.example.server.repository;

import com.example.server.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM Product p " +
            "WHERE (:categories IS NULL OR p.category IN :categories) " +
            "AND (:brands IS NULL OR p.brand IN :brands)")
    List<Product> findWithFilters(
            @Param("categories") List<String> categories,
            @Param("brands") List<String> brands,
            Sort sort
    );
}
