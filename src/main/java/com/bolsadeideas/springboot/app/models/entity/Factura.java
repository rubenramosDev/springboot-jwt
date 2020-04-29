package com.bolsadeideas.springboot.app.models.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String descripcion;
    private String observacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    /*Muchas facturas pueden tener un solo cliente.
     * Existen dos tipo, Lazy, eager
     * Lazy, solo busc en la BD solo la busqueda que se le pidio, mientra que eager, trae
     * mas datos de los que se solicitan*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Cliente cliente;

    /*Se agrega la anotacion JoinColum para generar la llave
     * foranea 'factura_id' en la tabla 'factura_items'*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    private List<ItemFactura> items;

    public Factura() {
        this.items = new ArrayList<ItemFactura>();
    }

    /*Se agrega este metodo que genera la fecha antes de hacer persistencia en la BD*/
    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public void addItemFactura(ItemFactura item) {
        this.items.add(item);
    }

    public Double getTotal() {
        Double total = 0.0;
        for (ItemFactura item : items) {
            total += item.calcularImporte();
        }
        return total;
    }

    @XmlTransient
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    private static final long serialVersionUID = 1L;

}
