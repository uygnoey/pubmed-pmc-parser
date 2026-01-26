package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Permissions / 권한
 *
 * KR: 저작권 및 라이선스 정보. JATS 1.4 완전 준수 모델.
 * EN: Copyright and license information. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT permissions (
 *          copyright-statement*,
 *          copyright-year*,
 *          copyright-holder*,
 *          (ali:free_to_read | license)*
 *      )>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/permissions.html
 *
 * Note: Contains copyright and licensing information for the article.
 * Multiple copyright statements, years, and holders are allowed to accommodate
 * different jurisdictions or versions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permissions {

    /**
     * 저작권 문구 목록 / Copyright statement list
     *
     * KR: 저작권 문구 목록.
     * EN: Copyright statement list.
     *
     * DTD: copyright-statement*
     * Required: NO (0 or more)
     *
     * Example: "© 2024 The Authors. Published by Example Publisher."
     */
    private List<CopyrightStatement> copyrightStatements;

    /**
     * 저작권 연도 목록 / Copyright year list
     *
     * KR: 저작권 연도 목록.
     * EN: Copyright year list.
     *
     * DTD: copyright-year*
     * Required: NO (0 or more)
     *
     * Example: "2024"
     */
    private List<CopyrightYear> copyrightYears;

    /**
     * 저작권 보유자 목록 / Copyright holder list
     *
     * KR: 저작권 보유자 목록.
     * EN: Copyright holder list.
     *
     * DTD: copyright-holder*
     * Required: NO (0 or more)
     *
     * Example: "The Authors", "Example University"
     */
    private List<CopyrightHolder> copyrightHolders;

    /**
     * 라이선스 목록 / License list
     *
     * KR: 라이선스 목록 (Creative Commons, Open Access 등).
     * EN: License list (Creative Commons, Open Access, etc.).
     *
     * DTD: license*
     * Required: NO (0 or more)
     *
     * Note: Multiple licenses can be present for different versions or jurisdictions.
     */
    private List<License> licenses;
}
