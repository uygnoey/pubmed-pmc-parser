package io.brillianttiger.bio.parser.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Identifier 클래스 테스트
 */
class IdentifierTest {

    @Test
    void testParseFromString() {
        // DOI 테스트
        Identifier doi = Identifier.parseFromString("doi", "10.1234/abcd.5678");
        assertEquals(Identifier.IdType.DOI, doi.getType());
        assertEquals("10.1234/abcd.5678", doi.getValue());
        assertTrue(doi.isValidated());

        // PMID 테스트
        Identifier pmid = Identifier.parseFromString("pmid", "12345678");
        assertEquals(Identifier.IdType.PMID, pmid.getType());
        assertTrue(pmid.isValidated());

        // ORCID 테스트
        Identifier orcid = Identifier.parseFromString("orcid", "0000-0001-2345-6789");
        assertEquals(Identifier.IdType.ORCID, orcid.getType());
        assertTrue(orcid.isValidated());
    }

    @Test
    void testIsValidDoi() {
        assertTrue(Identifier.isValidDoi("10.1234/abcd"));
        assertTrue(Identifier.isValidDoi("10.1000/xyz123"));
        assertFalse(Identifier.isValidDoi("invalid"));
        assertFalse(Identifier.isValidDoi("9.1234/abcd"));
        assertFalse(Identifier.isValidDoi(null));
    }

    @Test
    void testIsValidPmid() {
        assertTrue(Identifier.isValidPmid("12345678"));
        assertTrue(Identifier.isValidPmid("1"));
        assertFalse(Identifier.isValidPmid("abc123"));
        assertFalse(Identifier.isValidPmid("12345-678"));
        assertFalse(Identifier.isValidPmid(null));
    }

    @Test
    void testIsValidPmcid() {
        assertTrue(Identifier.isValidPmcid("PMC1234567"));
        assertTrue(Identifier.isValidPmcid("PMC1"));
        assertFalse(Identifier.isValidPmcid("pmc1234567")); // 대소문자 구분
        assertFalse(Identifier.isValidPmcid("1234567"));
        assertFalse(Identifier.isValidPmcid(null));
    }

    @Test
    void testIsValidOrcid() {
        assertTrue(Identifier.isValidOrcid("0000-0001-2345-6789"));
        assertTrue(Identifier.isValidOrcid("0000-0002-1825-000X"));
        assertTrue(Identifier.isValidOrcid("https://orcid.org/0000-0001-2345-6789"));
        assertFalse(Identifier.isValidOrcid("0000-0001-2345-67890")); // 너무 긺
        assertFalse(Identifier.isValidOrcid("0000-0001-2345-678")); // 너무 짧음
        assertFalse(Identifier.isValidOrcid(null));
    }

    @Test
    void testIsValidIsni() {
        assertTrue(Identifier.isValidIsni("0000000121032683"));
        assertTrue(Identifier.isValidIsni("0000 0001 2103 2683"));
        assertTrue(Identifier.isValidIsni("000000012103268X"));
        assertFalse(Identifier.isValidIsni("000000012103268")); // 너무 짧음
        assertFalse(Identifier.isValidIsni(null));
    }

    @Test
    void testIsValidRor() {
        assertTrue(Identifier.isValidRor("https://ror.org/0abcdef12"));
        assertTrue(Identifier.isValidRor("https://ror.org/02mhbdp94"));
        assertFalse(Identifier.isValidRor("https://ror.org/abcdef12")); // 0으로 시작 안함
        assertFalse(Identifier.isValidRor("ror.org/0abcdef12")); // https 없음
        assertFalse(Identifier.isValidRor(null));
    }

    @Test
    void testIsValidIsbn() {
        assertTrue(Identifier.isValidIsbn("1234567890"));
        assertTrue(Identifier.isValidIsbn("123456789X"));
        assertTrue(Identifier.isValidIsbn("978-1234567890"));
        assertTrue(Identifier.isValidIsbn("1234567890123"));
        assertFalse(Identifier.isValidIsbn("12345")); // 너무 짧음
        assertFalse(Identifier.isValidIsbn(null));
    }

    @Test
    void testIsValidIssn() {
        assertTrue(Identifier.isValidIssn("1234-5678"));
        assertTrue(Identifier.isValidIssn("1234-567X"));
        assertTrue(Identifier.isValidIssn("12345678"));
        assertFalse(Identifier.isValidIssn("1234-56789")); // 너무 긺
        assertFalse(Identifier.isValidIssn(null));
    }

    @Test
    void testValidateMethod() {
        assertTrue(Identifier.validate(Identifier.IdType.DOI, "10.1234/abcd"));
        assertTrue(Identifier.validate(Identifier.IdType.PMID, "12345678"));
        assertFalse(Identifier.validate(Identifier.IdType.DOI, "invalid"));
        assertFalse(Identifier.validate(Identifier.IdType.PMID, "abc123"));
    }

    @Test
    void testIsValidInstanceMethod() {
        Identifier validDoi = Identifier.builder()
                .type(Identifier.IdType.DOI)
                .value("10.1234/abcd")
                .build();
        assertTrue(validDoi.isValid());

        Identifier invalidDoi = Identifier.builder()
                .type(Identifier.IdType.DOI)
                .value("invalid")
                .build();
        assertFalse(invalidDoi.isValid());
    }

