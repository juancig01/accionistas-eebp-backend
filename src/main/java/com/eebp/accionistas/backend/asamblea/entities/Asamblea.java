package com.eebp.accionistas.backend.asamblea.entities;

import com.eebp.accionistas.backend.seguridad.entities.Asset;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asamblea")
public class Asamblea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer consecutivo;
    private String fechaAsamblea;
    private String horaAsamblea;
    private String estado;
    private String tipoAsamblea;

    @Transient
    private List<Asset> files;
}
