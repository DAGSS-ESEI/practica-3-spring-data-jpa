package es.uvigo.dagss.pedidos.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uvigo.dagss.pedidos.entidades.Cliente;

public interface ClienteDao extends JpaRepository<Cliente, String>{
	List<Cliente> findByNombreContaining(String nombre);
	List<Cliente> findByDireccionLocalidad(String localidad);
}
