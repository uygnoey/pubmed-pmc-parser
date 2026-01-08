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
            case "orcid" -> IdType.ORCID;
            case "isni" -> IdType.ISNI;
            case "scopus" -> IdType.SCOPUS;
            case "ror" -> IdType.ROR;
            case "ringgold" -> IdType.RINGGOLD;
            case "grid" -> IdType.GRID;
            default -> IdType.OTHER;
        };
    }
}
