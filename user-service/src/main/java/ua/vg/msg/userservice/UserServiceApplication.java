package ua.vg.msg.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * UserServiceApplication — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@SpringBootApplication
@ComponentScan(basePackages = {"ua.vg.msg.userservice", "ua.vg.msg.shared"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
