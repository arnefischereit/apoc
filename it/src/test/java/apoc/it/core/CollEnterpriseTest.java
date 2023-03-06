package apoc.it.core;
import apoc.util.Neo4jContainerExtension;
import apoc.util.TestContainerUtil.ApocPackage;
import apoc.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import java.util.List;
import org.neo4j.driver.Session;

import static apoc.util.TestContainerUtil.createEnterpriseDB;
import static org.junit.Assert.assertEquals;

public class CollEnterpriseTest {

    private static Neo4jContainerExtension neo4jContainer;
    private static Session session;

    @BeforeAll
    public static void beforeAll() {
        // We build the project, the artifact will be placed into ./build/libs
        neo4jContainer = createEnterpriseDB(List.of(ApocPackage.CORE), !TestUtil.isRunningInCI());
        neo4jContainer.start();
        session = neo4jContainer.getSession();
    }

    @AfterAll
    public static void afterAll() {
        session.close();
        neo4jContainer.close();
    }

    @RepeatedTest(50)
    public void testMin() {
        assertEquals(1L, session.run("RETURN apoc.coll.min([1,2]) as value").next().get("value").asLong());
        assertEquals(1L, session.run("RETURN apoc.coll.min([1,2,3]) as value").next().get("value").asLong());
        assertEquals(0.5D, session.run("RETURN apoc.coll.min([0.5,1,2.3]) as value").next().get("value").asDouble(), 0.1);
    }

    @RepeatedTest(50)
    public void testMax() {
        assertEquals(3L, session.run("RETURN apoc.coll.max([1,2,3]) as value").next().get("value").asLong());
        assertEquals(2.3D, session.run("RETURN apoc.coll.max([0.5,1,2.3]) as value").next().get("value").asDouble(), 0.1);
    }

}