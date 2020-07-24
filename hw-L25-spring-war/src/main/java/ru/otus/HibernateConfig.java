package ru.otus;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.hibernate.HibernateUtils;

@Configuration
public class HibernateConfig {
    @Bean
    public SessionFactory hibernateSessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Phone.class, Address.class);
    }
}
