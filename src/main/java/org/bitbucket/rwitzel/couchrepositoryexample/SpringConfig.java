package org.bitbucket.rwitzel.couchrepositoryexample;

import java.util.Map;
import java.util.Properties;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.spring.HttpClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.github.rwitzel.couchrepository.api.CouchDbCrudRepository;
import com.github.rwitzel.couchrepository.ektorp.EktorpCrudRepository;
import com.github.rwitzel.couchrepository.support.DocumentLoader;

/**
 * The Spring configuration.
 */
@Configuration
public class SpringConfig {

    /**
     * Configuration of the Ektorp driver.
     */
    @Bean
    public StdCouchDbConnector connector() throws Exception {

        String url = "http://localhost:5984/";
        String databaseName = "blogpost-example";

        Properties properties = new Properties();
        properties.setProperty("autoUpdateViewOnChange", "true");

        HttpClientFactoryBean factory = new HttpClientFactoryBean();
        factory.setUrl(url);
        factory.setProperties(properties);
        factory.afterPropertiesSet();
        HttpClient client = factory.getObject();

        CouchDbInstance dbInstance = new StdCouchDbInstance(client);
        return new StdCouchDbConnector(databaseName, dbInstance);
    }

    /**
     * @param db the Ektorp driver
     * @return Returns a Spring Data repository for {@link BlogPost} entities.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Bean
    public CouchDbCrudRepository<BlogPost, String> blogPostRepo(CouchDbConnector db) {
        return new EktorpCrudRepository(BlogPost.class, db);
    }

    /**
     * Creates a database and updates the design document <code>_design/Example</code>.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Bean
    @Lazy(false)
    public String createDatabaseAndUpdateDesignDocuments(CouchDbConnector db) {

        db.createDatabaseIfNotExists();

        DocumentLoader loader = new DocumentLoader(new EktorpCrudRepository(Map.class, db));

        loader.loadYaml(getClass().getResourceAsStream("BlogPost.yaml"));

        return "anything"; // return anything
    }

}