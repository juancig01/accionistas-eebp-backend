package com.eebp.accionistas.backend.accionistas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @Column(name = "COD_USUARIO")
    private String codUsuario;

    @Column(name = "tipdocumento")
    private String tipDocumento;

    @Column(name = "nompri")
    private String nomPri;

    @Column(name = "nomseg")
    private String nomSeg;

    @Column(name = "apepri")
    private String apePri;

    @Column(name = "apeseg")
    private String apeSeg;

    @Column(name = "tipopersona")
    private String tipoPersona;

    @Column(name = "razonsocial")
    private String razonSocial;

    @Column(name = "departamentoexp")
    private String departamentoExp;

    @Column(name = "municipioexp")
    private String municipioExp;

    @Column(name = "fecnacimiento")
    private String fecNacimiento;

    @Column(name = "genpersona")
    private String genPersona;

    @Column(name = "depnacimiento")
    private String depNacimiento;

    @Column(name = "lugnacimiento")
    private String lugNacimiento;

    @Column(name = "estcivpersona")
    private String estCivPersona;

    @Column(name = "celpersona")
    private String celPersona;

    @Column(name = "profpersona")
    private String profPersona;

    @Column(name = "actecopersona")
    private String actEcoPersona;

    @Column(name = "correopersona")
    private String correoPersona;

    @Column(name = "tipodirecciondomicilio")
    private String tipoDireccionDomicilio;

    @Column(name = "dirdomicilio")
    private String dirDomicilio;

    @Column(name = "departamentodomicilio")
    private String departamentoDomicilio;

    @Column(name = "municipiodomicilio")
    private String municipioDomicilio;

    @Column(name = "paisdomicilio")
    private String paisDomicilio;

    @Column(name = "telfdomicilio")
    private String telfDomicilio;

    @Column(name = "indteldomicilio")
    private String indTelDomicilio;

    @Column(name = "nomempresa")
    private String nomEmpresa;

    @Column(name = "tipodireccionlaboral")
    private String tipoDireccionLaboral;

    @Column(name = "dirlaboral")
    private String dirLaboral;

    @Column(name = "municipiolaboral")
    private String municipioLaboral;

    @Column(name = "departamentolaboral")
    private String departamentoLaboral;

    @Column(name = "paislaboral")
    private String paisLaboral;

    @Column(name = "telflaboral")
    private String telfLaboral;

    @Column(name = "extlaboral")
    private String extLaboral;

    @Column(name = "dircorrespondencia")
    private String dirCorrespondencia;

    @Column(name = "otradirlaboral")
    private String otraDirLaboral;

    @Column(name = "opcpotestad")
    private String opcPotestad;

    @Column(name = "huella")
    private byte[] huella;

    @Column(name = "huella2")
    private byte[] huella2;

    @Column(name = "firma")
    private byte[] firma;

    @Column(name = "tipovivienda")
    private String tipoVivienda;

    @Column(name = "numpersonas")
    private Integer numPersonas;

    @Column(name = "autorizacorreo")
    private Boolean autorizaCorreo;

    @Column(name = "autorizallamada")
    private Boolean autorizaLlamada;

    @Column(name = "autorizatodas")
    private Boolean autorizaTodas;

    @Column(name = "autorizamensaje")
    private Boolean autorizaMensaje;

    @Column(name = "autorizafisico")
    private Boolean autorizaFisico;

    @Column(name = "recursos")
    private String recursos;

    @Column(name = "ingresos")
    private String ingresos;

    @Column(name = "numsuscripcion")
    private String numSuscripcion;

    @Column(name = "barriolaboral")
    private String barrioLaboral;

    @Column(name = "barriodomicilio")
    private String barrioDomicilio;

    @Column(name = "numcuentabancaria")
    private BigInteger numCuentaBancaria;

    @Column(name = "tipocuentabancaria")
    private String tipoCuentaBancaria;

    @Column(name = "entidadbancaria")
    private String entidadBancaria;

    @Transient
    private String esAccionista;

}

