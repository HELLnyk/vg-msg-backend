package ua.vg.msg.messageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import ua.vg.msg.shared.properties.CommonProperties;

/**
 * MessageServiceApplication — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@SpringBootApplication
@ComponentScan(basePackages = {"ua.vg.msg.messageservice", "ua.vg.msg.shared"})
@EnableConfigurationProperties({CommonProperties.class})
public class MessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }
}
