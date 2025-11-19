# Ejemplo Proyecto Spring 
> DAGSS 2025-2026, semana 3

- Ejemplo de creación de proyectos Spring Boot 
- Ejemplo de definición de repositorios Spring Data JPA

## Previo
### Requisitos previos

- Servidor de BD MySQL
- Maven (versión > 3.5.x)
- (opcional) GIT
- (opcional) IDE Java (VSCode, Eclipse, Netbeans, IntelliJ)
- (recomendable) extensión *Spring Boot Extension Pack* en VSCode.

**Nota:** En los equipos de laboratorio, puede ser conveniente establecer la variable de entorno JAVA_HOME, para que el comando `mvn` (Maven) compile y ejecute los proyectos siempre con el mismo JDK. 

```sh 
export JAVA_HOME=/usr/lib/jvm/jdk-20 

export PATH=$JAVA_HOME/bin:$PATH 
```

### Crear BD para los ejemplos  (si no se ha hecho antes)

* Crear BD "pruebas_dagss" en MySQL 

```
mysql -u root -p    [pedirá la contraseña de MySQL]

mysql> create database pruebas_dagss;
mysql> create user dagss@localhost identified by "dagss";
mysql> grant all privileges on pruebas_dagss.* to dagss@localhost;

```

Adicionalmente, puede ser necesario establecer un formato de fecha compatible
```
mysql> set @@global.time_zone = '+00:00';
mysql> set @@session.time_zone = '+00:00';
```

