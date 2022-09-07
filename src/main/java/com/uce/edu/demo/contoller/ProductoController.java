package com.uce.edu.demo.contoller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uce.edu.demo.repository.modelo.Persona;
import com.uce.edu.demo.repository.modelo.Producto;
import com.uce.edu.demo.repository.modelo.to.ProductoStock;
import com.uce.edu.demo.service.IGestorVentaService;
import com.uce.edu.demo.service.IProductoService;

@Controller
@RequestMapping("http://localhost:8080/productos")
public class ProductoController {

	@Autowired
	private IProductoService iProductoService;

	@Autowired
	private IGestorVentaService gestorVentaService;

	// GET
	@GetMapping("/buscar")
	public String buscarTodos(Model modelo) {

		List<Producto> lista = this.gestorVentaService.reporteVentas();
		modelo.addAttribute("productos", lista);
		return "vistaVentas";
	}

	@PostMapping("/insertar")
	public String insertarProducto(Producto producto) {
		this.gestorVentaService.insertarProducto(producto);
		return "redirect:/productos/buscar";
	}

	@GetMapping("/buscarParametro/{codioBarras}")
	public String buscarPersona(@PathVariable("codigoBarras") String codigo, Model modelo) {
		System.out.println("El Codigo: " + codigo);
		ProductoStock pro = this.gestorVentaService.consultarStock(codigo);
		modelo.addAttribute("producto", pro);
		return "vistaProducto";
	}

}