package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.List;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    IClienteService iClienteService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        //Factura factura = iClienteService.findFacturaById(id);
        Factura factura = iClienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id);
        if (factura == null) {
            redirectAttributes.addFlashAttribute("error", "La factura no existe en los registros");
            return "redirect:/listar";
        }
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));

        return "factura/ver";
    }

    @GetMapping("/form/{idCliente}")
    public String crear(@PathVariable(value = "idCliente") Long idCliente, Model model, RedirectAttributes flash) {
        Cliente cliente = iClienteService.findOne(idCliente);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe");
            return "redirect: /listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Agregar una factura");
        return "factura/form";
    }

    @GetMapping(value = "/cargarProductos/{term}", produces = {"application/json"})
    public @ResponseBody
    List<Producto> cargarProductos(@PathVariable String term) {
        return iClienteService.buscarPorNombre(term);
    }

    @PostMapping(value = "/form")
    public String guardar
            (@Valid
                     Factura factura,
             BindingResult bindingResult,
             Model model,
             @RequestParam(name = "item_id[]", required = false) Long[] itemId,
             @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
             RedirectAttributes flash,
             SessionStatus sessionStatus
            ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("titulo", "Crear factura");
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Crear factura");
            model.addAttribute("error", "La factura no puedo estar vacia");
            return "factura/form";
        }

        for (int i = 0; i < itemId.length; i++) {
            Producto producto = iClienteService.findProductoById(itemId[i]);
            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);

            log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
        }
        iClienteService.saveFactura(factura);
        sessionStatus.setComplete();
        flash.addFlashAttribute("success", "Factura registrada exitosamente");
        return "redirect:/ver/" + factura.getCliente().getId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Factura factura = iClienteService.findFacturaById(id);
        if (factura == null) {
            redirectAttributes.addFlashAttribute("error", "La factura no existe");
            return "redirect:/ver/".concat(factura.getId().toString());
        }
        iClienteService.deleteFactura(id);
        redirectAttributes.addFlashAttribute("success", "La factura se ha eliminado");
        return "redirect:/ver/".concat(factura.getId().toString());
    }
}
