package org.hong.spring_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "detalles_orden")
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_orden")
    private Long id;
    private String nombre;
    private double cantidad;
    private double precio;
    private double total;



    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_orden")
    private Orden orden;

}
