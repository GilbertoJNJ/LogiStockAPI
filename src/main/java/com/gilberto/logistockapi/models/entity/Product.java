package com.gilberto.logistockapi.models.entity;

import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.models.enums.MeasureUnit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@Table(name = "pro_product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pro_id")
  private Long id;
  
  @Column(name = "pro_name", nullable = false)
  private String name;
  
  @Column(name = "pro_barcode", nullable = false, unique = true)
  private String barCode;
  
  @Column(name = "pro_category", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Category category;
  
  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private Supplier supplier;
  
  @Column(name = "pro_unit_price", nullable = false)
  private BigDecimal unitPrice;
  
  @Column(name = "pro_measure_unit", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private MeasureUnit measureUnit;
  
  @Column(name = "pro_stock_quantity", nullable = false)
  private Integer stockQuantity;
  
  @Column(name = "pro_min_stock_level", nullable = false)
  private Integer minStockLevel;
  
  @Column(name = "pro_max_stock_level", nullable = false)
  private Integer maxStockLevel;
  
  @Column(name = "pro_entry_date", nullable = false)
  @CreationTimestamp
  private LocalDate entryDate;
  
  @Column(name = "pro_description")
  private String description;
  
  public Product(ProductForm form) {
    this.name = form.getName();
    this.barCode = form.getBarCode();
    this.category = form.getCategory();
    this.unitPrice = form.getUnitPrice();
    this.measureUnit = form.getMeasureUnit();
    this.stockQuantity = form.getStockQuantity();
    this.minStockLevel = form.getMinStockLevel();
    this.maxStockLevel = form.getMaxStockLevel();
    this.description = form.getDescription();
  }
  
}
