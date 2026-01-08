package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pmc.parser.CommonPmcElementParser.*;

/**
 * BodyParser / Body 파서
 *
 * KR: PMC XML의 Body (본문) 및 Sec (섹션) 요소들을 파싱하는 클래스
 * EN: Class for parsing Body and Sec (section) elements in PMC XML
 */
public class BodyParser {

    /**
     * Body 파싱 / Parse Body
     * DTD: <!ELEMENT body (%body-model;)*>
     */
    public static Body parseBody(XMLStreamReader reader) throws XMLStreamException {
        Body.BodyBuilder builder = Body.builder();

        List<Sec> sections = new ArrayList<>();
        List<P> paragraphs = new ArrayList<>();
        StringBuilder content = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "sec":
                        sections.add(parseSec(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                content.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("body")) {
                    break;
                }
            }
        }

        builder.sections(sections.isEmpty() ? null : sections);
        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);

        String contentStr = content.toString().trim();
        builder.content(contentStr.isEmpty() ? null : contentStr);

        return builder.build();
    }

    /**
     * Sec 파싱 (재귀 구조) / Parse Sec (recursive structure)
     * DTD: <!ELEMENT sec (sec-meta?, label?, title?, (%sec-model;)*, (%sec-back-matter-mix;)*, sec*)>
     * DTD: <!ATTLIST sec id ID #IMPLIED sec-type CDATA #IMPLIED xml:lang CDATA #IMPLIED>
     */
    public static Sec parseSec(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String secType = reader.getAttributeValue(null, "sec-type");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");

        Sec.SecBuilder builder = Sec.builder()
                .id(id)
                .secType(secType)
                .xmlLang(xmlLang);

        List<P> paragraphs = new ArrayList<>();
        List<Sec> sections = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "sec-meta":
                        builder.secMeta(parseSecMeta(reader));
                        break;
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "sec":
                        // 재귀: 하위 섹션 파싱 / Recursive: parse sub-section
                        sections.add(parseSec(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("sec")) {
                    break;
                }
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.sections(sections.isEmpty() ? null : sections);

        return builder.build();
    }

    /**
     * SecMeta 파싱 / Parse SecMeta
     */
    public static SecMeta parseSecMeta(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "sec-meta");
        return SecMeta.builder().value(value).build();
    }
}
