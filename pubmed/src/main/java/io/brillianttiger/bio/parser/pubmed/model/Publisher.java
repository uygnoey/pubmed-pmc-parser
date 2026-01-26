package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Publisher / 출판사
 *
 * DTD: <!ELEMENT Publisher (PublisherName, PublisherLocation?)>
 *
 * KR: 출판사 정보
 * EN: Publisher information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    /**
     * 출판사명 / Publisher name
     */
    private PublisherName publisherName;

    /**
     * 출판사 위치 / Publisher location
     */
    private PublisherLocation publisherLocation;
}
