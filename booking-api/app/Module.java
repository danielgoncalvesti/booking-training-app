import com.google.inject.AbstractModule;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.typesafe.config.ConfigFactory;
/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    @Override
    public void configure() {
        String cassandraHosts = ConfigFactory.load().getString("cassandra.hosts");
        String[] hosts = cassandraHosts.split(",");
        Cluster.Builder builder = Cluster.builder();
        for (String host : hosts) {
            builder = builder.addContactPoint(host);
        }
        Cluster cluster = builder.build();
        Session session = cluster.connect("Booking");
        bind(Session.class).toInstance(session);
    }

}
