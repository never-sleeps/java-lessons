package ru.java.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.java.core.repository.DataTemplateHibernate;
import ru.java.core.repository.HibernateUtils;
import ru.java.core.sessionmanager.TransactionManagerHibernate;
import ru.java.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.java.crm.model.AddressEntity;
import ru.java.crm.model.ClientEntity;
import ru.java.crm.model.PhoneEntity;
import ru.java.crm.service.DbServiceClientImpl;

import java.util.Arrays;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        executeMigrations(configuration);

        var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                ClientEntity.class,
                AddressEntity.class,
                PhoneEntity.class
        );

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(ClientEntity.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        AddressEntity address = new AddressEntity("077", "Москва", "Цветной бульвар", "35/1");
        PhoneEntity phone1 = new PhoneEntity("123456789");
        PhoneEntity phone2 = new PhoneEntity("987654321");
        var clientFirst = dbServiceClient.saveClient(new ClientEntity(
                "dbServiceFirst",
                address,
                Arrays.asList(phone1, phone2))
        );
        /*
            insert into address (city, house, region_code, street, id) values (?, ?, ?, ?, ?)
            insert into client (address_id, name, id) values (?, ?, ?)
            insert into phone (number, id) values (?, ?)
            insert into phone (number, id) values (?, ?)
         */


        var clientSecond = dbServiceClient.saveClient(new ClientEntity("dbServiceSecond"));
        /*
            insert into client (address_id, name, id) values (?, ?, ?)
         */

        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected: {}", clientSecondSelected);
        /*
            select
                client1x0_.id as id1_1_0_,
                client1x0_.address_id as address_3_1_0_,
                client1x0_.name as name2_1_0_,
                address1_.id as id1_0_1_,
                address1_.city as city2_0_1_,
                address1_.house as house3_0_1_,
                address1_.region_code as region_c4_0_1_,
                address1_.street as street5_0_1_,
                phones2_.client_id as client_i3_2_2_,
                phones2_.id as id1_2_2_,
                phones2_.id as id1_2_3_,
                phones2_.client_id as client_i3_2_3_,
                phones2_.number as number2_2_3_
            from
                client client1x0_
            left outer join
                address address1_
                    on client1x0_.address_id=address1_.id
            left outer join
                phone phones2_
                    on client1x0_.id=phones2_.client_id
            where
                client1x0_.id=?

             clientSecondSelected: Client{id=2, name='dbServiceSecond', address=null, phones=[]}
         */
///
        dbServiceClient.saveClient(new ClientEntity(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        /*
            update client set address_id=?, name=? where id=?
         */

        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated: {}", clientUpdated);
        /*
            select
                client1x0_.id as id1_1_0_,
                client1x0_.address_id as address_3_1_0_,
                client1x0_.name as name2_1_0_,
                address1_.id as id1_0_1_,
                address1_.city as city2_0_1_,
                address1_.house as house3_0_1_,
                address1_.region_code as region_c4_0_1_,
                address1_.street as street5_0_1_,
                phones2_.client_id as client_i3_2_2_,
                phones2_.id as id1_2_2_,
                phones2_.id as id1_2_3_,
                phones2_.client_id as client_i3_2_3_,
                phones2_.number as number2_2_3_
            from
                client client1x0_
            left outer join
                address address1_
                    on client1x0_.address_id=address1_.id
            left outer join
                phone phones2_
                    on client1x0_.id=phones2_.client_id
            where
                client1x0_.id=?

            clientUpdated: Client{id=2, name='dbServiceSecondUpdated', address=null, phones=[]}
         */


        log.info("All clients");
        var allClients = dbServiceClient.findAll();
        allClients.forEach(client -> log.info("client:{}", client));
        /*
            All clients

            select
                client1x0_.id as id1_1_,
                client1x0_.address_id as address_3_1_,
                client1x0_.name as name2_1_
            from
                client client1x0_

            select
                address0_.id as id1_0_0_,
                address0_.city as city2_0_0_,
                address0_.house as house3_0_0_,
                address0_.region_code as region_c4_0_0_,
                address0_.street as street5_0_0_
            from
                address address0_
            where
                address0_.id=?

            select
                phones0_.client_id as client_i3_2_0_,
                phones0_.id as id1_2_0_,
                phones0_.id as id1_2_1_,
                phones0_.client_id as client_i3_2_1_,
                phones0_.number as number2_2_1_
            from
                phone phones0_
            where
                phones0_.client_id=?

            select
                phones0_.client_id as client_i3_2_0_,
                phones0_.id as id1_2_0_,
                phones0_.id as id1_2_1_,
                phones0_.client_id as client_i3_2_1_,
                phones0_.number as number2_2_1_
            from
                phone phones0_
            where
                phones0_.client_id=?

            client:Client{id=1, name='dbServiceFirst', address=Address{id=1, regionCode='077', city='Москва', street='Цветной бульвар', house='35/1'}, phones=[Phone{id=1, number='123456789'}, Phone{id=2, number='987654321'}]}
            client:Client{id=2, name='dbServiceSecondUpdated', address=null, phones=[]}
         */
    }

    private static void executeMigrations(Configuration configuration) {
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }
}
