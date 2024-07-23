package org.hong.spring_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    private String nombre;

    private String descripcion;

    private String imagen;

    private double precio;

    private int cantidad;


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ToString.Exclude
    @OneToMany(mappedBy = "producto", orphanRemoval = true)
    private List<DetalleOrden> detallesOrden = new ArrayList<>();

}