    @Test
    void testParseIdType() {
        assertEquals(Identifier.IdType.DOI, Identifier.parseIdType("doi"));
        assertEquals(Identifier.IdType.PMID, Identifier.parseIdType("pmid"));
        assertEquals(Identifier.IdType.PMID, Identifier.parseIdType("pubmed"));
        assertEquals(Identifier.IdType.ORCID, Identifier.parseIdType("orcid"));
        assertEquals(Identifier.IdType.ISBN, Identifier.parseIdType("isbn"));
        assertEquals(Identifier.IdType.ISSN, Identifier.parseIdType("issn"));
        assertEquals(Identifier.IdType.REGISTRY_NUMBER, Identifier.parseIdType("registry-number"));
        assertEquals(Identifier.IdType.REGISTRY_NUMBER, Identifier.parseIdType("cas"));
        assertEquals(Identifier.IdType.OTHER, Identifier.parseIdType("unknown"));
        assertEquals(Identifier.IdType.OTHER, Identifier.parseIdType(null));
    }

    @Test
    void testParseIdTypeAllCases() {
        // Test all missing IdType cases
        assertEquals(Identifier.IdType.PMCID, Identifier.parseIdType("pmcid"));
        assertEquals(Identifier.IdType.PMCID, Identifier.parseIdType("pmc"));
        assertEquals(Identifier.IdType.PMC_UID, Identifier.parseIdType("pmc-uid"));
        assertEquals(Identifier.IdType.PII, Identifier.parseIdType("pii"));
        assertEquals(Identifier.IdType.PUBLISHER_ID, Identifier.parseIdType("publisher-id"));
        assertEquals(Identifier.IdType.MANUSCRIPT, Identifier.parseIdType("manuscript"));
        assertEquals(Identifier.IdType.MEDLINE, Identifier.parseIdType("medline"));
        assertEquals(Identifier.IdType.ISNI, Identifier.parseIdType("isni"));
        assertEquals(Identifier.IdType.SCOPUS, Identifier.parseIdType("scopus"));
        assertEquals(Identifier.IdType.RESEARCHER_ID, Identifier.parseIdType("researcher-id"));
        assertEquals(Identifier.IdType.ROR, Identifier.parseIdType("ror"));
        assertEquals(Identifier.IdType.RINGGOLD, Identifier.parseIdType("ringgold"));
        assertEquals(Identifier.IdType.GRID, Identifier.parseIdType("grid"));
    }

    @Test
    void testValidateAllTypes() {
        // Test validate() for all validation types
        assertTrue(Identifier.validate(Identifier.IdType.PMCID, "PMC1234567"));
        assertFalse(Identifier.validate(Identifier.IdType.PMCID, "invalid"));

        assertTrue(Identifier.validate(Identifier.IdType.ISNI, "0000000121032683"));
        assertFalse(Identifier.validate(Identifier.IdType.ISNI, "invalid"));

        assertTrue(Identifier.validate(Identifier.IdType.ROR, "https://ror.org/0abcdef12"));
        assertFalse(Identifier.validate(Identifier.IdType.ROR, "invalid"));

        assertTrue(Identifier.validate(Identifier.IdType.ISBN, "1234567890"));
        assertFalse(Identifier.validate(Identifier.IdType.ISBN, "invalid"));

        assertTrue(Identifier.validate(Identifier.IdType.ISSN, "1234-5678"));
        assertFalse(Identifier.validate(Identifier.IdType.ISSN, "invalid"));

        assertTrue(Identifier.validate(Identifier.IdType.ORCID, "0000-0001-2345-6789"));
        assertFalse(Identifier.validate(Identifier.IdType.ORCID, "invalid"));
    }

    @Test
    void testValidateNullAndBlank() {
        // Test validate() with null and blank values (covers isBlank() branches)
        assertFalse(Identifier.validate(Identifier.IdType.DOI, null));
        assertFalse(Identifier.validate(Identifier.IdType.DOI, ""));
        assertFalse(Identifier.validate(Identifier.IdType.DOI, "   "));

        assertFalse(Identifier.validate(Identifier.IdType.PMID, null));
        assertFalse(Identifier.validate(Identifier.IdType.PMID, ""));
        assertFalse(Identifier.validate(Identifier.IdType.PMID, "   "));

        // Direct method calls to cover individual isBlank() branches
        assertFalse(Identifier.isValidDoi("   "));
        assertFalse(Identifier.isValidPmid("   "));
        assertFalse(Identifier.isValidPmcid("   "));
        assertFalse(Identifier.isValidOrcid("   "));
        assertFalse(Identifier.isValidIsni("   "));
        assertFalse(Identifier.isValidRor("   "));
        assertFalse(Identifier.isValidIsbn("   "));
        assertFalse(Identifier.isValidIssn("   "));
    }

    @Test
    void testValidateDefaultCase() {
        // Test validate() default case - other types are always valid if value exists
        assertTrue(Identifier.validate(Identifier.IdType.OTHER, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.PMC_UID, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.PII, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.PUBLISHER_ID, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.MANUSCRIPT, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.MEDLINE, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.SCOPUS, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.RESEARCHER_ID, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.RINGGOLD, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.GRID, "any-value"));
        assertTrue(Identifier.validate(Identifier.IdType.REGISTRY_NUMBER, "any-value"));
    }
}
