package maventest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
<<<<<<< HEAD
@MapperScan({"maventest.**.mapper"})
=======
@MapperScan({"maventest.auth.mapper", "maventest.claim.mapper", "maventest.customer.mapper", "maventest.visit.mapper", "maventest.policy.mapper", "maventest.dashboard.mapper", "maventest.notification.mapper", "maventest.policyapplication.infrastructure.repository.mapper", "maventest.product.mapper"})
@EnableScheduling
>>>>>>> develop
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
