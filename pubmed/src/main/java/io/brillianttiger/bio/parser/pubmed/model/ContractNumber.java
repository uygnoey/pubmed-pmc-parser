package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ContractNumber / 계약 번호
 *
 * DTD: <!ELEMENT ContractNumber (#PCDATA)>
 *
 * KR: 계약 번호 (DTD에 정의되어 있으나 사용처 미확인)
 * EN: Contract number (defined in DTD but usage unclear)
 *
 * NOTE: DTD에 정의되어 있지만 실제 사용하는 상위 요소가 명확하지 않음
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractNumber {

    /**
     * 계약 번호 / Contract number
     */
    private String value;
}
