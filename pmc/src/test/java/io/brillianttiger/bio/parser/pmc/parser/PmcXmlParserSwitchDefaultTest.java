package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.Tr;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

/**
 * PmcXmlParser의 switch default case 커버리지 테스트
 *
 * parseTr의 switch default case (th, td가 아닌 요소) 커버
 */
@DisplayName("PmcXmlParser Switch Default Case Tests")
class PmcXmlParserSwitchDefaultTest {

    @Test
    @DisplayName("parseTr() - switch default case: th, td가 아닌 요소")
    void testParseTr_SwitchDefaultCase() throws Exception {
        // Given: tr 안에 th, td가 아닌 다른 요소 (잘못된 XML이지만 방어적 코드 테스트)
        // abbr의 END_ELEMENT를 만나면 break되므로 첫 th만 파싱됨
        String xml = """
            <tr xmlns:xlink="http://www.w3.org/1999/xlink">
                <th>Header</th>
                <abbr>This should trigger switch default</abbr>
            </tr>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseTr", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        Tr tr = (Tr) method.invoke(parser, reader);

        // Then: abbr START_ELEMENT에서 switch default, abbr END_ELEMENT에서 break
        assertThat(tr).isNotNull();
        assertThat(tr.getHeaderCells()).hasSize(1);
        assertThat(tr.getDataCells()).isNull();
    }

    @Test
    @DisplayName("parseTr() - switch default case: 다른 invalid 요소")
    void testParseTr_MultipleInvalidElements() throws Exception {
        // Given: tr 안에 td 먼저, 그 다음 invalid 요소
        // span의 END_ELEMENT를 만나면 break됨
        String xml = """
            <tr xmlns:xlink="http://www.w3.org/1999/xlink">
                <td>D1</td>
                <span>Invalid element</span>
            </tr>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseTr", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        Tr tr = (Tr) method.invoke(parser, reader);

        // Then: span START_ELEMENT에서 switch default, span END_ELEMENT에서 break
        assertThat(tr).isNotNull();
        assertThat(tr.getHeaderCells()).isNull();
        assertThat(tr.getDataCells()).hasSize(1);
    }

    // ==================== Helper Methods ====================

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        return factory.createXMLStreamReader(new StringReader(xml));
    }
}
