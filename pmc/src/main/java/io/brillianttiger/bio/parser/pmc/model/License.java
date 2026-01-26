package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * License / 라이선스
 *
 * KR: 저작물 사용 권한 및 라이선스 정보. JATS 1.4 완전 준수 모델.
 * EN: License information for the work. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT license (ali:license_ref*, (%id.class;)*, (%license-p.class; | %x.class;)*)>
 *      <!ATTLIST license
 *          %jats-common-atts;
 *          license-type CDATA #IMPLIED
 *          %might-link-atts;
 *          %xlink-simple-link-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/license.html
 *
 * Note: Contains licensing information, typically for open access articles.
 * Common license types include Creative Commons licenses (CC-BY, CC-BY-NC, etc.).
 *
 * Example:
 * <license license-type="open-access" xlink:href="http://creativecommons.org/licenses/by/4.0/">
 *   <license-p>This is an open access article distributed under the terms of the
 *   Creative Commons Attribution License.</license-p>
 * </license>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class License {

    /**
     * 라이선스 타입 / License type
     *
     * KR: 라이선스 유형 (open-access, CC-BY 등).
     * EN: Type of license (open-access, CC-BY, etc.).
     *
     * DTD: license-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "open-access", "CC-BY-4.0", "CC-BY-NC-SA-4.0"
     */
    private String licenseType;

    /**
     * XLink 참조 URL / XLink reference URL
     *
     * KR: 라이선스 상세 정보 URL (Creative Commons 라이선스 페이지 등).
     * EN: URL to license details (e.g., Creative Commons license page).
     *
     * DTD: xlink:href CDATA #IMPLIED (from xlink-simple-link-atts)
     * Required: NO
     *
     * Example: "http://creativecommons.org/licenses/by/4.0/"
     */
    private String xlinkHref;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 라이선스의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this license.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     *
     * Example: "web-only", "print-only", "supplementary-material"
     */
    private String specificUse;

    /**
     * 라이선스 단락 목록 / License paragraph list
     *
     * KR: 라이선스 설명 텍스트를 담은 단락 목록.
     * EN: List of paragraphs containing license description text.
     *
     * DTD: license-p*
     * Required: NO (0 or more)
     *
     * Note: license-p elements contain the human-readable license text.
     * Multiple paragraphs can be used for complex license descriptions.
     */
    private List<P> paragraphs;
}
