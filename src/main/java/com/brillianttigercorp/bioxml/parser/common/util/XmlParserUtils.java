package com.brillianttigercorp.bioxml.parser.common.util;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * XmlParserUtils / XML 파싱 유틸리티
 *
 * KR: XML 파싱에 필요한 공통 유틸리티 메서드
 * EN: Common utility methods for XML parsing
 */
public class XmlParserUtils {

    /**
     * XXE 공격 방지가 적용된 안전한 XMLInputFactory 생성
     * Create secure XMLInputFactory with XXE attack prevention
     *
     * KR: XXE (XML External Entity) 공격을 방지하기 위한 보안 설정이 적용된 XMLInputFactory 반환
     * EN: Returns XMLInputFactory with security settings to prevent XXE (XML External Entity) attacks
     *
     * @return 보안 설정이 적용된 XMLInputFactory / XMLInputFactory with security settings
     */
    public static XMLInputFactory createSecureXMLInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        // XXE 공격 방지 / Prevent XXE attacks
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        // 성능 최적화: 인접한 텍스트 노드 병합 / Performance optimization: coalesce adjacent text nodes
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);

        return factory;
    }

    /**
     * 요소의 텍스트 콘텐츠 추출 / Extract text content from element
     *
     * KR: XML 요소의 모든 텍스트 내용을 추출하여 반환
     * EN: Extract and return all text content from XML element
     *
     * @param reader XMLStreamReader 객체 / XMLStreamReader object
     * @param elementName 요소 이름 / Element name
     * @return 텍스트 내용 (공백 제거) / Text content (trimmed)
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     */
    public static String parseTextContent(XMLStreamReader reader, String elementName) throws XMLStreamException {
        StringBuilder content = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
                content.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals(elementName)) {
                    break;
                }
            }
        }

        return content.toString().trim();
    }

    /**
     * 혼합 콘텐츠 추출 (텍스트 + 중첩 요소) / Extract mixed content (text + nested elements)
     *
     * KR: 텍스트와 하위 요소가 혼합된 콘텐츠를 모두 추출하여 반환
     * EN: Extract and return mixed content containing both text and nested elements
     *
     * @param reader XMLStreamReader 객체 / XMLStreamReader object
     * @param elementName 요소 이름 / Element name
     * @return 혼합 콘텐츠 (공백 제거) / Mixed content (trimmed)
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     */
    public static String parseMixedContent(XMLStreamReader reader, String elementName) throws XMLStreamException {
        StringBuilder content = new StringBuilder();
        int depth = 1; // 현재 요소 깊이 추적 / Track current element depth

        while (reader.hasNext() && depth > 0) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.CDATA:
                    content.append(reader.getText());
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    depth++;
                    // 중첩 요소는 건너뛰고 텍스트만 추출 / Skip nested elements, extract text only
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    depth--;
                    if (depth == 0 && reader.getLocalName().equals(elementName)) {
                        return content.toString().trim();
                    }
                    break;

                default:
                    // 다른 이벤트는 무시 / Ignore other events
                    break;
            }
        }

        return content.toString().trim();
    }

    /**
     * 요소 건너뛰기 / Skip element
     *
     * KR: 처리하지 않는 요소와 모든 하위 요소를 건너뜀
     * EN: Skip unprocessed element and all its children
     *
     * @param reader XMLStreamReader 객체 / XMLStreamReader object
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     */
    public static void skipElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;

        while (reader.hasNext() && depth > 0) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                depth++;
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                depth--;
            }
        }
    }

    /**
     * 속성 값 안전하게 추출 / Safely extract attribute value
     *
     * KR: 속성 값을 추출하되, null인 경우 기본값 반환
     * EN: Extract attribute value, return default value if null
     *
     * @param reader XMLStreamReader 객체 / XMLStreamReader object
     * @param attributeName 속성 이름 / Attribute name
     * @param defaultValue 기본값 / Default value
     * @return 속성 값 또는 기본값 / Attribute value or default value
     */
    public static String getAttributeValue(XMLStreamReader reader, String attributeName, String defaultValue) {
        String value = reader.getAttributeValue(null, attributeName);
        return value != null ? value : defaultValue;
    }

    /**
     * 현재 요소 이름 확인 / Check current element name
     *
     * KR: 현재 위치의 요소가 지정된 이름과 일치하는지 확인
     * EN: Check if current element matches the specified name
     *
     * @param reader XMLStreamReader 객체 / XMLStreamReader object
     * @param expectedName 예상 요소 이름 / Expected element name
     * @return 일치 여부 / Whether it matches
     */
    public static boolean isStartElement(XMLStreamReader reader, String expectedName) {
        return reader.getEventType() == XMLStreamConstants.START_ELEMENT
                && reader.getLocalName().equals(expectedName);
    }

    /**
     * 요소 종료 확인 / Check element end
     *
     * KR: 현재 위치가 지정된 요소의 종료 태그인지 확인
     * EN: Check if current position is the end tag of specified element
     *
     * @param reader XMLStreamReader 객체 / XMLStreamReader object
     * @param expectedName 예상 요소 이름 / Expected element name
     * @return 일치 여부 / Whether it matches
     */
    public static boolean isEndElement(XMLStreamReader reader, String expectedName) {
        return reader.getEventType() == XMLStreamConstants.END_ELEMENT
                && reader.getLocalName().equals(expectedName);
    }

    /**
     * 비어있지 않은 텍스트 확인 / Check non-empty text
     *
     * KR: 텍스트가 null이 아니고 공백이 아닌 경우 true 반환
     * EN: Return true if text is not null and not blank
     *
     * @param text 확인할 텍스트 / Text to check
     * @return 비어있지 않은 경우 true / True if not empty
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * 안전한 문자열 변환 / Safe string conversion
     *
     * KR: null 또는 빈 문자열을 null로 변환
     * EN: Convert null or empty string to null
     *
     * @param text 변환할 텍스트 / Text to convert
     * @return null 또는 원본 텍스트 / Null or original text
     */
    public static String toNullIfEmpty(String text) {
        return (text == null || text.trim().isEmpty()) ? null : text;
    }
}
