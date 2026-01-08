package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChapterTitle / 챕터 제목
 *
 * KR: 책 챕터 제목
 * EN: Book chapter title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterTitle {
    private String value;
}
