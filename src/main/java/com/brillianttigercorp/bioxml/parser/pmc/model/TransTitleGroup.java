package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TransTitleGroup / 번역 제목 그룹
 *
 * KR: 번역된 제목 그룹
 * EN: Translated title group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransTitleGroup {
    private String xmlLang;
    private Title transTitle;
    private Subtitle transSubtitle;
    private String value;
}
