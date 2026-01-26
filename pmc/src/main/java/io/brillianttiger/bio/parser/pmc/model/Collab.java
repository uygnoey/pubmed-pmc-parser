package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Collab / 협력 저자
 *
 * KR: 단체 저자 또는 협력 그룹.
 *     연구 컨소시엄, 그룹 저자, 조직 저자 등을 표현.
 *     JATS 1.4 완전 준수 모델.
 * EN: Collective author or collaboration group.
 *     Represents research consortiums, group authors, organizational authors, etc.
 *     Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT collab (#PCDATA | %all-phrase; | %contrib-elements;)*>
 *
 * DTD: <!ATTLIST collab
 *          collab-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/collab.html
 *
 * Note: Can contain mixed content including contributor information, making it
 *       possible to represent both the collaboration name and individual members.
 *
 * Examples:
 * <collab>The Cancer Genome Atlas Research Network</collab>
 * <collab collab-type="consortium">International HapMap Consortium</collab>
 * <collab id="collab1">ENCODE Project Consortium</collab>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collab {

    /**
     * 협력 유형 / Collaboration type
     *
     * KR: 협력의 유형 (consortium, workgroup, committee 등).
     * EN: Type of collaboration (consortium, workgroup, committee, etc.).
     *
     * DTD: collab-type CDATA #IMPLIED
     *
     * Common values:
     * - "consortium": 연구 컨소시엄 / Research consortium
     * - "workgroup": 작업 그룹 / Working group
     * - "committee": 위원회 / Committee
     * - "group": 일반 그룹 / General group
     */
    private String collabType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 협력 그룹의 고유 ID (문서 내 참조용).
     * EN: Unique ID of the collaboration (for in-document reference).
     *
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 협력 정보의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this collaboration information (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * XLink actuate 속성 / XLink actuate attribute
     *
     * KR: 링크 활성화 동작 (onLoad, onRequest, other, none).
     * EN: Link activation behavior (onLoad, onRequest, other, none).
     *
     * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     */
    private String xlinkActuate;

    /**
     * XLink href 속성 / XLink href attribute
     *
     * KR: 협력 그룹의 외부 링크 (웹사이트 등).
     * EN: External link to the collaboration (website, etc.).
     *
     * DTD: xlink:href CDATA #IMPLIED
     *
     * Example: "https://www.cancer.gov/tcga"
     */
    private String xlinkHref;

    /**
     * 협력 저자명 / Collaboration name
     *
     * KR: 협력 그룹의 이름 (텍스트 내용).
     *     Mixed content를 포함할 수 있음.
     * EN: Name of the collaboration (text content).
     *     Can contain mixed content.
     *
     * DTD: (#PCDATA | %all-phrase; | %contrib-elements;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     *
     * Examples:
     * - "The Cancer Genome Atlas Research Network"
     * - "International HapMap Consortium"
     * - "ENCODE Project Consortium"
     */
    private String value;

    /**
     * 기여자 목록 / Contributor list
     *
     * KR: 협력 그룹 내의 개별 기여자 목록 (선택적).
     * EN: List of individual contributors within the collaboration (optional).
     *
     * DTD: %contrib-elements;*
     * Required: NO
     */
    private List<Contrib> contributors;
}
