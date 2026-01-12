package com.brillianttiger.bio.parser.pmc.model;

/**
 * XlinkShow / XLink 표시 방식
 *
 * KR: XLink show 속성 값을 나타내는 열거형.
 *     링크 리소스를 어떻게 표시할지 지정.
 * EN: Enumeration representing XLink show attribute values.
 *     Specifies how the linked resource should be displayed.
 *
 * DTD: xlink:show (embed | new | none | other | replace) #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/xlink-show.html
 */
public enum XlinkShow {

    /**
     * 내장 / Embed
     * KR: 링크 리소스를 현재 문서에 내장
     * EN: Embed the linked resource in the current document
     */
    EMBED("embed"),

    /**
     * 새 창 / New
     * KR: 새 창이나 탭에서 링크 리소스 표시
     * EN: Display the linked resource in a new window or tab
     */
    NEW("new"),

    /**
     * 없음 / None
     * KR: 링크 리소스를 자동으로 표시하지 않음
     * EN: Do not automatically display the linked resource
     */
    NONE("none"),

    /**
     * 기타 / Other
     * KR: 다른 방식으로 표시
     * EN: Some other display method
     */
    OTHER("other"),

    /**
     * 대체 / Replace
     * KR: 현재 리소스를 링크 리소스로 대체
     * EN: Replace the current resource with the linked resource
     */
    REPLACE("replace");

    private final String value;

    XlinkShow(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return xlink:show 값 (예: "new")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 XlinkShow 변환 / Convert from string to XlinkShow
     *
     * KR: XML에서 파싱한 문자열을 XlinkShow enum으로 변환.
     *     매칭되는 값이 없으면 null 반환.
     * EN: Converts parsed string from XML to XlinkShow enum.
     *     Returns null if no matching value found.
     *
     * @param value xlink:show 속성 값 / xlink:show attribute value
     * @return 해당하는 XlinkShow, 없으면 null / Corresponding XlinkShow, or null if not found
     */
    public static XlinkShow fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (XlinkShow show : values()) {
            if (show.value.equals(normalized)) {
                return show;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
