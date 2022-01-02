package ru.java.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.app.messagesystem.MessageSystem;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Configuration
public class ContextDestructionConfig {
    private static final Logger log = LoggerFactory.getLogger(ContextDestructionConfig.class);
    private final MessageSystem messageSystem;

    public ContextDestructionConfig(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    @Bean
    ServletListenerRegistrationBean<ServletContextListener> servletListener() {
        ServletListenerRegistrationBean<ServletContextListener> listenerRegistrationBean = new ServletListenerRegistrationBean<>();
        listenerRegistrationBean.setListener(
                new ServletContextListener() {
                    @Override
                    public void contextDestroyed(ServletContextEvent event) {
                        try {
                            messageSystem.dispose();
                            Thread.sleep(60 * 1000);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        return listenerRegistrationBean;
    }
}


