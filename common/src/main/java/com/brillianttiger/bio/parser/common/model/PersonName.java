package com.brillianttiger.bio.parser.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PersonName / 저자/연구자 이름 공통 모델
 *
 * KR: 저자/연구자 이름 공통 모델.
 *     PubMed의 Author, Investigator와 JATS의 contrib, name에 사용.
 * EN: Common model for author/investigator names.
 *     Used for PubMed Author, Investigator and JATS contrib, name.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonName {

    // 이름 컴포넌트 / Name components
    private String lastName;        // surname
    private String foreName;        // given-names
    private String initials;
    private String suffix;
    private String prefix;          // JATS only

    // 단체명 (개인이 아닌 경우) / Collective name (if not individual)
    private String collectiveName;

    // 식별자 / Identifiers
    private List<PersonIdentifier> identifiers;

    // 소속 / Affiliations
    private List<Affiliation> affiliations;

    // 속성 / Attributes
    private boolean valid;          // ValidYN
    private boolean equalContrib;   // EqualContrib
    private boolean corresponding;  // JATS corresp
    private boolean deceased;       // JATS deceased

    // 역할 (JATS) / Role (JATS)
    private String contribType;     // author, editor, etc.
    private List<String> roles;

    // 이름 스타일 (JATS) / Name style (JATS)
    private NameStyle nameStyle;

    /**
     * PersonIdentifier / 개인 식별자
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonIdentifier {
        private String source;      // ORCID, Scopus, etc.
        private String value;
        private Boolean authenticated;  // JATS authenticated
    }

    /**
     * Affiliation / 소속
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Affiliation {
        private String id;
        private String text;
        private List<Institution> institutions;
        private String country;
        private String email;
    }

    /**
     * Institution / 기관
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Institution {
        private String name;
        private String id;
        private String idType;      // ror, isni, ringgold
    }

    /**
     * NameStyle / 이름 스타일
     */
    public enum NameStyle {
        WESTERN,    // Given-Family (기본)
        EASTERN,    // Family-Given
        ISLENSK,    // 아이슬란드식
        GIVEN_ONLY  // 이름만
    }

    /**
     * 전체 이름 문자열 / Full name string
     *
     * @return formatted full name
     */
    public String getFullName() {
        if (collectiveName != null) {
            return collectiveName;
        }

        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix).append(" ");
        }
        if (foreName != null) {
            sb.append(foreName).append(" ");
        }
        if (lastName != null) {
            sb.append(lastName);
        }
        if (suffix != null) {
            sb.append(" ").append(suffix);
        }

        return sb.toString().trim();
    }
}
