package org.hong.spring_ecommerce.repository;

import org.hong.spring_ecommerce.model.Orden;
import org.hong.spring_ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByUsuario(Usuario usuario);

}
