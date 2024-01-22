package com.gilberto.logistockapi.repositories;

import com.gilberto.logistockapi.models.entity.Supplier;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, Long> {
  
  Optional<Supplier> findByLegalDocument(String legalDocument);
  
}
