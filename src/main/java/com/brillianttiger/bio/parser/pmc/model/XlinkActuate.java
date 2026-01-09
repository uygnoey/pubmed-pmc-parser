package com.brillianttiger.bio.parser.pmc.model;

/**
 * XlinkActuate / XLink 실행 시점
 *
 * KR: XLink actuate 속성 값을 나타내는 열거형.
 *     링크 리소스를 언제 활성화할지 지정.
 * EN: Enumeration representing XLink actuate attribute values.
 *     Specifies when the linked resource should be activated.
 *
 * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/xlink-actuate.html
 */
public enum XlinkActuate {

    /**
     * 로드 시 실행 / On load
     * KR: 문서 로드 시 자동으로 링크 리소스 활성화
     * EN: Automatically activate the link when document loads
     */
    ON_LOAD("onLoad"),

    /**
     * 요청 시 실행 / On request
     * KR: 사용자 요청(클릭 등) 시 링크 리소스 활성화
     * EN: Activate the link when user requests (e.g., click)
     */
    ON_REQUEST("onRequest"),

    /**
     * 기타 / Other
     * KR: 다른 방식으로 활성화
     * EN: Some other activation method
     */
    OTHER("other"),

    /**
     * 없음 / None
     * KR: 자동 활성화 없음
     * EN: No automatic activation
     */
    NONE("none");

    private final String value;

    XlinkActuate(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return xlink:actuate 값 (예: "onRequest")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 XlinkActuate 변환 / Convert from string to XlinkActuate
     *
     * KR: XML에서 파싱한 문자열을 XlinkActuate enum으로 변환.
     *     매칭되는 값이 없으면 null 반환.
     * EN: Converts parsed string from XML to XlinkActuate enum.
     *     Returns null if no matching value found.
     *
     * @param value xlink:actuate 속성 값 / xlink:actuate attribute value
     * @return 해당하는 XlinkActuate, 없으면 null / Corresponding XlinkActuate, or null if not found
     */
    public static XlinkActuate fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim();
        for (XlinkActuate actuate : values()) {
            if (actuate.value.equalsIgnoreCase(normalized)) {
                return actuate;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
