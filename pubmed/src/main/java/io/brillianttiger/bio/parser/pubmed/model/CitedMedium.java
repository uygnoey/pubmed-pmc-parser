package io.brillianttiger.bio.parser.pubmed.model;

/**
 * CitedMedium / 인용 매체
 *
 * DTD: <!ATTLIST JournalIssue CitedMedium (Internet | Print) #REQUIRED>
 *
 * KR: 저널 호가 인용된 매체
 * EN: Medium in which the journal issue was cited
 */
public enum CitedMedium {
    /**
     * 인터넷 / Internet
     *
     * KR: 온라인/인터넷 매체
     * EN: Online/Internet medium
     */
    Internet("Internet"),

    /**
     * 인쇄 / Print
     *
     * KR: 인쇄 매체
     * EN: Print medium
     */
    Print("Print");

    private final String value;

    CitedMedium(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 CitedMedium enum 변환 / Convert from string to CitedMedium enum
     *
     * @param value 문자열 값 / String value
     * @return CitedMedium enum
     */
    public static CitedMedium fromValue(String value) {
        for (CitedMedium medium : values()) {
            if (medium.value.equals(value)) {
                return medium;
            }
        }
        throw new IllegalArgumentException("Unknown CitedMedium value: " + value);
    }
}
