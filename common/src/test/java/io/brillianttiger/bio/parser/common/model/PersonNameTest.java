package io.brillianttiger.bio.parser.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PersonNameTest / PersonName 테스트
 *
 * KR: PersonName 클래스와 내부 클래스들의 테스트.
 *     NameStyle enum, getFullName() 메서드, 모든 필드 커버.
 * EN: Test for PersonName class and inner classes.
 *     Covers NameStyle enum, getFullName() method, all fields.
 */
class PersonNameTest {

    @Test
    @DisplayName("NameStyle enum - all values")
    public void test01_nameStyleEnumValues() {
        // Test NameStyle enum values
        PersonName.NameStyle[] values = PersonName.NameStyle.values();
        assertEquals(4, values.length);

        assertEquals(PersonName.NameStyle.WESTERN, PersonName.NameStyle.valueOf("WESTERN"));
        assertEquals(PersonName.NameStyle.EASTERN, PersonName.NameStyle.valueOf("EASTERN"));
        assertEquals(PersonName.NameStyle.ISLENSK, PersonName.NameStyle.valueOf("ISLENSK"));
        assertEquals(PersonName.NameStyle.GIVEN_ONLY, PersonName.NameStyle.valueOf("GIVEN_ONLY"));
    }

    @Test
    @DisplayName("getFullName - collectiveName only")
    public void test02_getFullNameCollective() {
        // Test collective name (단체명) - should return immediately
        PersonName person = PersonName.builder()
                .collectiveName("World Health Organization")
                .lastName("Smith")  // Should be ignored
                .foreName("John")   // Should be ignored
                .build();

        assertEquals("World Health Organization", person.getFullName());
    }

    @Test
    @DisplayName("getFullName - all name components")
    public void test03_getFullNameAllComponents() {
        // Test all name components: prefix + foreName + lastName + suffix
        PersonName person = PersonName.builder()
                .prefix("Dr.")
                .foreName("John")
                .lastName("Smith")
                .suffix("Jr.")
                .build();

        assertEquals("Dr. John Smith Jr.", person.getFullName());
    }

    @Test
    @DisplayName("getFullName - only lastName")
    public void test04_getFullNameLastNameOnly() {
        // Test lastName only
        PersonName person = PersonName.builder()
                .lastName("Einstein")
                .build();

        assertEquals("Einstein", person.getFullName());
    }

    @Test
    @DisplayName("getFullName - only foreName")
    public void test05_getFullNameForeNameOnly() {
        // Test foreName only (GIVEN_ONLY style)
        PersonName person = PersonName.builder()
                .foreName("Madonna")
                .build();

        assertEquals("Madonna", person.getFullName());
    }

    @Test
    @DisplayName("getFullName - foreName + lastName")
    public void test06_getFullNameForeAndLast() {
        // Test foreName + lastName (most common)
        PersonName person = PersonName.builder()
                .foreName("Albert")
                .lastName("Einstein")
                .build();

        assertEquals("Albert Einstein", person.getFullName());
    }

    @Test
    @DisplayName("getFullName - prefix + lastName")
    public void test07_getFullNamePrefixAndLast() {
        // Test prefix + lastName (no foreName)
        PersonName person = PersonName.builder()
                .prefix("Dr.")
                .lastName("Smith")
                .build();

        assertEquals("Dr. Smith", person.getFullName());
    }

    @Test
    @DisplayName("getFullName - lastName + suffix")
    public void test08_getFullNameLastAndSuffix() {
        // Test lastName + suffix (no foreName)
        PersonName person = PersonName.builder()
                .lastName("Kennedy")
                .suffix("Jr.")
                .build();

        assertEquals("Kennedy Jr.", person.getFullName());
    }

    @Test
    @DisplayName("PersonName - all boolean fields")
    public void test09_allBooleanFields() {
        // Test all boolean fields
        PersonName person = PersonName.builder()
                .lastName("Test")
                .valid(true)
                .equalContrib(true)
                .corresponding(true)
                .deceased(true)
                .build();

        assertTrue(person.isValid());
        assertTrue(person.isEqualContrib());
        assertTrue(person.isCorresponding());
        assertTrue(person.isDeceased());
    }

    @Test
    @DisplayName("PersonName - all string fields")
    public void test10_allStringFields() {
        // Test all string fields
        PersonName person = PersonName.builder()
                .lastName("Smith")
                .foreName("John")
                .initials("J.S.")
                .suffix("Jr.")
                .prefix("Dr.")
                .collectiveName("WHO")
                .contribType("author")
                .build();

        assertEquals("Smith", person.getLastName());
        assertEquals("John", person.getForeName());
        assertEquals("J.S.", person.getInitials());
        assertEquals("Jr.", person.getSuffix());
        assertEquals("Dr.", person.getPrefix());
        assertEquals("WHO", person.getCollectiveName());
        assertEquals("author", person.getContribType());
    }

