package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {

    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> buscarPorNombre(String termino);

    /* Esta es otra forma de implementar el metodo, utilizando el tipado de
    JPA..
    public List<Producto> findByNombreLikeIgnoreCase(String termino);
    */
}
