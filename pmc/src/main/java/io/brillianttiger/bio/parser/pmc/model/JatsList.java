package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS list 요소 / JATS list element
 *
 * <p>DTD: {@code <!ELEMENT list (label?, title?, list-item+)>}</p>
 *
 * <p>
 * KR: 목록<br>
 * EN: List
 * </p>
 *
 * <p><strong>Note:</strong> 클래스명이 JatsList인 이유는 java.util.List와의 이름 충돌을 방지하기 위함입니다.</p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/list.html">
 *      JATS list Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JatsList {

    /**
     * 텍스트 내용 / Text content
     */
    private String value;

    /**
     * 공통 속성: id
     */
    private String id;

    /**
     * 공통 속성: xml:lang
     */
    private String xmlLang;

    /**
     * 목록 타입 (예: bullet, order, simple)
     */
    private String listType;

    /**
     * 라벨
     */
    private Label label;

    /**
     * 제목
     */
    private Title title;

    /**
     * 목록 항목들 / List items
     */
    @Builder.Default
    private List<ListItem> listItems = new ArrayList<>();

}
