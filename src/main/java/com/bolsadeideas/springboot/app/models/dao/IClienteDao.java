package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

    /*Se agrega consulta personalizada para optimizar la entrada a base de datos por parte de JPA.
    * Se agrega left join para que se seleccionen los clientes independientemente si tienen o no
    * facturas, si no se hace esto, solo se mostraron los clientes que tienen factura.*/
    @Query("select c from Cliente c left join fetch c.facturas f where c.id = ?1")
    public Cliente fecthByIdWithFacturas(Long id);

}
