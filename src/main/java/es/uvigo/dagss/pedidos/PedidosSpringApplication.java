package es.uvigo.dagss.pedidos;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uvigo.dagss.pedidos.daos.AlmacenDao;
import es.uvigo.dagss.pedidos.daos.ArticuloAlmacenDao;
import es.uvigo.dagss.pedidos.daos.ArticuloDao;
import es.uvigo.dagss.pedidos.daos.ClienteDao;
import es.uvigo.dagss.pedidos.daos.FamiliaDao;
import es.uvigo.dagss.pedidos.daos.PedidoDao;
import es.uvigo.dagss.pedidos.entidades.Almacen;
import es.uvigo.dagss.pedidos.entidades.Articulo;
import es.uvigo.dagss.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.dagss.pedidos.entidades.Cliente;
import es.uvigo.dagss.pedidos.entidades.Direccion;
import es.uvigo.dagss.pedidos.entidades.Familia;
import es.uvigo.dagss.pedidos.entidades.LineaPedido;
import es.uvigo.dagss.pedidos.entidades.Pedido;

@SpringBootApplication
public class PedidosSpringApplication implements CommandLineRunner {
	@Autowired
	FamiliaDao familiaDao;

	@Autowired
	ArticuloDao articuloDao;

	@Autowired
	ClienteDao clienteDao;

	@Autowired
	PedidoDao pedidoDao;

	@Autowired
	AlmacenDao almacenDao;

	@Autowired
	ArticuloAlmacenDao articuloAlmacenDao;

	public static void main(String[] args) {
		SpringApplication.run(PedidosSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		crearEntidades();
		consultarEntidades();
	}

	// Mantienen referencias al Almacen "principal" y al Cliente "Pepe", creados en "crearEntidades()", para ser usadas en "consultarEntidades()"
	private Almacen almacenPrincipal;  
	private Cliente clientePepe;

	private void crearEntidades() {
		Familia f1 = new Familia("tubos", "tubos de todas clases");
		Familia f2 = new Familia("tuercas", "tuercas de todas clases");
		f1 = familiaDao.save(f1);
		f2 = familiaDao.save(f2);

		Articulo a1 = new Articulo("tubo acero", "tubo de acero", f1, 10.0);
		Articulo a2 = new Articulo("tubo plastico", "tubo de plastico", f1, 5.0);
		Articulo a3 = new Articulo("tuerca acero", "tuerca de acero 10/18", f2, 10.0);
		Articulo a4 = new Articulo("tuerca plástico", "tuerca de plástico", f2, 5.0);
		a1 = articuloDao.save(a1);
		a2 = articuloDao.save(a2);
		a3 = articuloDao.save(a3);
		a4 = articuloDao.save(a4);

		Direccion d1 = new Direccion("calle 1", "Ourense", "11111", "Ourense", "988111111");
		Direccion d2 = new Direccion("calle 2", "Santiago", "22222", "A Coruña", "981222222");

		Cliente c1 = new Cliente("11111111A", "Pepe Cliente1 Cliente1", d1);
		Cliente c2 = new Cliente("22222222A", "Ana Cliente2 Cliente2", d2);
		c1 = clienteDao.save(c1);
		c2 = clienteDao.save(c2);
		clientePepe = c1; // Guarda referencia

		Direccion d3 = new Direccion("calle 3", "Santiago", "33333", "A Coruña", "981333333");
		Almacen a = new Almacen("principal", "almacen principal", d3);
		a = almacenDao.save(a);
		almacenPrincipal = a; // Guarda referencia

		ArticuloAlmacen aa1 = new ArticuloAlmacen(a1, a, 10);
		ArticuloAlmacen aa2 = new ArticuloAlmacen(a2, a, 15);
		ArticuloAlmacen aa3 = new ArticuloAlmacen(a3, a, 20);
		ArticuloAlmacen aa4 = new ArticuloAlmacen(a4, a, 25);
		aa1 = articuloAlmacenDao.save(aa1);
		aa2 = articuloAlmacenDao.save(aa2);
		aa3 = articuloAlmacenDao.save(aa3);
		aa4 = articuloAlmacenDao.save(aa4);

		Pedido p1 = new Pedido(Calendar.getInstance().getTime(), c1);
		p1.anadirLineaPedido(new LineaPedido(p1, 2, a1, a1.getPrecioUnitario()));
		p1.anadirLineaPedido(new LineaPedido(p1, 2, a2, a2.getPrecioUnitario()));
		p1.anadirLineaPedido(new LineaPedido(p1, 2, a3, a3.getPrecioUnitario()));
		p1 = pedidoDao.save(p1);

		Pedido p2 = new Pedido(Calendar.getInstance().getTime(), c1);
		p2.anadirLineaPedido(new LineaPedido(p2, 100, a1, a1.getPrecioUnitario()));
		p2.anadirLineaPedido(new LineaPedido(p2, 100, a2, a2.getPrecioUnitario()));
		p2 = pedidoDao.save(p2);

	}

	private void consultarEntidades() {
		System.out.println("[PRUEBA SPRING]: -------------------");
		List<Articulo> articulos = articuloDao.findAll();
		System.out.println("[PRUEBA SPRING]: Todos los Articulos");
		for (Articulo a : articulos) {
			System.out.println("[PRUEBA SPRING]:    " + a);
		}
		System.out.println("[PRUEBA SPRING]: -----------");

		System.out.println("[PRUEBA SPRING]: Todos los tubos:");
		List<Familia> familiasTubos = familiaDao.findByPatronDescripcion("tubo");
		for (Familia f : familiasTubos) {
			List<Articulo> tubos = articuloDao.findByFamiliaId(f.getId());
			for (Articulo a : tubos) {
				System.out.println("[PRUEBA SPRING]:    " + a);
			}
		}
		System.out.println("[PRUEBA SPRING]: -----------");

		System.out.println("[PRUEBA SPRING]: Stock en almacen principal "+almacenPrincipal.getNombre());	
		List<ArticuloAlmacen> stocks = articuloAlmacenDao.findByArticuloId(almacenPrincipal.getId());
		for (ArticuloAlmacen stock : stocks) {
			System.out.println("[PRUEBA SPRING]:    "+stock.getArticulo()+" [Unidades="+stock.getStock()+"]");
		}
		System.out.println("[PRUEBA SPRING]: -----------");

		System.out.println("[PRUEBA SPRING]: Pedidos de "+clientePepe.getNombre());	
		List<Pedido> pedidos = pedidoDao.findByClienteDNI(clientePepe.getDNI());
		for (Pedido p : pedidos) {
			System.out.println("[PRUEBA SPRING]:    "+p);
			Pedido pConLineas = pedidoDao.findPedidoConLineas(p.getId());
			for (LineaPedido lp : pConLineas.getLineas()) {
				System.out.println("[PRUEBA SPRING]:      - "+lp);
			}
			System.out.println("[PRUEBA SPRING]:    ---");
		}
		System.out.println("[PRUEBA SPRING]: -----------");
	}

}
