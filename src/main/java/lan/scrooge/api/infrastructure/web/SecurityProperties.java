package lan.scrooge.api.infrastructure.web;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties(prefix = "scrooge.infrastructure.web")
@Getter
@Setter
public class SecurityProperties {

    List<String> publicUrls = new ArrayList<>();
    List<String> trustedIssuers = new ArrayList<>();

}
