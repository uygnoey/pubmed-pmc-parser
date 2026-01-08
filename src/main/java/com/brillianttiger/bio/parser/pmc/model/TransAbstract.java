package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TransAbstract / 번역 초록
 *
 * KR: 번역된 초록
 * EN: Translated abstract
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransAbstract {
    private String xmlLang;
    private String abstractType;
    private java.util.List<P> paragraphs;
    private String value;
}
