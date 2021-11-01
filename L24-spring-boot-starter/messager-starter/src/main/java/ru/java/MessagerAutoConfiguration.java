package ru.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.java.Messager;
import ru.java.MessagerConfig;


@Configuration
@ConditionalOnClass(Messager.class) // будет срабатывать, когда в classpath'е есть класс  Messager
@EnableConfigurationProperties(Props.class)
public class MessagerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MessagerAutoConfiguration.class);

    private final Props props;

    public MessagerAutoConfiguration(Props props) {
        this.props = props;
    }

    @Bean
    // @ConditionalOnMissingBean  - если нет бина MessagerConfig, он будет создан.
    // Эту аннотацию можно и не использовать, но тогда у пользователя стартера не будет возможности исключить
    // использование этого бина (например, через exclude), что не очень корректно по отношению к пользователю,
    // поскольку мы лишим его возможности использовать его собственный бин MessagerConfig
    @ConditionalOnMissingBean
    public MessagerConfig messagerConfig() {
        var message = props.getMessage() == null ? "default message" : props.getMessage();
        logger.info("AutoConfig. creating MessagerConfig, default message:{}", message);
        return new MessagerConfig(message);
    }

    @Bean
    @ConditionalOnMissingBean
    public Messager messager(MessagerConfig messagerConfig) {
        logger.info("AutoConfig. creating Messager");
        return new Messager(messagerConfig);
    }
}
