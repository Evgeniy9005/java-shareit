package ru.practicum.shareit.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.practicum")
@PropertySource(value = "classpath:application.properties")
public class PersistenceConfig {
    private final Environment environment;

    public PersistenceConfig(Environment environment) {
        this.environment = environment;

    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        /*properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql", "false"));
        properties.put("javax.persistence.schema-generation.database.action",
                environment.getProperty("javax.persistence.schema-generation.database.action", "none"));
        properties.put("javax.persistence.schema-generation.create-script-source",
                environment.getProperty("javax.persistence.schema-generation.create-script-source"));*/

        //настройка диалекта на БД
        properties.put("spring.jpa.properties.hibernate.dialect",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));

        //вывод sql запросов гибернации на консоль
        properties.put("spring.jpa.properties.hibernate.format_sql",
                environment.getProperty("spring.jpa.properties.hibernate.format_sql", "false"));

        //отключение инициализации БД через гибернацию
        properties.put("spring.jpa.hibernate.ddl-auto",
                environment.getProperty("spring.jpa.hibernate.ddl-auto", "none"));

        //чтобы всегда инициализировать БД на основе сценариев
        properties.put("spring.sql.init.mode",
                environment.getProperty("spring.sql.init.mode","never"));

       /* //
        properties.put("spring.sql.init.schema-locations",
                environment.getProperty("spring.sql.init.schema-locations"));

        //
        properties.put("spring.sql.init.data-locations",
                environment.getProperty("spring.sql.init.data-locations"));*/

        //
        properties.put("logging.level.org.springframework.orm.jpa",
                environment.getProperty("logging.level.org.springframework.orm.jpa","INFO"));

        //
        properties.put("logging.level.org.springframework.transaction",
                environment.getProperty("logging.level.org.springframework.transaction","INFO"));

        //
        properties.put("logging.level.org.springframework.transaction.interceptor",
                environment.getProperty("logging.level.org.springframework.transaction.interceptor","INFO"));

        //
        properties.put("logging.level.org.springframework.orm.jpa.JpaTransactionManager",
                environment.getProperty("logging.level.org.springframework.orm.jpa.JpaTransactionManager"));

        //
        properties.put("spring.jpa.show-sql",
                environment.getProperty("spring.jpa.show-sql"));

        //
        properties.put("spring.jackson.serialization.FAIL_ON_EMPTY_BEANS",
                environment.getProperty("spring.jackson.serialization.FAIL_ON_EMPTY_BEANS"));

        return properties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //драйвер
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"));
        //путь к БД
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        //имя пользователя
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        //пароль
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("ru.practicum");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(hibernateProperties());

        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
