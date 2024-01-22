package com.gilberto.logistockapi.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "sup_supplier")
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sup_id")
  private Long id;
  
  @Column(name = "sup_name", nullable = false)
  private String name;
  
  @Column(name = "sup_legal_document", nullable = false, unique = true)
  private String legalDocument;
  
  @Column(name = "sup_email")
  private String email;
  
  @Column(name = "sup_phone")
  private String phone;
  
  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  @JoinColumn(name = "sup_address_id")
  private Address address;
  
}
