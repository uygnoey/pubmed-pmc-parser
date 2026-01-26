package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CollectiveName / 단체명
 *
 * DTD: <!ELEMENT CollectiveName (%text;)*>
 * DTD: <!ATTLIST CollectiveName
 *          Investigators IDREF #IMPLIED>
 *
 * KR: 단체 저자명 (예: "WHO Study Group")
 * EN: Collective author name
 *
 * **2024 변경사항**: Investigators 속성 추가
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectiveName {

    /**
     * 조사자 목록 ID 참조 (2024 신규) / Investigator list ID reference (2024 new)
     */
    private String investigators;

    /**
     * 단체명 / Collective name
     */
    private String value;
}
