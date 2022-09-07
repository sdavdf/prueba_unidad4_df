package com.uce.edu.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Repository;

import com.uce.edu.demo.repository.modelo.Producto;
import com.uce.edu.demo.repository.modelo.to.ProductoStock;


@Repository
@Transactional
public class ProductoRepositoryImpl implements IProductoRepository{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(value = TxType.MANDATORY)
	public void insertar(Producto p) {
		// TODO Auto-generated method stub
		this.entityManager.persist(p);
	}

	@Override
	@Transactional(value = TxType.MANDATORY)
	public void actualizar(Producto p) {
		// TODO Auto-generated method stub
		this.entityManager.merge(p);
	}

	@Override
	@Transactional(value = TxType.NOT_SUPPORTED)
	public Producto buscarPorCodigoBarras(String codigoBarras) {
		// TODO Auto-generated method stub
		Query myQuery = this.entityManager.createNativeQuery("SELECT NEW com.uce.edu.demo.repository.modelo.to(p.codigo, p.nombre, p.categoria, p.stock) FROM producto WHERE p.codigo = :datoCodigo",
				ProductoStock.class);
		myQuery.setParameter("datoCodigo", codigoBarras);
		return (Producto) myQuery.getSingleResult();
	}

	@Override
	@Transactional(value = TxType.NOT_SUPPORTED)
	public List<String> consultarCodigosBarras() {
		// TODO Auto-generated method stub
		TypedQuery<String> myQuery = this.entityManager.createQuery("SELECT p.codigoBarras FROM Producto p",
				String.class);
		return myQuery.getResultList();
	}

}
