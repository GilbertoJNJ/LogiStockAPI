package com.gilberto.logistockapi.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "add_address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "add_id")
  private Long id;
  
  @Column(name = "add_street", nullable = false)
  private String street;
  
  @Column(name = "add_number", nullable = false)
  private String number;
  
  @Column(name = "add_district", nullable = false)
  private String district;
  
  @Column(name = "add_city_name", nullable = false)
  private String cityName;
  
  @Column(name = "add_state_name", nullable = false)
  private String stateName;
  
  @Column(name = "add_postal_code", nullable = false)
  private String postalCode;
  
  @Column(name = "add_complement")
  private String complement;
  
}
