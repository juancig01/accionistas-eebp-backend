package com.eebp.accionistas.backend.accionistas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bancos")
public class Banco {

    @Column(name = "codbanco")
    @Id
    private Integer codBanco;

    @Column(name = "nombanco")
    private String nomBanco;
}
