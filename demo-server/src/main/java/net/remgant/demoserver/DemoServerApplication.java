package net.remgant.demoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
public class DemoServerApplication {

    AtomicInteger counter = new AtomicInteger(1);

    public static void main(String[] args) {
        SpringApplication.run(DemoServerApplication.class, args);
    }

    @RequestMapping("/user")
    public Map<String, Object> user(Principal principal) {
        return Map.of("id", counter.getAndIncrement(), "text", String.format("Hello User %s!", principal.getName()));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public Map<String, Object> admin(Principal principal) {
        return Map.of("id", counter.getAndIncrement(), "text", String.format("Hello Administrator %s!", principal.getName()));
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
