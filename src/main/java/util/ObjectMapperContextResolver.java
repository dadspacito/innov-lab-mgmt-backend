package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * Provides a custom-configured Jackson {@link ObjectMapper} for JAX-RS.
 *
 * This resolver registers the {@link JavaTimeModule} to handle Java 8 date and time types
 * and configures the mapper to disable writing dates as timestamps.
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper mapper;
    /**
     * Constructs a new {@link ObjectMapperContextResolver} and configures the {@link ObjectMapper}.
     *
     * The configuration includes:
     * <ul>
     *     <li>Registering the {@link JavaTimeModule} to handle Java 8 date and time types.</li>
     *     <li>Disabling the feature that writes dates as timestamps.</li>
     * </ul>
     */
    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    /**
     * Returns the configured {@link ObjectMapper} instance.
     *
     * @param type the class type
     * @return the configured {@link ObjectMapper}
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}