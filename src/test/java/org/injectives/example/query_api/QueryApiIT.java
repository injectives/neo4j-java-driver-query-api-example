package org.injectives.example.query_api;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class QueryApiIT {
    @Container
    private static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(DockerImageName.parse("neo4j:2025-enterprise"))
            .withEnv("NEO4J_ACCEPT_LICENSE_AGREEMENT", Optional.ofNullable(System.getenv("NEO4J_ACCEPT_LICENSE_AGREEMENT")).orElse("no"));

    @Test
    void shouldExecuteQuerries() throws URISyntaxException {
        // Home datbase resoultion is currently not supported in Query API, meaning that it is mandatory to set explicit
        // database name on every driver session and executable query. To ease transitioning process, the driver
        // supports on optional and experimental `defaultDatabase` parameter with a default database name to be used
        // when no explicit database name is set.
        var httpUri = new URIBuilder(neo4jContainer.getHttpUrl())
                .addParameter("defaultDatabase", "neo4j")
                .build();
        var authToken = AuthTokens.basic("neo4j", neo4jContainer.getAdminPassword());
        try (var driver = GraphDatabase.driver(httpUri, authToken)) {
            // create Movie node
            var movie = new Movie("title", "tagline", 2025);
            driver.executableQuery("CREATE (:Movie $movie)")
                    .withParameters(Map.of("movie", movie))
                    .execute();

            // read Movie node
            var foundMovie = driver.executableQuery("""
                            MATCH (movie:Movie {title: $title})
                            RETURN movie
                            """)
                    .withParameters(Map.of("title", movie.title))
                    .execute()
                    .records()
                    .stream()
                    .findFirst()
                    .map(v -> v.get("movie").as(Movie.class))
                    .orElse(null);

            assertEquals(movie, foundMovie);
        }
    }

    public record Movie(String title, String tagline, long released) {
    }
}
