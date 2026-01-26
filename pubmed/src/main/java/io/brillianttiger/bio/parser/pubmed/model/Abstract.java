package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Abstract / 초록
 *
 * DTD: <!ELEMENT Abstract (AbstractText+, CopyrightInformation?)>
 *
 * KR: 논문의 초록
 * EN: Article abstract
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Abstract {

    /**
     * 초록 텍스트 목록 / Abstract text list
     */
    private List<AbstractText> abstractTexts;

    /**
     * 저작권 정보 / Copyright information
     */
    private CopyrightInformation copyrightInformation;
}
