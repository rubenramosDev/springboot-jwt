package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

    /*Se realiza esta consulta para ahorrar las consultas que hace a la base de datos JPA de forma
    * predeterminada... Y traer todos los objetos necesarios en una sola consulta.*/
    @Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id =?1")
    public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);
}
