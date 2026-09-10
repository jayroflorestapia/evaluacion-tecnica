
package mx.evaluacion.entrada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiEntradaApplication {
    public static void main(String[] args) { SpringApplication.run(ApiEntradaApplication.class, args); }
}
