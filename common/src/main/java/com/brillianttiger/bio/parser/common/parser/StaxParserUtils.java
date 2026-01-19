package com.brillianttiger.bio.parser.common.parser;

import javax.xml.stream.*;
import java.util.*;

/**
 * StaxParserUtils / StAX 파싱 유틸리티
 *
 * KR: StAX 파싱 유틸리티.
 * EN: StAX parsing utilities.
 */
public final class StaxParserUtils {

    private StaxParserUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * 다음 시작 요소까지 이동 / Move to next start element
     *
     * @param reader XMLStreamReader
     * @return true if start element found, false otherwise
     * @throws XMLStreamException if parsing error occurs
     */
    public static boolean moveToStartElement(XMLStreamReader reader)
            throws XMLStreamException {
        while (reader.hasNext()) {
            if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                return true;
            }
        }
        return false;
    }

    /**
     * 특정 이름의 다음 시작 요소까지 이동 / Move to next start element with specific name
     *
     * @param reader XMLStreamReader
     * @param elementName target element name
     * @return true if element found, false otherwise
     * @throws XMLStreamException if parsing error occurs
     */
    public static boolean moveToStartElement(XMLStreamReader reader, String elementName)
            throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT &&
                elementName.equals(reader.getLocalName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 현재 요소의 모든 속성을 Map으로 반환 / Get all attributes as Map
     *
     * @param reader XMLStreamReader
     * @return Map of attribute names to values
     */
    public static Map<String, String> getAttributes(XMLStreamReader reader) {
        Map<String, String> attrs = new LinkedHashMap<>();
        int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String name = reader.getAttributeLocalName(i);
            String value = reader.getAttributeValue(i);
            attrs.put(name, value);
        }
        return attrs;
    }

    /**
     * 현재 요소가 특정 이름인지 확인 / Check if current element has specific name
     *
     * @param reader XMLStreamReader
     * @param elementName expected element name
     * @return true if matches, false otherwise
     */
    public static boolean isStartElement(XMLStreamReader reader, String elementName) {
        return reader.getEventType() == XMLStreamConstants.START_ELEMENT &&
               elementName.equals(reader.getLocalName());
    }

    /**
     * 현재 종료 요소가 특정 이름인지 확인 / Check if current end element has specific name
     *
     * @param reader XMLStreamReader
     * @param elementName expected element name
     * @return true if matches, false otherwise
     */
    public static boolean isEndElement(XMLStreamReader reader, String elementName) {
        return reader.getEventType() == XMLStreamConstants.END_ELEMENT &&
               elementName.equals(reader.getLocalName());
    }

    /**
     * 네임스페이스 URI 가져오기 / Get namespace URI
     *
     * @param reader XMLStreamReader
     * @return namespace URI or null
     */
    public static String getNamespaceURI(XMLStreamReader reader) {
        return reader.getNamespaceURI();
    }

    /**
     * 네임스페이스 프리픽스 가져오기 / Get namespace prefix
     *
     * @param reader XMLStreamReader
     * @return namespace prefix or null
     */
    public static String getPrefix(XMLStreamReader reader) {
        return reader.getPrefix();
    }
}
