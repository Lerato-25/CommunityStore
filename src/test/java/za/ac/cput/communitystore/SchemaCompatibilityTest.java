package za.ac.cput.communitystore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:schema_compatibility;MODE=MySQL;DB_CLOSE_DELAY=-1;NON_KEYWORDS=CONDITION",
    "spring.jpa.hibernate.ddl-auto=validate",
    "spring.sql.init.mode=always",
    "spring.sql.init.schema-locations=classpath:schema-compatibility.sql"
})
@ActiveProfiles("test")
class SchemaCompatibilityTest {
    @Test void allEntitiesValidateAgainstSuppliedTableDefinitions() { }
}
