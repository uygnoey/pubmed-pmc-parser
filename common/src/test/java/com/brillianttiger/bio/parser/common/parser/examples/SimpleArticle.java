package com.brillianttiger.bio.parser.common.parser.examples;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SimpleArticle / 간단한 논문 예시 모델
 *
 * KR: StreamParser 예시를 위한 간단한 논문 모델
 * EN: Simple article model for StreamParser example
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleArticle {

    /**
     * 논문 ID / Article ID
     */
    private String id;

    /**
     * 제목 / Title
     */
    private String title;

    /**
     * 저자 / Author
     */
    private String author;

    /**
     * 년도 / Year
     */
    private Integer year;
}
