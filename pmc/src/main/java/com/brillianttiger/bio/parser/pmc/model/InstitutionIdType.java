package com.brillianttiger.bio.parser.pmc.model;

import lombok.Getter;

/**
 * InstitutionIdType / 기관 ID 유형 Enum
 *
 * KR: 기관 식별자의 유형을 나타내는 열거형.
 *     ROR, ISNI, Ringgold, GRID 등 다양한 기관 식별 체계 지원.
 * EN: Enumeration representing institution identifier types.
 *     Supports various institution identification systems such as ROR, ISNI, Ringgold, GRID, etc.
 *
 * DTD: <!ATTLIST institution-id
 *          institution-id-type CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/institution-id.html
 *
 * Common values:
 * - ror: Research Organization Registry
 * - isni: International Standard Name Identifier
 * - ringgold: Ringgold Identifier
 * - grid: Global Research Identifier Database
 */
@Getter
public enum InstitutionIdType {
    /**
     * ROR (Research Organization Registry)
     *
     * KR: 연구 기관 등록부 - 전 세계 연구 기관의 고유 식별자
     * EN: Research Organization Registry - unique identifier for research organizations worldwide
     *
     * Example: https://ror.org/02mhbdp94
     * URL Format: https://ror.org/[9-character-id]
     */
    ROR("ror"),

    /**
     * ISNI (International Standard Name Identifier)
     *
     * KR: 국제 표준 이름 식별자 - 기관 및 개인을 식별
     * EN: International Standard Name Identifier - identifies institutions and individuals
     *
     * Example: 0000 0001 2345 6789
     * Format: 16-digit number
     */
    ISNI("isni"),

    /**
     * Ringgold Identifier
     *
     * KR: Ringgold 식별자 - 출판 및 학술 기관 식별 체계
     * EN: Ringgold Identifier - identification system for publishing and academic institutions
     *
     * Example: 12345
     * Format: Numeric ID
     */
    RINGGOLD("ringgold"),

    /**
     * GRID (Global Research Identifier Database)
     *
     * KR: 글로벌 연구 식별자 데이터베이스
     * EN: Global Research Identifier Database
     *
     * Note: GRID has been replaced by ROR as of 2021
     * Example: grid.1234.5
     * Format: grid.[numeric].[numeric]
     */
    GRID("grid"),

    /**
     * Other / Unknown institution ID type
     *
     * KR: 기타 또는 알 수 없는 기관 ID 유형
     * EN: Other or unknown institution ID type
     */
    OTHER("other");

    private final String value;

    InstitutionIdType(String value) {
        this.value = value;
    }

    /**
     * 문자열 값으로부터 InstitutionIdType enum을 반환 / Return InstitutionIdType enum from string value
     *
     * KR: 주어진 문자열 값에 해당하는 InstitutionIdType을 반환.
     *     매칭되는 값이 없으면 OTHER를 반환.
     * EN: Returns the InstitutionIdType corresponding to the given string value.
     *     Returns OTHER if no matching value is found.
     *
     * @param value 기관 ID 유형 문자열 / Institution ID type string
     * @return InstitutionIdType enum 값 / InstitutionIdType enum value
     */
    public static InstitutionIdType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (InstitutionIdType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }
}