    @Test
    @DisplayName("PersonName - roles list")
    public void test11_rolesField() {
        // Test roles list
        List<String> roles = Arrays.asList("editor", "reviewer");
        PersonName person = PersonName.builder()
                .lastName("Smith")
                .roles(roles)
                .build();

        assertEquals(2, person.getRoles().size());
        assertTrue(person.getRoles().contains("editor"));
        assertTrue(person.getRoles().contains("reviewer"));
    }

    @Test
    @DisplayName("PersonName - nameStyle field")
    public void test12_nameStyleField() {
        // Test nameStyle field
        PersonName person = PersonName.builder()
                .lastName("Tanaka")
                .foreName("Yuki")
                .nameStyle(PersonName.NameStyle.EASTERN)
                .build();

        assertEquals(PersonName.NameStyle.EASTERN, person.getNameStyle());
    }

    @Test
    @DisplayName("PersonIdentifier - all fields")
    public void test13_personIdentifierAllFields() {
        // Test PersonIdentifier inner class
        PersonName.PersonIdentifier identifier = PersonName.PersonIdentifier.builder()
                .source("ORCID")
                .value("0000-0001-2345-6789")
                .authenticated(true)
                .build();

        assertEquals("ORCID", identifier.getSource());
        assertEquals("0000-0001-2345-6789", identifier.getValue());
        assertTrue(identifier.getAuthenticated());
    }

    @Test
    @DisplayName("PersonName - identifiers list")
    public void test14_identifiersList() {
        // Test identifiers list
        PersonName.PersonIdentifier id1 = PersonName.PersonIdentifier.builder()
                .source("ORCID")
                .value("0000-0001-2345-6789")
                .build();

        PersonName.PersonIdentifier id2 = PersonName.PersonIdentifier.builder()
                .source("Scopus")
                .value("12345")
                .build();

        PersonName person = PersonName.builder()
                .lastName("Smith")
                .identifiers(Arrays.asList(id1, id2))
                .build();

        assertEquals(2, person.getIdentifiers().size());
        assertEquals("ORCID", person.getIdentifiers().get(0).getSource());
        assertEquals("Scopus", person.getIdentifiers().get(1).getSource());
    }

    @Test
    @DisplayName("Affiliation - all fields")
    public void test15_affiliationAllFields() {
        // Test Affiliation inner class
        PersonName.Affiliation affiliation = PersonName.Affiliation.builder()
                .id("aff1")
                .text("Department of Computer Science")
                .country("USA")
                .email("test@example.com")
                .build();

        assertEquals("aff1", affiliation.getId());
        assertEquals("Department of Computer Science", affiliation.getText());
        assertEquals("USA", affiliation.getCountry());
        assertEquals("test@example.com", affiliation.getEmail());
    }

    @Test
    @DisplayName("Institution - all fields")
    public void test16_institutionAllFields() {
        // Test Institution inner class
        PersonName.Institution institution = PersonName.Institution.builder()
                .name("Stanford University")
                .id("grid.168010.e")
                .idType("grid")
                .build();

        assertEquals("Stanford University", institution.getName());
        assertEquals("grid.168010.e", institution.getId());
        assertEquals("grid", institution.getIdType());
    }

    @Test
    @DisplayName("Affiliation - with institutions list")
    public void test17_affiliationWithInstitutions() {
        // Test Affiliation with institutions list
        PersonName.Institution inst = PersonName.Institution.builder()
                .name("MIT")
                .id("grid.116068.8")
                .idType("grid")
                .build();

        PersonName.Affiliation affiliation = PersonName.Affiliation.builder()
                .id("aff1")
                .text("Computer Science Dept")
                .institutions(Arrays.asList(inst))
                .build();

        assertEquals(1, affiliation.getInstitutions().size());
        assertEquals("MIT", affiliation.getInstitutions().get(0).getName());
    }

    @Test
    @DisplayName("PersonName - with affiliations list")
    public void test18_personNameWithAffiliations() {
        // Test PersonName with affiliations list
        PersonName.Institution inst = PersonName.Institution.builder()
                .name("Harvard")
                .build();

        PersonName.Affiliation aff = PersonName.Affiliation.builder()
                .id("aff1")
                .text("Medical School")
                .institutions(Arrays.asList(inst))
                .build();

        PersonName person = PersonName.builder()
                .lastName("Watson")
                .foreName("James")
                .affiliations(Arrays.asList(aff))
                .build();

        assertEquals(1, person.getAffiliations().size());
        assertEquals("aff1", person.getAffiliations().get(0).getId());
        assertEquals("Harvard", person.getAffiliations().get(0).getInstitutions().get(0).getName());
    }

    @Test
    @DisplayName("getFullName - empty string trim test")
    public void test19_getFullNameEmptyTrim() {
        // Test getFullName with all null fields - should return empty string
        PersonName person = PersonName.builder().build();

        assertEquals("", person.getFullName());
    }

    @Test
    @DisplayName("PersonName - constructor test")
    public void test20_constructorTest() {
        // Test no-args constructor
        PersonName person = new PersonName();
        person.setLastName("Test");

        assertEquals("Test", person.getLastName());
        assertEquals("Test", person.getFullName());
    }
}
