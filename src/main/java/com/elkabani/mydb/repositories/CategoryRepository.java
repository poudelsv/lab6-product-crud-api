package com.elkabani.mydb.repositories;

import com.elkabani.mydb.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}