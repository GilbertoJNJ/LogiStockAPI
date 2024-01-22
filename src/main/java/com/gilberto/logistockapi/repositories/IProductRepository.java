package com.gilberto.logistockapi.repositories;

import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.models.enums.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
  
  Optional<Product> findByBarCode(String barCode);
  
  @Query("select product from pro_product as product " +
      " where (product.category in :categories) " +
      "   and ((:search = '' OR CAST(product.id AS text) = :search) " +
      "     or (upper(product.barCode) like concat('%', upper(:search), '%')) " +
      "     or (upper(product.name) like concat('%', upper(:search), '%'))) " +
      " order by product.entryDate desc " +
      " limit :pageSize " +
      " offset :pageNumber")
  List<Product> findAllByFilters(List<Category> categories, String search, int pageSize,
                                 int pageNumber);
  
}