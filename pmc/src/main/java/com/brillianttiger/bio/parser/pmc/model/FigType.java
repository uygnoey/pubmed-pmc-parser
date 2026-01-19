package com.brillianttiger.bio.parser.pmc.model;

/**
 * FigType / 그림 유형
 *
 * KR: JATS fig-type 속성 값을 나타내는 열거형.
 *     그림의 종류를 분류하는 데 사용.
 * EN: Enumeration representing JATS fig-type attribute values.
 *     Used to classify the type of figure.
 *
 * DTD: fig-type CDATA #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/fig-type.html
 *
 * Common Values (확장 가능):
 * - map: 지도
 * - chart: 차트, 그래프
 * - scheme: 반응식 등 도식
 * - drawing: 도면, 스케치
 * - photo: 사진
 * - illustration: 일러스트레이션
 */
public enum FigType {

    /**
     * 지도 / Map
     * KR: 지도, 지리적 표현
     * EN: Map, geographical representation
     */
    MAP("map"),

    /**
     * 차트 / Chart
     * KR: 차트, 그래프
     * EN: Chart, graph
     */
    CHART("chart"),

    /**
     * 도식 / Scheme
     * KR: 반응식, 도식, 다이어그램
     * EN: Reaction scheme, diagram
     */
    SCHEME("scheme"),

    /**
     * 도면 / Drawing
     * KR: 도면, 스케치
     * EN: Drawing, sketch
     */
    DRAWING("drawing"),

    /**
     * 사진 / Photo
     * KR: 사진, 현미경 이미지 등
     * EN: Photo, microscopy image, etc.
     */
    PHOTO("photo"),

    /**
     * 일러스트레이션 / Illustration
     * KR: 일러스트레이션, 예술적 표현
     * EN: Illustration, artistic representation
     */
    ILLUSTRATION("illustration"),

    /**
     * 화학 구조식 / Chemical structure
     * KR: 화학 구조식, 분자 구조도
     * EN: Chemical structure, molecular structure diagram
     */
    CHEMICAL_STRUCTURE("chemical-structure"),

    /**
     * 기타 / Other
     * KR: 기타 그림 유형
     * EN: Other figure types not listed above
     */
    OTHER("other");

    private final String value;

    FigType(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return fig-type 값 (예: "chart")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 FigType 변환 / Convert from string to FigType
     *
     * KR: XML에서 파싱한 문자열을 FigType enum으로 변환.
     *     매칭되는 값이 없으면 OTHER 반환.
     * EN: Converts parsed string from XML to FigType enum.
     *     Returns OTHER if no matching value found.
     *
     * @param value fig-type 속성 값 / fig-type attribute value
     * @return 해당하는 FigType, 없으면 OTHER / Corresponding FigType, or OTHER if not found
     */
    public static FigType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (FigType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }

    @Override
    public String toString() {
        return value;
    }
}
