package com.curso.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.curso.springboot.webflux.app.models.dao.ProductoDao;
import com.curso.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {
	
	@Autowired
	private ProductoDao dao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		mongoTemplate.dropCollection("productos").subscribe();
		
		Flux.just(
			new Producto("Dell Monitor 25''", 5300.00),
			new Producto("NPET Teclado Mecanico", 2100.00),
			new Producto("EAGLE WARRIOR MOUSE Gaming", 530.00),
			new Producto("Samung Monitor 32''", 6200.00),
			new Producto("Redmi AirDot3 Pro", 1650.00),
			new Producto("Bose QuietComform A13", 8200.00),
			new Producto("Redmi Note12 128GB", 12250.00)
		)
		.flatMap(producto -> {
			producto.setCreatedAt(new Date());
			return dao.save(producto);
		})
		.subscribe(producto -> log.info("Inserted: " + producto.getId() + " " + producto.getNombre()));
		
	}

}
