package com.uce.edu.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.uce.edu.demo.repository.modelo.Producto;
import com.uce.edu.demo.repository.modelo.to.ProductoCompra;
import com.uce.edu.demo.repository.modelo.to.ProductoStock;
import com.uce.edu.demo.repository.modelo.to.ReporteVenta;

public interface IGestorVentaService {

	public void insertarProducto(Producto p);

	public void realizarVenta(List<ProductoCompra> productos, String cedulaCliente, String numVenta);

	public List<ReporteVenta> reporteVentas(LocalDateTime fecha, String categoria, Integer cantidad);

	public ProductoStock consultarStock(String codigoBarras);
}