## CREAR PROYECTO SPRING BOOT
Existen varias alternativas:
- Crear un proyecto Maven vacío e incluir las dependencias de los _starters_ de Spring Boot
- Usar Spring Tool Suite ([https://spring.io/tools](https://spring.io/tools)) y crear un nuevo proyecto _Spring Starter project_ (disponible en extensión VSCode)
- Crear el proyecto desde _Spring Initializr_ ([https://start.spring.io/](https://start.spring.io/))

### Características del proyecto
```
Project: Maven Project
Language: Java
Spring Boot version: 3.4.11

Proyecto: 
   groupId: es.uvigo.dagss
   artefactId: ejemplo-spring
   package: es.uvigo.dagss.pedidos
   name: PedidosSpring

Packaging: Jar
Java version: 21
Configuration: Properties

Dependencias a incluir:
	- Spring Data JPA       # Carga dependencias de Spring DATA y Spring DATA JPA => autoconfiguración de @Repository, Datasource, etc
	- MySQLDriver           # Driver JDBC de MySQL/MariaDB
	- Lombok  (opc)         # Librería con utilidades para autogenerar funciones de JavaBeans (https://projectlombok.org/)
	- Spring Boot DevTools (opc) # Utilidades de desarrollo en tiempo de ejecución
```

Fichero `pom.xml` resultante

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.4.0</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>es.uvigo.dagss</groupId>
	<artifactId>ejemplo-spring</artifactId>
	<version>0.1-SNAPSHOT</version>
	<name>PedidosSpring</name>
	<description>Ejemplo Spring y Spring DATA JPA</description>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<image>
						<builder>paketobuildpacks/builder-jammy-base:latest</builder>
					</image>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>

```
El fichero `pom.xml` resultante:
- hereda del "super"-POM base de Spring Boot (version 3.4.12)
- declara la dependencias necesarias, incluido el _starter_ `spring-boot-starter-data-jpa` que habilita Spring DATA JPA.
- declara el _plugin_ `spring-boot-maven-plugin` que se encarga del empaquetado y ejecución de proyectos Spring Boot.

Clase de configuración resultante (en `src/main/java/es/uvigo/dagss/pedidos/PedidosSpringApplication`)
```java
package es.uvigo.dagss.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PedidosSpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(PedidosSpringApplication.class, args);
	}
}
```

La anotación `@SpringBootApplication` engloba a las anotaciones:

- `@ComponentScan`: búsqueda de _beans_ Spring anotados con `@Component` o derivados
- `@EnableAutoConfiguration`: habilita la "autoconfiguración" del _Spring Application Context_
  - Spring Boot se encargará de recorrer el classpath buscando las dependencias (_starters_) usadas en el proyecto e inicializando con valores por defecto los elementos que necesite la aplicación (conexión con BD, control de transacciones, servidor de Servlets, etc).

## CREAR REPOSITORIOS SPRING DATA JPA

### Copiar clases de entidades (se usarán mismas de `ejemplo-persistencia`)
Crear el directorio para el paquete `entidades` y copiar los ficheros Java con la definición de las entidades (disponibles en [package entidades de ejemplo-persistencia-24](https://github.com/esei-si-dagss/ejemplo-persistencia-24/tree/main/src/main/java/es/uvigo/dagss/pedidos/entidades))

```sh
mkdir -p src/main/java/es/uvigo/dagss/pedidos/entidades

cd src/main/java/es/uvigo/dagss/pedidos/entidades

wget https://raw.githubusercontent.com/esei-si-dagss/ejemplo-persistencia-24/main/src/main/java/es/uvigo/dagss/pedidos/entidades/{Articulo,Almacen,ArticuloAlmacen,ArticuloAlmacenId,Familia,Direccion,Cliente,Pedido,LineaPedido,EstadoPedido}.java

pushd
```


### Crear (descargar) los _interfaces_ de los repositorios `FamiliaDAO`, `ArticuloDAO`, `AlmacenDAO`, `ArticuloAlmacenDAO`, `ClienteDAO` y `PedidoDAO`.

#### Crear paquete `es.uvigo.dagss.pedidos.daos` y copiar código de los DAOs Spring DATA (sólo se define el interfaz).
```sh
mkdir -p src/main/java/es/uvigo/dagss/pedidos/daos
cd src/main/java/es/uvigo/dagss/pedidos/daos

wget https://raw.githubusercontent.com/esei-si-dagss/ejemplo-spring-24/main/src/main/java/es/uvigo/dagss/pedidos/daos/{AlmacenDAO,ArticuloAlmacenDAO,ArticuloDAO,ClienteDAO,FamiliaDAO,PedidoDAO}.java

pushd
```
La definición de los respectivos _interfaces_ será:
```java
public interface AlmacenDAO extends JpaRepository<Almacen, Long>{
	List<Almacen> findByNombreContaining(String nombre);
	List<Almacen> findByDireccionLocalidad(String localidad);

	@Query("SELECT aa.almacen FROM ArticuloAlmacen AS aa WHERE aa.articulo.id = :articuloId")
	List<Almacen> findByArticuloId(@Param("articuloId") Long articuloId);
}

public interface ArticuloAlmacenDAO extends JpaRepository<ArticuloAlmacen, ArticuloAlmacenId>{
	List<ArticuloAlmacen> findByAlmacenId(Long almacenId);
	List<ArticuloAlmacen> findByArticuloId(Long articuloId);
}

public interface ArticuloDAO extends JpaRepository<Articulo, Long>{
	public List<Articulo> findByFamiliaId(Long id);
	public List<Articulo> findByNombre(String nombre);
    
	@Query("SELECT a FROM Articulo a WHERE a.descripcion LIKE %?1%")
	public List<Articulo> findByPatronDescripcion(String patron);
}

public interface ClienteDAO extends JpaRepository<Cliente, String>{
	List<Cliente> findByNombreContaining(String nombre);
	List<Cliente> findByDireccionLocalidad(String localidad);
}

public interface FamiliaDAO extends JpaRepository<Familia, Long> {
	public List<Familia> findByNombre(String nombre);
    
	@Query("SELECT f FROM Familia f WHERE f.descripcion LIKE %:patron%")
	public List<Familia> findByPatronDescripcion(@Param("patron") String patron);
}

public interface PedidoDAO extends JpaRepository<Pedido, Long>{

	@Query("SELECT DISTINCT p FROM Pedido AS p JOIN FETCH p.lineas WHERE p.id = :id")
	public Pedido findPedidoConLineas(@Param("id") Long id);

	public List<Pedido> findByClienteDNI(String dni);
}
```
Todos los _interfaces_ heredan de `JpaRepository<T,K>`, por lo que tendrán implementaciones de los [métodos disponibles](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html) en ese _interface_ (`save()`, `findAll()`, etc), junto con las implementaciones de los _query methods_ adicionales añadidos a cada uno de los _interfaces_.
- Si no se aporta un `@Query` propio, Spring DATA JPA tratará de proporcionar una implementación de la consulta acorde al nombre del método (siempre que este siga las [convenciones de nombrado](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html) de Spring DATA).

Al heredar de `JpaRepository<T,K>` no es necesario marcar los interfaces con `@Repository` para que Spring los trate como _Repositorios/DAOs_.
- El mecanismo de 'autodescubrimiento' de _Repositorios_ de Spring DATA JPA, activado por Spring Boot al incluir el _starter_ `spring-boot-starter-data-jpa` en el `pom.xml`, localizará sus definiciones.
- Cuando se requiera su inyección vía `@Autowired` se generarán los correspondientes _proxies_ con la implementación adecuada.

Respecto a los _Repositorios_/DAOs creados en el ejemplo:

- El _Repositorio_/DAO `PedidoDAO` aprovecha que la relación 1:N entre `Pedido` y `LineaPedido` está anotada con `cascade = CascadeType.ALL` para realizar la gestión de las entidades `LineaPedido` junto con la entidad `Pedido` a la que pertenece cada línea de pedido, no siendo necesario contar con un `LineaPedidoDAO` propio.
  - Es decir, al guardar con el método `save()` un nuevo `Pedido` con una serie de `LineaPedido` vinculadas, se insertará en la BD una tupla en la tabla `Pedido` y las _n_ tuplas correspondientes en la tabla `LineaPedido`.
  - Al modificar un `Pedido` existente con el método `save()` del DAO, se actualizarán las respectivas `LineaPedido` que hayan sido modificadas, eliminando aquellas que ya no estén explícitamente vinculadas a esa entidad `Pedido` que se modifica (como resultado del `orphanRemoval=true` en la anotación `@OneToMany` de `Pedido`).
  - Al eliminar un `Pedido` existente con el método `delete()` del DAO, se eliminarán las respectivas `LineaPedido`.
- El resto de _Repository_/DAO gestionan únicamente la entidad que corresponde y las relaciones con otras entidades se recuperan implícitamente como consecuencia de los mapeos JPA con valor `fetch=FetchType.EAGER` (relaciones `@OneToOne` y `@ManyToOne`).

### Configurar _Datasource_

Editar fichero `src/main/resources/application.properties` y establecer los valores de conexión a la BD:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pruebas_dagss
spring.datasource.username=dagss
spring.datasource.password=dagss
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
# detalles en https://www.baeldung.com/hibernate-field-naming-spring-boot
```

A partir de esta configuración de conexión con la BD, la _autoconfiguración_ de SpringBoot como consecuencia de incluir el _starter_ `spring-boot-starter-data-jpa` en el `pom.xml`, creará e inicializará los correspondientes _Datasource_, _EntityManagerFactory_, _PersistenceUnit_ y el gestor de Transacciones.

## EJECUCIÓN DEL PROYECTO

### Crear `main()` de prueba [Modificar `PedidosSpringApplication` en paquete `es.uvigo.dagss.pedidos`] 

```sh
cd src/main/java/es/uvigo/dagss/pedidos

wget https://raw.githubusercontent.com/esei-si-dagss/ejemplo-spring-24/main/src/main/java/es/uvigo/dagss/pedidos/PedidosSpringApplication.java

pushd
```

El nuevo contenido será:
```java
package es.uvigo.dagss.pedidos;
...

@SpringBootApplication
public class PedidosSpringApplication implements CommandLineRunner {
	@Autowired
	FamiliaDAO familiaDAO;

	@Autowired
	ArticuloDAO articuloDAO;

	@Autowired
	ClienteDAO clienteDAO;

	@Autowired
	PedidoDAO pedidoDAO;

	@Autowired
	AlmacenDAO almacenDAO;

	@Autowired
	ArticuloAlmacenDAO articuloAlmacenDAO;
	
	public static void main(String[] args) {
		SpringApplication.run(PedidosSpringApplication.class, args);
	}
    
	@Override
	public void run(String... args) throws Exception {
		crearEntidades();
		consultarEntidades();
	}
	
	private void crearEntidades() {...}
	private void consultarEntidades() {...}	
}
```
En este _main()_:
- La clase implementa el _interface_ `CommandLineRunner`, que permite la ejecución desde línea de comandos del método `run()`.
- Con `@Autowired` se inyectan instancias de los _Proxies_ que implementan los _Repositorios/DAOs_ definidos anteriormente.
- Se incluyen dos funciones con ejemplos de uso de esos _Repositorios_, para crear entidades (con `save()`) y consultarlas con métodos `findXXXX()`.

### Ejecutar el 'main()' creado

En Spring Tool Suite: Proyecto 'ejemplo-spring' `[botón derecho] > Run As > Spring Boot App`

Desde línea de comandos:
```sh
mvn spring-boot:run
```

ó

```sh
mvn install
java -jar target/ejemplo-spring-0.1-SNAPSHOT.jar
```

**Nota**: 

- Los proyectos creados con _Spring Initializr_ incluyen su propia instalación de Maven (_wrapped_ Maven), invocada por los comandos `mvnw` en GNU/Linux y `mvnw.cmd` en MS Windows. 

- Así, es posible compilar y ejecutar los proyectos Spring Boot sin tener que instalar Maven o depender de la versión concreta instalada en el sistema.

```sh
./mvnw spring-boot:run        # en GNU/linux
mvnw.cmd spring-boot:run    # en MS Windows
```

## EXTRA: Pruebas adicionales y adelanto de REST en Spring

### 1. **Query methods** de JPA

1. Abre las clases `ArticuloDAO` y `FamiliaDAO` y convierte los métodos `findByPatronDescripcion` en **query methods** de JPA.
2. Abre la clase `ClienteDAO` y añade un **query method** para listar los clientes que pertenencen a un código postal dado.
3. Abre la clase `ArticuloDAO` y añade un **query method** para listar los artículos cuyo precio esté comprendido entre dos valores dados.

### 2. Spring Data REST (generador de APIs REST para _repositorios_ Spring DATA)

1. Añadir/descomentar la siguiente dependencia en el `pom.xml` (activa el _starter_ de Spring Data REST):
```xml
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-rest</artifactId>
   </dependency>
```
   - Detalles en https://spring.io/projects/spring-data-rest

2. Ejecutar de nuevo el proyecto (con `mvn spring-boot:run`) y acceder con un navegador a las siguientes URLs:
   - http://localhost:8080
   - http://localhost:8080/clientes
   - http://localhost:8080/articuloes
   - http://localhost:8080/pedidoes
   - etc

3. Con el proyecto arrancado, realiza pruebas enviando peticiones HTTP con `curl` o con extensiones que faciliten este trabajo. Más detalles [aquí](doc/peticiones_REST.md).

### 3. SpringDoc OpenAPI (generador de documentación online OpenAPI/Swagger) [no es parte de Spring]

1. Añadir/descomentar las siguientes dependencias en el `pom.xml` (activa el proyecto `springdoc-openapi`):
```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-api</artifactId>
      <version>2.6.0</version>
   </dependency>

   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>2.6.0</version>
   </dependency>
```
   - Detalles en https://springdoc.org/

2. Ejecutar de nuevo el proyecto (con `mvn spring-boot:run`) y acceder con un navegador a la URL http://localhost:8080/swagger-ui.html
