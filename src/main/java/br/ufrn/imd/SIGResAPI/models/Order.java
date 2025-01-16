package br.ufrn.imd.SIGResAPI.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "orders")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isVariant;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_variants_id", nullable = true)
    @JsonManagedReference
    private ProductVariant productVariant;

    private int amount;

    @ManyToOne
    @JoinColumn(name = "desk_id", nullable = false)
    @JsonManagedReference
    private Desk desk;

    private boolean active;
    private String time;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @JsonManagedReference
    private User createdBy;
}
