

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class GrizzlyRunner {

	public static void main(String[] args) throws IOException {
		final String baseUri = "http://localhost:9998/";
		final ResourceConfig rc = new ResourceConfig().packages("org.bookit.server");
		rc.property("contextConfigLocation", "org.bookit");
		HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);

		System.out.println(String.format("Jersey app started with WADL available at "
				+ "%sapplication.wadl\nHit enter to stop it...", baseUri));
		System.in.read();
		httpServer.shutdown();
		System.exit(0);
	}
}
