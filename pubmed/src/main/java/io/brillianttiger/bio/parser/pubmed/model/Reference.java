package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reference / 참조
 *
 * DTD: <!ELEMENT Reference (Citation, ArticleIdList?)>
 *
 * KR: 참고문헌 정보
 * EN: Reference information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reference {

    /**
     * 인용 정보 / Citation information
     */
    private Citation citation;

    /**
     * 논문 ID 목록 / Article ID list
     */
    private ArticleIdList articleIdList;
}
