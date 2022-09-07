package com.uce.edu.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.demo.repository.IDetalleVentaRepository;
import com.uce.edu.demo.repository.IProductoRepository;
import com.uce.edu.demo.repository.IVentaRepository;
import com.uce.edu.demo.repository.modelo.DetalleVenta;
import com.uce.edu.demo.repository.modelo.Producto;
import com.uce.edu.demo.repository.modelo.Venta;
import com.uce.edu.demo.repository.modelo.to.ProductoCompra;
import com.uce.edu.demo.repository.modelo.to.ProductoStock;
import com.uce.edu.demo.repository.modelo.to.ReporteVenta;

@Service
public class GestorServiceImpl implements IGestorVentaService{

	@Autowired
	private IProductoRepository iProductoRepository;

	@Autowired
	private IVentaRepository iVentaRepository;

	@Autowired
	private IDetalleVentaRepository iDetalleVentaRepository;

	@Override
	@Transactional(value = TxType.REQUIRED)
	public void insertarProducto(Producto p) {
		// TODO Auto-generated method stub
		List<String> codigos = this.iProductoRepository.consultarCodigosBarras();

		if (codigos.contains(p.getCodigoBarras())) {
			Producto productoBase = this.iProductoRepository.buscarPorCodigoBarras(p.getCodigoBarras());
			productoBase.setStock(productoBase.getStock() + p.getStock());

			this.iProductoRepository.actualizar(productoBase);
		} else
			this.iProductoRepository.insertar(p);
	}

	@Override
	@Transactional(value = TxType.REQUIRED)
	public void realizarVenta(List<ProductoCompra> productos, String cedulaCliente, String numVenta) {
		// TODO Auto-generated method stub
		Venta v = new Venta();
		v.setNumero(numVenta);
		v.setFecha(LocalDateTime.now());
		v.setCedulaCliente(cedulaCliente);

		Function<ProductoCompra, DetalleVenta> generarDetalle = p -> {
			Producto productoBusc = this.iProductoRepository.buscarPorCodigoBarras(p.getCodigoBarras());

			if (productoBusc.getStock() >= p.getCantidad()) {
				productoBusc.setStock(productoBusc.getStock() - p.getCantidad());
				this.iProductoRepository.actualizar(productoBusc);

				DetalleVenta detalle = new DetalleVenta();

				detalle.setCantidad(p.getCantidad());
				detalle.setPrecioUnitario(productoBusc.getPrecio());
				detalle.setSubtotal(productoBusc.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));

				detalle.setVenta(v);
				detalle.setProducto(productoBusc);

				return detalle;
			}

			return null;
		};

		List<DetalleVenta> detalles = new ArrayList<DetalleVenta>();
		BigDecimal montoPagar = BigDecimal.ZERO;

		for (ProductoCompra pc : productos) {
			DetalleVenta dv = generarDetalle.apply(pc);

			if (dv == null)
				throw new RuntimeException("Stock sin productos");
			else {
				detalles.add(dv);
				montoPagar = montoPagar.add(dv.getSubtotal());
			}
		}

		v.setDetalles(detalles);
		v.setTotalVenta(montoPagar);

		this.iVentaRepository.insertar(v);
	}

	@Override
	@Transactional(value = TxType.REQUIRED)
	public List<ReporteVenta> reporteVentas(LocalDateTime fecha, String categoria, Integer cantidad) {
		List<DetalleVenta> detalles = this.iDetalleVentaRepository.consultarDetalles(fecha, categoria, cantidad);

		List<ReporteVenta> reportes = new ArrayList<>();

		Consumer<DetalleVenta> guardarReportes = d -> {
			ReporteVenta r = new ReporteVenta();
			r.setCodigoBarras(d.getProducto().getCodigoBarras());
			r.setCategoria(d.getProducto().getCategoria());
			r.setCantidad(d.getCantidad());
			r.setPrecioUnitario(d.getPrecioUnitario());
			r.setSubtotal(d.getSubtotal());

			reportes.add(r);
		};

		detalles.forEach(guardarReportes);

		return reportes;
	}

	@Override
	@Transactional(value = TxType.REQUIRED)
	public ProductoStock consultarStock(String codigoBarras) {
		Function<Producto, ProductoStock> obtenerStock = p -> {
			ProductoStock ps = new ProductoStock();
			ps.setCodigoBarras(p.getCodigoBarras());
			ps.setNombre(p.getNombre());
			ps.setCategoria(p.getCategoria());
			ps.setStock(p.getStock());

			return ps;
		};

		Producto producto = this.iProductoRepository.buscarPorCodigoBarras(codigoBarras);
		ProductoStock productoS = obtenerStock.apply(producto);

		return productoS;
	}
}
