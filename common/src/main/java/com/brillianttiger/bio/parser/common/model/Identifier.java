package com.brillianttiger.bio.parser.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Identifier / 공통 식별자 모델
 *
 * KR: 각종 식별자 공통 모델.
 *     DOI, PMID, PMCID, ORCID 등 모든 ID에 사용.
 * EN: Common identifier model.
 *     Used for all IDs including DOI, PMID, PMCID, ORCID, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Identifier {

    private IdType type;
    private String value;
    private String source;          // PubMed Source 속성
    private String assigningAuthority;  // JATS
    private String specificUse;     // JATS
    private boolean validated;      // 유효성 확인 여부

    /**
     * IdType / 식별자 타입
     */
    public enum IdType {
        // Article IDs
        DOI,
        PMID,
        PMCID,
        PMC_UID,
        PII,
        PUBLISHER_ID,
        MANUSCRIPT,
        MEDLINE,
        SICI,
        ARK,
        ISBN,
        ISSN,

        // Person IDs
        ORCID,
        ISNI,
        SCOPUS,
        RESEARCHER_ID,
        WOS_RESEARCHER_ID,

        // Institution IDs
        ROR,
        RINGGOLD,
        GRID,

        // Chemical/Substance IDs
        REGISTRY_NUMBER,

        // Other
        OTHER
    }

    /**
     * ID 타입 문자열로부터 변환 / Parse ID type from string
     *
     * @param typeStr type string
     * @return IdType enum value
     */
    public static IdType parseIdType(String typeStr) {
        if (typeStr == null) {
            return IdType.OTHER;
        }

        return switch (typeStr.toLowerCase()) {
            case "doi" -> IdType.DOI;
            case "pmid", "pubmed" -> IdType.PMID;
            case "pmcid", "pmc" -> IdType.PMCID;
            case "pmc-uid" -> IdType.PMC_UID;
            case "pii" -> IdType.PII;
            case "publisher-id" -> IdType.PUBLISHER_ID;
            case "manuscript" -> IdType.MANUSCRIPT;
            case "medline" -> IdType.MEDLINE;
            case "isbn" -> IdType.ISBN;
            case "issn" -> IdType.ISSN;
            case "orcid" -> IdType.ORCID;
            case "isni" -> IdType.ISNI;
            case "scopus" -> IdType.SCOPUS;
            case "researcher-id" -> IdType.RESEARCHER_ID;
            case "ror" -> IdType.ROR;
            case "ringgold" -> IdType.RINGGOLD;
            case "grid" -> IdType.GRID;
            case "registry-number", "cas" -> IdType.REGISTRY_NUMBER;
            default -> IdType.OTHER;
        };
    }

    /**
     * 타입과 값으로부터 Identifier 생성 (팩토리 메서드) / Create Identifier from type and value (factory method)
     *
     * KR: 타입 문자열과 값으로부터 Identifier 객체를 생성하고 검증
     * EN: Create and validate Identifier from type string and value
     *
     * @param typeStr ID 타입 문자열 / ID type string
     * @param value ID 값 / ID value
     * @return Identifier 객체 / Identifier object
     */
    public static Identifier parseFromString(String typeStr, String value) {
        IdType type = parseIdType(typeStr);
        boolean isValid = validate(type, value);

        return Identifier.builder()
                .type(type)
                .value(value)
                .validated(isValid)
                .build();
    }

    /**
     * ID 검증 (타입별) / Validate ID by type
     *
     * @param type ID 타입 / ID type
     * @param value ID 값 / ID value
     * @return 검증 결과 / validation result
     */
    public static boolean validate(IdType type, String value) {
        if (value == null || value.isBlank()) {
            return false;
        }

        return switch (type) {
            case DOI -> isValidDoi(value);
            case PMID -> isValidPmid(value);
            case PMCID -> isValidPmcid(value);
            case ORCID -> isValidOrcid(value);
            case ISNI -> isValidIsni(value);
            case ROR -> isValidRor(value);
            case ISBN -> isValidIsbn(value);
            case ISSN -> isValidIssn(value);
            default -> true; // 기타 타입은 값만 있으면 유효 / Other types are valid if value exists
        };
    }

    /**
     * 현재 인스턴스의 ID 검증 / Validate current instance ID
     *
     * @return 검증 결과 / validation result
     */
    public boolean isValid() {
        return validate(this.type, this.value);
    }

    /**
     * DOI 검증 / Validate DOI
     *
     * KR: DOI 형식 검증 (10.xxxx/yyyy 패턴)
     * EN: Validate DOI format (10.xxxx/yyyy pattern)
     *
     * @param doi DOI 값 / DOI value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidDoi(String doi) {
        if (doi == null || doi.isBlank()) {
            return false;
        }
        // DOI는 10.으로 시작하고 / 뒤에 값이 있어야 함
        return doi.matches("^10\\.\\d+/.*$");
    }

    /**
     * PMID 검증 / Validate PMID
     *
     * KR: PMID 형식 검증 (숫자만)
     * EN: Validate PMID format (numeric only)
     *
     * @param pmid PMID 값 / PMID value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidPmid(String pmid) {
        if (pmid == null || pmid.isBlank()) {
            return false;
        }
        return pmid.matches("^\\d+$");
    }

    /**
     * PMCID 검증 / Validate PMCID
     *
     * KR: PMCID 형식 검증 (PMC + 숫자)
     * EN: Validate PMCID format (PMC + digits)
     *
     * @param pmcid PMCID 값 / PMCID value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidPmcid(String pmcid) {
        if (pmcid == null || pmcid.isBlank()) {
            return false;
        }
        return pmcid.matches("^PMC\\d+$");
    }

    /**
     * ORCID 검증 / Validate ORCID
     *
     * KR: ORCID 형식 검증 (0000-0000-0000-000X 패턴)
     * EN: Validate ORCID format (0000-0000-0000-000X pattern)
     *
     * @param orcid ORCID 값 / ORCID value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidOrcid(String orcid) {
        if (orcid == null || orcid.isBlank()) {
            return false;
        }
        // ORCID 형식: 0000-0000-0000-000X (X는 숫자 또는 X)
        // URL 형식도 허용: https://orcid.org/0000-0000-0000-000X
        String cleanOrcid = orcid.replace("https://orcid.org/", "")
                                 .replace("http://orcid.org/", "");
        return cleanOrcid.matches("^\\d{4}-\\d{4}-\\d{4}-\\d{3}[0-9X]$");
    }

    /**
     * ISNI 검증 / Validate ISNI
     *
     * KR: ISNI 형식 검증 (0000 0000 0000 000X 패턴)
     * EN: Validate ISNI format (0000 0000 0000 000X pattern)
     *
     * @param isni ISNI 값 / ISNI value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidIsni(String isni) {
        if (isni == null || isni.isBlank()) {
            return false;
        }
        // ISNI 형식: 0000 0000 0000 000X (공백 또는 없음)
        String cleanIsni = isni.replaceAll("\\s+", "");
        return cleanIsni.matches("^\\d{15}[0-9X]$");
    }

    /**
     * ROR 검증 / Validate ROR
     *
     * KR: ROR 형식 검증 (https://ror.org/... 패턴)
     * EN: Validate ROR format (https://ror.org/... pattern)
     *
     * @param ror ROR 값 / ROR value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidRor(String ror) {
        if (ror == null || ror.isBlank()) {
            return false;
        }
        // ROR 형식: https://ror.org/0abcdef12 (9자리 영숫자)
        return ror.matches("^https://ror\\.org/0[a-z0-9]{8}$");
    }

    /**
     * ISBN 검증 / Validate ISBN
     *
     * KR: ISBN 형식 검증 (10자리 또는 13자리)
     * EN: Validate ISBN format (10 or 13 digits)
     *
     * @param isbn ISBN 값 / ISBN value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return false;
        }
        // 하이픈 제거
        String cleanIsbn = isbn.replaceAll("[\\s-]", "");

        // ISBN-10 또는 ISBN-13 (마지막 자리는 X 가능)
        return cleanIsbn.matches("^\\d{9}[0-9X]$") ||  // ISBN-10
               cleanIsbn.matches("^\\d{13}$");         // ISBN-13
    }

    /**
     * ISSN 검증 / Validate ISSN
     *
     * KR: ISSN 형식 검증 (XXXX-XXXX 패턴)
     * EN: Validate ISSN format (XXXX-XXXX pattern)
     *
     * @param issn ISSN 값 / ISSN value
     * @return 검증 결과 / validation result
     */
    public static boolean isValidIssn(String issn) {
        if (issn == null || issn.isBlank()) {
            return false;
        }
        // ISSN 형식: XXXX-XXXX (마지막 자리는 X 가능)
        String cleanIssn = issn.replaceAll("\\s+", "");
        return cleanIssn.matches("^\\d{4}-?\\d{3}[0-9X]$");
    }
}
