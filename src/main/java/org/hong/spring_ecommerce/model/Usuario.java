package org.hong.spring_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "username")
    @NotEmpty(message = "Campo requerido")
    private String username;

    @Column(name = "email")
    @NotBlank(message = "Campo requerido")
    @Email(message = "Email debe ser válido")
    private String email;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private List<Orden> ordenes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();

    public Usuario(Long id, String nombre, String username, String email, String direccion, String telefono, String tipo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipo = tipo;
        this.password = password;
    }

    public Usuario(String nombre, String username, String email, String direccion, String telefono, String tipo, String password) {
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipo = tipo;
        this.password = password;
    }
}
