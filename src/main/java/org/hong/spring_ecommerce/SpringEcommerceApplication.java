package org.hong.spring_ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//(exclude =DataSourceAutoConfiguration.class) Hace que inicie la aplicación sin error de Datasource
//al no estar configurado el datasource en el aplication.properties
@SpringBootApplication (exclude =DataSourceAutoConfiguration.class)
public class SpringEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEcommerceApplication.class, args);
	}

}
