package com.uce.edu.demo.repository.modelo.to;

public class ProductoCompra {

	private String codigoBarras;
	private Integer cantidad;

	// GET y SET
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "ProductoCompra [codigoBarras=" + codigoBarras + ", cantidad=" + cantidad + "]";
	}
}
