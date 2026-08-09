package ua.vg.msg.userservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * CommonProperties — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "application.common")
public class CommonProperties {
    long refreshTokenTtlDays;
    long appAccessTokenTtlMinutes;
}
