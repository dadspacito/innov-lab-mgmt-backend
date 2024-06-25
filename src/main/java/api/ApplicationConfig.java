package api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configures the base path for the REST API.
 */

@ApplicationPath("/api")
public class ApplicationConfig extends Application{

}
