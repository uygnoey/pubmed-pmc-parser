package io.brillianttiger.bio.parser.pubmed.model;

/**
 * PubModel / 출판 모델
 *
 * DTD: <!ATTLIST Article PubModel (Print | Print-Electronic | Electronic |
 *                                  Electronic-Print | Electronic-eCollection) #REQUIRED>
 *
 * KR: 논문의 출판 방식
 * EN: Article publication model
 */
public enum PubModel {
    /**
     * 인쇄 출판 / Print publication
     *
     * KR: 인쇄본으로만 출판
     * EN: Published in print only
     */
    Print("Print"),

    /**
     * 인쇄-전자 출판 / Print-Electronic publication
     *
     * KR: 인쇄본과 전자본 동시 출판
     * EN: Published in both print and electronic formats
     */
    Print_Electronic("Print-Electronic"),

    /**
     * 전자 출판 / Electronic publication
     *
     * KR: 전자본으로만 출판
     * EN: Published in electronic format only
     */
    Electronic("Electronic"),

    /**
     * 전자-인쇄 출판 / Electronic-Print publication
     *
     * KR: 전자본 먼저 출판 후 인쇄본
     * EN: Published electronically first, then in print
     */
    Electronic_Print("Electronic-Print"),

    /**
     * 전자 컬렉션 / Electronic-eCollection
     *
     * KR: 전자 컬렉션으로 출판
     * EN: Published as electronic collection
     */
    Electronic_eCollection("Electronic-eCollection");

    private final String value;

    PubModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 PubModel enum 변환 / Convert from string to PubModel enum
     *
     * @param value 문자열 값 / String value
     * @return PubModel enum
     */
    public static PubModel fromValue(String value) {
        for (PubModel model : values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Unknown PubModel value: " + value);
    }
}
