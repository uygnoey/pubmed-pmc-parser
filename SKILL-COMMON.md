# SKILL: PubMed & PMC Parser - Common Implementation Guide

## Overview
PubMed와 PMC XML 파서의 공통 유틸리티 및 구현 가이드라인.

---

# Part 1: Project Structure

## 1.1 Recommended Package Structure
```
pubmed-pmc-parser/
├── src/main/java/
│   └── com/brillianttiger/bio/
│       ├── common/
│       │   ├── model/
│       │   │   ├── TextContent.java        # Mixed content 처리
│       │   │   ├── DateComponents.java     # 날짜 공통
│       │   │   ├── Identifier.java         # ID 공통
│       │   │   └── PersonName.java         # 이름 공통
│       │   ├── parser/
│       │   │   ├── XmlParserBase.java      # 파서 베이스
│       │   │   ├── StaxParserUtils.java    # StAX 유틸리티
│       │   │   └── DateParser.java         # 날짜 파싱
│       │   └── util/
│       │       ├── GzipUtils.java          # GZip 처리
│       │       ├── XmlEntityResolver.java  # Entity 해석
│       │       └── ValidationUtils.java    # 검증 유틸
│       ├── pubmed/
│       │   ├── model/                      # PubMed 모델 클래스
│       │   │   ├── PubmedArticleSet.java
│       │   │   ├── PubmedArticle.java
│       │   │   ├── MedlineCitation.java
│       │   │   └── ... (모든 요소)
│       │   └── parser/
│       │       ├── PubmedXmlParser.java
│       │       └── PubmedStreamParser.java
│       └── pmc/
│           ├── model/                      # PMC/JATS 모델 클래스
│           │   ├── JatsArticle.java
│           │   ├── Front.java
│           │   ├── Body.java
│           │   └── ... (모든 요소)
│           └── parser/
│               ├── PmcXmlParser.java
│               └── PmcStreamParser.java
├── src/test/java/
│   └── com/brillianttiger/bio/
│       ├── pubmed/
│       │   └── parser/
│       │       └── PubmedXmlParserTest.java
│       └── pmc/
│           └── parser/
│               └── PmcXmlParserTest.java
└── src/test/resources/
    ├── pubmed/
    │   └── sample.xml.gz
    └── pmc/
        └── sample.xml
```

---

# Part 2: Common Models

## 2.1 TextContent (Mixed Content Handler)
```java
import lombok.Data;
import lombok.Builder;
import java.util.List;

/**
 * Mixed content를 처리하는 공통 모델.
 * PubMed의 %text; entity와 JATS의 mixed content에 사용.
 * 
 * 지원 인라인 요소:
 * - PubMed: b, i, u, sup, sub
 * - JATS: bold, italic, underline, sup, sub, monospace, sc, xref, ext-link
 */
@Data
@Builder
public class TextContent {
    
    /** 원본 텍스트 (마크업 제거) */
    private String plainText;
    
    /** 마크업 보존 텍스트 (HTML 형식) */
    private String htmlText;
    
    /** 원본 XML 텍스트 */
    private String rawXml;
    
    /** 인라인 요소 목록 */
    private List<InlineElement> inlineElements;
    
    @Data
    @Builder
    public static class InlineElement {
        private InlineType type;
        private String content;
        private int startIndex;
        private int endIndex;
        private java.util.Map<String, String> attributes;
    }
    
    public enum InlineType {
        BOLD,           // PubMed: b, JATS: bold
        ITALIC,         // PubMed: i, JATS: italic
        UNDERLINE,      // PubMed: u, JATS: underline
        SUPERSCRIPT,    // sup
        SUBSCRIPT,      // sub
        MONOSPACE,      // JATS: monospace
        SMALL_CAPS,     // JATS: sc
        XREF,           // JATS: xref
        EXT_LINK,       // JATS: ext-link
        NAMED_CONTENT,  // JATS: named-content
        STYLED_CONTENT  // JATS: styled-content
    }
}
```

## 2.2 DateComponents
```java
import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;

/**
 * 날짜 컴포넌트 공통 모델.
 * PubMed의 PubDate, ArticleDate와 JATS의 pub-date, date에 사용.
 */
@Data
@Builder
public class DateComponents {
    
    private Integer year;
    private Integer month;
    private Integer day;
    private String season;          // "Spring", "Summer", etc.
    private String medlineDate;     // "2024 Jan-Feb" 같은 비정형
    private String stringDate;      // JATS string-date
    private String era;             // JATS era
    
    // JATS 전용 속성
    private String dateType;        // received, accepted, pub, etc.
    private String pubType;         // ppub, epub, etc.
    private String publicationFormat;  // print, electronic
    private String iso8601Date;     // ISO 8601 형식
    private String calendar;        // 달력 종류
    
    /**
     * LocalDate로 변환 (가능한 경우)
     */
    public LocalDate toLocalDate() {
        if (year == null) return null;
        int m = month != null ? month : 1;
        int d = day != null ? day : 1;
        return LocalDate.of(year, m, d);
    }
    
    /**
     * 날짜 문자열 표현
     */
    public String toDisplayString() {
        if (medlineDate != null) return medlineDate;
        if (stringDate != null) return stringDate;
        
        StringBuilder sb = new StringBuilder();
        if (year != null) sb.append(year);
        if (month != null) sb.append("-").append(String.format("%02d", month));
        if (day != null) sb.append("-").append(String.format("%02d", day));
        if (season != null) sb.append(" ").append(season);
        
        return sb.toString();
    }
}
```

## 2.3 PersonName
```java
import lombok.Data;
import lombok.Builder;
import java.util.List;

/**
 * 저자/연구자 이름 공통 모델.
 * PubMed의 Author, Investigator와 JATS의 contrib, name에 사용.
 */
@Data
@Builder
public class PersonName {
    
    // 이름 컴포넌트
    private String lastName;        // surname
    private String foreName;        // given-names
    private String initials;
    private String suffix;
    private String prefix;          // JATS only
    
    // 단체명 (개인이 아닌 경우)
    private String collectiveName;
    
    // 식별자
    private List<PersonIdentifier> identifiers;
    
    // 소속
    private List<Affiliation> affiliations;
    
    // 속성
    private boolean valid;          // ValidYN
    private boolean equalContrib;   // EqualContrib
    private boolean corresponding;  // JATS corresp
    private boolean deceased;       // JATS deceased
    
    // 역할 (JATS)
    private String contribType;     // author, editor, etc.
    private List<String> roles;
    
    // 이름 스타일 (JATS)
    private NameStyle nameStyle;
    
    @Data
    @Builder
    public static class PersonIdentifier {
        private String source;      // ORCID, Scopus, etc.
        private String value;
        private Boolean authenticated;  // JATS authenticated
    }
    
    @Data
    @Builder
    public static class Affiliation {
        private String id;
        private String text;
        private List<Institution> institutions;
        private String country;
        private String email;
    }
    
    @Data
    @Builder
    public static class Institution {
        private String name;
        private String id;
        private String idType;      // ror, isni, ringgold
    }
    
    public enum NameStyle {
        WESTERN,    // Given-Family (기본)
        EASTERN,    // Family-Given
        ISLENSK,    // 아이슬란드식
        GIVEN_ONLY  // 이름만
    }
    
    /**
     * 전체 이름 문자열
     */
    public String getFullName() {
        if (collectiveName != null) return collectiveName;
        
        StringBuilder sb = new StringBuilder();
        if (prefix != null) sb.append(prefix).append(" ");
        if (foreName != null) sb.append(foreName).append(" ");
        if (lastName != null) sb.append(lastName);
        if (suffix != null) sb.append(" ").append(suffix);
        
        return sb.toString().trim();
    }
}
```

## 2.4 Identifier (공통 ID)
```java
import lombok.Data;
import lombok.Builder;

/**
 * 각종 식별자 공통 모델.
 * DOI, PMID, PMCID, ORCID 등 모든 ID에 사용.
 */
@Data
@Builder
public class Identifier {
    
    private IdType type;
    private String value;
    private String source;          // PubMed Source 속성
    private String assigningAuthority;  // JATS
    private String specificUse;     // JATS
    private boolean validated;      // 유효성 확인 여부
    
    public enum IdType {
        // Article IDs
        DOI,
        PMID,
        PMCID,
        PMC_UID,
        PII,
        PUBLISHER_ID,
        MANUSCRIPT,
        MEDLINE,
        SICI,
        ARK,
        
        // Person IDs
        ORCID,
        ISNI,
        SCOPUS,
        RESEARCHER_ID,
        WOS_RESEARCHER_ID,
        
        // Institution IDs
        ROR,
        RINGGOLD,
        GRID,
        
        // Other
        OTHER
    }
    
    /**
     * ID 타입 문자열로부터 변환
     */
    public static IdType parseIdType(String typeStr) {
        if (typeStr == null) return IdType.OTHER;
        
        return switch (typeStr.toLowerCase()) {
            case "doi" -> IdType.DOI;
            case "pmid", "pubmed" -> IdType.PMID;
            case "pmcid", "pmc" -> IdType.PMCID;
            case "pmc-uid" -> IdType.PMC_UID;
            case "pii" -> IdType.PII;
            case "publisher-id" -> IdType.PUBLISHER_ID;
            case "manuscript" -> IdType.MANUSCRIPT;
            case "medline" -> IdType.MEDLINE;
            case "orcid" -> IdType.ORCID;
            case "isni" -> IdType.ISNI;
            case "scopus" -> IdType.SCOPUS;
            case "ror" -> IdType.ROR;
            case "ringgold" -> IdType.RINGGOLD;
            case "grid" -> IdType.GRID;
            default -> IdType.OTHER;
        };
    }
}
```

---

# Part 3: Parser Base Classes

## 3.1 XmlParserBase
```java
import javax.xml.stream.*;
import java.io.*;
import java.nio.file.*;
import java.util.zip.GZIPInputStream;

/**
 * XML 파서 기본 클래스.
 * StAX 기반 파싱의 공통 기능 제공.
 */
public abstract class XmlParserBase {
    
    protected final XMLInputFactory factory;
    
    protected XmlParserBase() {
        factory = XMLInputFactory.newInstance();
        configureFactory();
    }
    
    /**
     * XMLInputFactory 보안 설정
     */
    private void configureFactory() {
        // XXE (XML External Entity) 공격 방지
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        
        // 성능 최적화
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    }
    
    /**
     * 파일에서 InputStream 생성 (GZip 자동 감지)
     */
    protected InputStream openInputStream(Path path) throws IOException {
        InputStream is = Files.newInputStream(path);
        
        // GZip 압축 파일 처리
        String fileName = path.toString().toLowerCase();
        if (fileName.endsWith(".gz") || fileName.endsWith(".gzip")) {
            is = new GZIPInputStream(is);
        }
        
        return new BufferedInputStream(is, 65536);
    }
    
    /**
     * XMLStreamReader 생성
     */
    protected XMLStreamReader createReader(Path path) throws Exception {
        return factory.createXMLStreamReader(openInputStream(path));
    }
    
    protected XMLStreamReader createReader(InputStream is) throws Exception {
        return factory.createXMLStreamReader(is);
    }
    
    /**
     * 현재 요소의 텍스트 내용 추출
     */
    protected String getElementText(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder sb = new StringBuilder();
        
        while (reader.hasNext()) {
            int event = reader.next();
            
            switch (event) {
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.CDATA:
                    sb.append(reader.getText());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return sb.toString().trim();
                case XMLStreamConstants.START_ELEMENT:
                    // 중첩 요소는 건너뛰기
                    skipElement(reader);
                    break;
            }
        }
        
        return sb.toString().trim();
    }
    
    /**
     * Mixed content 추출 (마크업 보존)
     */
    protected TextContent getMixedContent(XMLStreamReader reader, String endTag) 
            throws XMLStreamException {
        StringBuilder plainText = new StringBuilder();
        StringBuilder htmlText = new StringBuilder();
        StringBuilder rawXml = new StringBuilder();
        int depth = 1;
        
        while (reader.hasNext() && depth > 0) {
            int event = reader.next();
            
            switch (event) {
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.CDATA:
                    String text = reader.getText();
                    plainText.append(text);
                    htmlText.append(escapeHtml(text));
                    rawXml.append(escapeXml(text));
                    break;
                    
                case XMLStreamConstants.START_ELEMENT:
                    depth++;
                    String tag = reader.getLocalName();
                    String htmlTag = mapToHtmlTag(tag);
                    htmlText.append("<").append(htmlTag).append(">");
                    rawXml.append("<").append(tag);
                    appendAttributes(reader, rawXml);
                    rawXml.append(">");
                    break;
                    
                case XMLStreamConstants.END_ELEMENT:
                    depth--;
                    if (depth > 0) {
                        String closeTag = reader.getLocalName();
                        String htmlCloseTag = mapToHtmlTag(closeTag);
                        htmlText.append("</").append(htmlCloseTag).append(">");
                        rawXml.append("</").append(closeTag).append(">");
                    }
                    break;
            }
        }
        
        return TextContent.builder()
                .plainText(plainText.toString().trim())
                .htmlText(htmlText.toString().trim())
                .rawXml(rawXml.toString().trim())
                .build();
    }
    
    /**
     * 현재 요소 건너뛰기
     */
    protected void skipElement(XMLStreamReader reader) throws XMLStreamException {
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
     * 속성값 가져오기 (null-safe)
     */
    protected String getAttribute(XMLStreamReader reader, String name) {
        return reader.getAttributeValue(null, name);
    }
    
    protected String getAttribute(XMLStreamReader reader, String namespace, String name) {
        return reader.getAttributeValue(namespace, name);
    }
    
    /**
     * 속성값을 boolean으로 변환
     */
    protected boolean getBooleanAttribute(XMLStreamReader reader, String name, boolean defaultValue) {
        String value = getAttribute(reader, name);
        if (value == null) return defaultValue;
        return "Y".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || 
               "true".equalsIgnoreCase(value);
    }
    
    /**
     * PubMed/JATS 인라인 태그를 HTML로 매핑
     */
    private String mapToHtmlTag(String tag) {
        return switch (tag) {
            case "b", "bold" -> "strong";
            case "i", "italic" -> "em";
            case "u", "underline" -> "u";
            case "sup" -> "sup";
            case "sub" -> "sub";
            case "sc" -> "span"; // style="font-variant: small-caps"
            case "monospace" -> "code";
            default -> "span";
        };
    }
    
    private void appendAttributes(XMLStreamReader reader, StringBuilder sb) {
        int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            sb.append(" ")
              .append(reader.getAttributeLocalName(i))
              .append("=\"")
              .append(escapeXml(reader.getAttributeValue(i)))
              .append("\"");
        }
    }
    
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }
    
    private String escapeXml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
```

## 3.2 StaxParserUtils
```java
import javax.xml.stream.*;
import java.util.*;

/**
 * StAX 파싱 유틸리티.
 */
public final class StaxParserUtils {
    
    private StaxParserUtils() {}
    
    /**
     * 다음 시작 요소까지 이동
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
     * 특정 이름의 다음 시작 요소까지 이동
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
     * 현재 요소의 모든 속성을 Map으로 반환
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
     * 현재 요소가 특정 이름인지 확인
     */
    public static boolean isStartElement(XMLStreamReader reader, String elementName) {
        return reader.getEventType() == XMLStreamConstants.START_ELEMENT &&
               elementName.equals(reader.getLocalName());
    }
    
    public static boolean isEndElement(XMLStreamReader reader, String elementName) {
        return reader.getEventType() == XMLStreamConstants.END_ELEMENT &&
               elementName.equals(reader.getLocalName());
    }
    
    /**
     * 네임스페이스 URI 가져오기
     */
    public static String getNamespaceURI(XMLStreamReader reader) {
        return reader.getNamespaceURI();
    }
    
    /**
     * 네임스페이스 프리픽스 가져오기
     */
    public static String getPrefix(XMLStreamReader reader) {
        return reader.getPrefix();
    }
}
```

---

# Part 4: Date Parsing

## 4.1 DateParser
```java
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;

/**
 * PubMed와 JATS의 다양한 날짜 형식 파싱.
 */
public final class DateParser {
    
    private static final Map<String, Integer> MONTH_MAP = new HashMap<>();
    private static final Map<String, String> SEASON_MONTH_MAP = new HashMap<>();
    
    static {
        // 월 이름 매핑
        MONTH_MAP.put("jan", 1); MONTH_MAP.put("january", 1);
        MONTH_MAP.put("feb", 2); MONTH_MAP.put("february", 2);
        MONTH_MAP.put("mar", 3); MONTH_MAP.put("march", 3);
        MONTH_MAP.put("apr", 4); MONTH_MAP.put("april", 4);
        MONTH_MAP.put("may", 5);
        MONTH_MAP.put("jun", 6); MONTH_MAP.put("june", 6);
        MONTH_MAP.put("jul", 7); MONTH_MAP.put("july", 7);
        MONTH_MAP.put("aug", 8); MONTH_MAP.put("august", 8);
        MONTH_MAP.put("sep", 9); MONTH_MAP.put("september", 9);
        MONTH_MAP.put("oct", 10); MONTH_MAP.put("october", 10);
        MONTH_MAP.put("nov", 11); MONTH_MAP.put("november", 11);
        MONTH_MAP.put("dec", 12); MONTH_MAP.put("december", 12);
        
        // 계절 매핑
        SEASON_MONTH_MAP.put("spring", "03");
        SEASON_MONTH_MAP.put("summer", "06");
        SEASON_MONTH_MAP.put("fall", "09");
        SEASON_MONTH_MAP.put("autumn", "09");
        SEASON_MONTH_MAP.put("winter", "12");
    }
    
    private DateParser() {}
    
    /**
     * MedlineDate 파싱 (예: "2024 Jan-Feb", "2024 Spring")
     */
    public static DateComponents parseMedlineDate(String medlineDate) {
        if (medlineDate == null || medlineDate.isBlank()) {
            return null;
        }
        
        var builder = DateComponents.builder()
                .medlineDate(medlineDate);
        
        // 연도 추출
        Pattern yearPattern = Pattern.compile("(\\d{4})");
        Matcher yearMatcher = yearPattern.matcher(medlineDate);
        if (yearMatcher.find()) {
            builder.year(Integer.parseInt(yearMatcher.group(1)));
        }
        
        // 월 범위 추출 (예: Jan-Feb)
        Pattern monthRangePattern = Pattern.compile("([A-Za-z]{3,})-([A-Za-z]{3,})");
        Matcher monthRangeMatcher = monthRangePattern.matcher(medlineDate);
        if (monthRangeMatcher.find()) {
            String startMonth = monthRangeMatcher.group(1).toLowerCase();
            Integer monthNum = MONTH_MAP.get(startMonth);
            if (monthNum != null) {
                builder.month(monthNum);
            }
        } else {
            // 단일 월 추출
            Pattern monthPattern = Pattern.compile("([A-Za-z]{3,})");
            Matcher monthMatcher = monthPattern.matcher(medlineDate);
            if (monthMatcher.find()) {
                String month = monthMatcher.group(1).toLowerCase();
                Integer monthNum = MONTH_MAP.get(month);
                if (monthNum != null) {
                    builder.month(monthNum);
                } else if (SEASON_MONTH_MAP.containsKey(month)) {
                    builder.season(month.substring(0, 1).toUpperCase() + month.substring(1));
                }
            }
        }
        
        return builder.build();
    }
    
    /**
     * ISO 8601 날짜 파싱
     */
    public static DateComponents parseIso8601Date(String iso8601Date) {
        if (iso8601Date == null || iso8601Date.isBlank()) {
            return null;
        }
        
        var builder = DateComponents.builder()
                .iso8601Date(iso8601Date);
        
        try {
            LocalDate date = LocalDate.parse(iso8601Date);
            builder.year(date.getYear())
                   .month(date.getMonthValue())
                   .day(date.getDayOfMonth());
        } catch (DateTimeParseException e) {
            // YYYY-MM 형식
            try {
                YearMonth ym = YearMonth.parse(iso8601Date);
                builder.year(ym.getYear())
                       .month(ym.getMonthValue());
            } catch (DateTimeParseException e2) {
                // YYYY 형식
                try {
                    Year y = Year.parse(iso8601Date);
                    builder.year(y.getValue());
                } catch (DateTimeParseException e3) {
                    // 파싱 실패
                }
            }
        }
        
        return builder.build();
    }
    
    /**
     * 월 문자열을 숫자로 변환
     */
    public static Integer parseMonth(String monthStr) {
        if (monthStr == null || monthStr.isBlank()) {
            return null;
        }
        
        // 숫자인 경우
        try {
            int month = Integer.parseInt(monthStr);
            if (month >= 1 && month <= 12) {
                return month;
            }
        } catch (NumberFormatException ignored) {}
        
        // 문자열인 경우
        return MONTH_MAP.get(monthStr.toLowerCase());
    }
}
```

---

# Part 5: GZip Handling

## 5.1 GzipUtils
```java
import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

/**
 * GZip 압축 파일 처리 유틸리티.
 */
public final class GzipUtils {
    
    private static final int BUFFER_SIZE = 65536;  // 64KB
    private static final byte[] GZIP_MAGIC = {0x1f, (byte) 0x8b};
    
    private GzipUtils() {}
    
    /**
     * GZip 압축 파일 여부 확인
     */
    public static boolean isGzipFile(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            byte[] header = new byte[2];
            if (is.read(header) == 2) {
                return header[0] == GZIP_MAGIC[0] && header[1] == GZIP_MAGIC[1];
            }
        }
        return false;
    }
    
    /**
     * InputStream 열기 (GZip 자동 감지)
     */
    public static InputStream openInputStream(Path path) throws IOException {
        InputStream is = Files.newInputStream(path);
        
        // 파일 확장자로 먼저 확인
        String fileName = path.toString().toLowerCase();
        if (fileName.endsWith(".gz") || fileName.endsWith(".gzip")) {
            is = new GZIPInputStream(is, BUFFER_SIZE);
        }
        
        return new BufferedInputStream(is, BUFFER_SIZE);
    }
    
    /**
     * GZip 파일 압축 해제
     */
    public static void decompress(Path source, Path target) throws IOException {
        try (InputStream is = new GZIPInputStream(Files.newInputStream(source), BUFFER_SIZE);
             OutputStream os = new BufferedOutputStream(Files.newOutputStream(target), BUFFER_SIZE)) {
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
        }
    }
    
    /**
     * 파일을 GZip으로 압축
     */
    public static void compress(Path source, Path target) throws IOException {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(source), BUFFER_SIZE);
             OutputStream os = new GZIPOutputStream(Files.newOutputStream(target), BUFFER_SIZE)) {
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
        }
    }
}
```

---

# Part 6: Streaming Parser Pattern

## 6.1 Stream Parser Interface
```java
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * 대용량 파일을 위한 스트리밍 파서 인터페이스.
 */
public interface StreamParser<T> {
    
    /**
     * 파일을 스트리밍 방식으로 파싱.
     * 각 아이템이 파싱될 때마다 handler가 호출됨.
     * 
     * @param path XML 파일 경로
     * @param handler 각 아이템 처리 핸들러
     * @return 처리된 아이템 수
     */
    long parseStream(Path path, Consumer<T> handler) throws Exception;
    
    /**
     * 파일을 스트리밍 방식으로 파싱 (배치 처리).
     * 
     * @param path XML 파일 경로
     * @param batchSize 배치 크기
     * @param handler 배치 처리 핸들러
     * @return 처리된 아이템 수
     */
    long parseStreamBatch(Path path, int batchSize, Consumer<java.util.List<T>> handler) 
            throws Exception;
}
```

## 6.2 Streaming Parser Example
```java
import javax.xml.stream.*;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

/**
 * PubMed 스트리밍 파서 예시.
 */
public class PubmedStreamParser extends XmlParserBase 
        implements StreamParser<PubmedArticle> {
    
    @Override
    public long parseStream(Path path, Consumer<PubmedArticle> handler) throws Exception {
        long count = 0;
        
        try (var is = openInputStream(path)) {
            XMLStreamReader reader = createReader(is);
            
            while (reader.hasNext()) {
                int event = reader.next();
                
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();
                    
                    if ("PubmedArticle".equals(localName)) {
                        PubmedArticle article = parsePubmedArticle(reader);
                        handler.accept(article);
                        count++;
                    } else if ("DeleteCitation".equals(localName)) {
                        // DeleteCitation 처리
                        handleDeleteCitation(reader);
                    }
                }
            }
        }
        
        return count;
    }
    
    @Override
    public long parseStreamBatch(Path path, int batchSize, 
            Consumer<List<PubmedArticle>> handler) throws Exception {
        long count = 0;
        List<PubmedArticle> batch = new ArrayList<>(batchSize);
        
        try (var is = openInputStream(path)) {
            XMLStreamReader reader = createReader(is);
            
            while (reader.hasNext()) {
                int event = reader.next();
                
                if (event == XMLStreamConstants.START_ELEMENT &&
                    "PubmedArticle".equals(reader.getLocalName())) {
                    
                    PubmedArticle article = parsePubmedArticle(reader);
                    batch.add(article);
                    count++;
                    
                    if (batch.size() >= batchSize) {
                        handler.accept(new ArrayList<>(batch));
                        batch.clear();
                    }
                }
            }
            
            // 남은 배치 처리
            if (!batch.isEmpty()) {
                handler.accept(batch);
            }
        }
        
        return count;
    }
    
    private PubmedArticle parsePubmedArticle(XMLStreamReader reader) 
            throws XMLStreamException {
        // 구현...
        return null;
    }
    
    private void handleDeleteCitation(XMLStreamReader reader) 
            throws XMLStreamException {
        // 구현...
    }
}
```

---

# Part 7: Testing Strategy

## 7.1 Test Data Sources
```java
/**
 * 테스트 데이터 소스:
 * 
 * PubMed:
 * - Baseline: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/
 * - Updates: https://ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/
 * - 샘플: pubmed24n0001.xml.gz (첫 번째 baseline 파일)
 * 
 * PMC:
 * - Open Access: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/
 * - 샘플: oa_comm/xml/
 */
```

## 7.2 Test Cases Template
```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ParserTestTemplate {
    
    // ==================== ATTRIBUTE TESTS ====================
    
    @Test
    void shouldParseAllRequiredAttributes() {
        // 모든 필수 속성이 파싱되는지 확인
    }
    
    @Test
    void shouldApplyDefaultAttributeValues() {
        // 기본값이 올바르게 적용되는지 확인
        // 예: PMID.Version = "1", AuthorList.CompleteYN = "Y"
    }
    
    @Test
    void shouldParseOptionalAttributes() {
        // 선택적 속성이 있을 때/없을 때 모두 처리
    }
    
    // ==================== ELEMENT TESTS ====================
    
    @Test
    void shouldParseRequiredElements() {
        // 필수 요소가 모두 파싱되는지 확인
    }
    
    @Test
    void shouldHandleOptionalElements() {
        // 선택적 요소 처리 확인
    }
    
    @Test
    void shouldHandleRepeatingElements() {
        // 반복 요소 (0-N, 1-N) 처리 확인
    }
    
    @Test
    void shouldHandleNestedElements() {
        // 중첩 요소 처리 확인 (예: ReferenceList 재귀)
    }
    
    // ==================== MIXED CONTENT TESTS ====================
    
    @Test
    void shouldParseMixedContent() {
        // 인라인 마크업 처리 확인
    }
    
    @Test
    void shouldPreserveMarkupInMixedContent() {
        // 마크업 보존 옵션 확인
    }
    
    @Test
    void shouldExtractPlainTextFromMixedContent() {
        // 플레인 텍스트 추출 확인
    }
    
    // ==================== SPECIAL CASES ====================
    
    @Test
    void shouldParseMedlineDate() {
        // 비정형 날짜 처리: "2024 Jan-Feb", "2024 Spring"
    }
    
    @Test
    void shouldParseCollectiveName() {
        // 단체 저자 처리
    }
    
    @Test
    void shouldParseDeleteCitation() {
        // 삭제된 PMID 목록 처리
    }
    
    // ==================== PERFORMANCE TESTS ====================
    
    @Test
    void shouldStreamLargeFile() {
        // 대용량 파일 스트리밍 처리 확인
    }
    
    @Test
    void shouldHandleGzipFile() {
        // GZip 압축 파일 처리 확인
    }
    
    // ==================== ERROR HANDLING ====================
    
    @Test
    void shouldHandleMalformedXml() {
        // 잘못된 XML 처리
    }
    
    @Test
    void shouldHandleMissingRequiredElements() {
        // 필수 요소 누락 처리
    }
}
```

---

# Part 8: Validation

## 8.1 ValidationUtils
```java
import java.util.*;

/**
 * 파싱 결과 검증 유틸리티.
 */
public final class ValidationUtils {
    
    private ValidationUtils() {}
    
    /**
     * PubMed 필수 필드 검증
     */
    public static List<String> validatePubmedArticle(PubmedArticle article) {
        List<String> errors = new ArrayList<>();
        
        if (article.getMedlineCitation() == null) {
            errors.add("MedlineCitation is required");
            return errors;
        }
        
        var citation = article.getMedlineCitation();
        
        // 필수 속성
        if (citation.getStatus() == null) {
            errors.add("MedlineCitation.Status is required");
        }
        
        // 필수 요소
        if (citation.getPmid() == null) {
            errors.add("PMID is required");
        }
        
        if (citation.getArticle() == null) {
            errors.add("Article is required");
        } else {
            errors.addAll(validateArticle(citation.getArticle()));
        }
        
        if (citation.getMedlineJournalInfo() == null) {
            errors.add("MedlineJournalInfo is required");
        }
        
        return errors;
    }
    
    private static List<String> validateArticle(Article article) {
        List<String> errors = new ArrayList<>();
        
        if (article.getPubModel() == null) {
            errors.add("Article.PubModel is required");
        }
        
        if (article.getJournal() == null) {
            errors.add("Journal is required");
        }
        
        if (article.getArticleTitle() == null) {
            errors.add("ArticleTitle is required");
        }
        
        if (article.getPublicationTypeList() == null || 
            article.getPublicationTypeList().isEmpty()) {
            errors.add("PublicationTypeList is required");
        }
        
        return errors;
    }
    
    /**
     * JATS 필수 필드 검증
     */
    public static List<String> validateJatsArticle(JatsArticle article) {
        List<String> errors = new ArrayList<>();
        
        if (article.getFront() == null) {
            errors.add("front is required");
            return errors;
        }
        
        var front = article.getFront();
        
        if (front.getArticleMeta() == null) {
            errors.add("article-meta is required");
        }
        
        return errors;
    }
}
```

---


---

# Part 9: Dependencies

## 9.1 Gradle build.gradle.kts
```kotlin
plugins {
    java
    id("io.freefair.lombok") version "8.6"
}

group = "io.brillianttiger.bio"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.9")
    
    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
```

## 9.2 settings.gradle.kts
```kotlin
rootProject.name = "pubmed-pmc-parser"
```

## 9.3 gradle.properties
```properties
org.gradle.jvmargs=-Xmx2g -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
```

## 9.4 File Integrity Verification (MD5 Checksum)

### PubMed: MD5 체크섬 검증 필수
PubMed FTP에서는 각 XML 파일에 대해 별도의 `.md5` 파일을 제공합니다.

```
pubmed25n0001.xml.gz      (19MB)
pubmed25n0001.xml.gz.md5  (60 bytes) - MD5 해시값만 포함
```

**MD5 파일 형식:**
```
MD5(pubmed25n0001.xml.gz)= d41d8cd98f00b204e9800998ecf8427e
```

**검증 유틸리티:**
```java
package io.brillianttiger.bio.common.util;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.regex.*;

/**
 * PubMed 파일 무결성 검증 유틸리티
 */
public final class Md5Verifier {
    
    private static final Pattern MD5_PATTERN = 
        Pattern.compile("([0-9a-fA-F]{32})");
    
    /**
     * 파일의 MD5 해시 계산
     */
    public static String calculateMd5(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = new BufferedInputStream(Files.newInputStream(file), 65536)) {
            byte[] buffer = new byte[65536];
            int read;
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        return bytesToHex(md.digest());
    }
    
    /**
     * .md5 파일에서 해시값 추출
     */
    public static String extractMd5FromFile(Path md5File) throws IOException {
        String content = Files.readString(md5File).trim();
        Matcher matcher = MD5_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).toLowerCase();
        }
        throw new IOException("Invalid MD5 file format: " + md5File);
    }
    
    /**
     * 파일 무결성 검증
     * @return true if MD5 matches
     */
    public static boolean verify(Path dataFile, Path md5File) 
            throws IOException, NoSuchAlgorithmException {
        String expected = extractMd5FromFile(md5File).toLowerCase();
        String actual = calculateMd5(dataFile).toLowerCase();
        return expected.equals(actual);
    }
    
    /**
     * PubMed 파일 검증 (자동으로 .md5 파일 찾기)
     */
    public static boolean verifyPubmedFile(Path xmlGzFile) 
            throws IOException, NoSuchAlgorithmException {
        Path md5File = Path.of(xmlGzFile.toString() + ".md5");
        if (!Files.exists(md5File)) {
            throw new FileNotFoundException("MD5 file not found: " + md5File);
        }
        return verify(xmlGzFile, md5File);
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
```

**배치 다운로드 및 검증:**
```java
/**
 * PubMed 파일 다운로드 및 검증
 */
public class PubmedDownloader {
    
    public void downloadAndVerify(String baseUrl, String fileName, Path outputDir) 
            throws Exception {
        Path dataFile = outputDir.resolve(fileName);
        Path md5File = outputDir.resolve(fileName + ".md5");
        
        // 1. MD5 파일 먼저 다운로드
        download(baseUrl + "/" + fileName + ".md5", md5File);
        
        // 2. 데이터 파일 다운로드
        download(baseUrl + "/" + fileName, dataFile);
        
        // 3. 검증
        if (!Md5Verifier.verify(dataFile, md5File)) {
            Files.deleteIfExists(dataFile);
            throw new IOException("MD5 verification failed for: " + fileName);
        }
        
        log.info("Verified: {}", fileName);
    }
}
```

### PMC: 체크섬 미제공
PMC FTP는 별도의 체크섬 파일을 제공하지 않습니다.

| 서비스 | 체크섬 | 비고 |
|--------|--------|------|
| PMC FTP | ❌ 없음 | filelist.csv에 체크섬 컬럼 없음 |
| PMC AWS S3 | ⚠️ ETag | "may or may not be MD5" - 보장 안됨 |

**PMC 대안: 파일 크기 검증**
```java
/**
 * PMC filelist.csv 기반 파일 크기 검증 (체크섬 대안)
 */
public class PmcFileValidator {
    
    /**
     * tar.gz 압축 해제 후 XML 파일 존재 여부 확인
     */
    public boolean validatePmcPackage(Path tarGzFile) {
        try (TarArchiveInputStream tar = new TarArchiveInputStream(
                new GZIPInputStream(Files.newInputStream(tarGzFile)))) {
            TarArchiveEntry entry;
            boolean hasXml = false;
            while ((entry = tar.getNextTarEntry()) != null) {
                if (entry.getName().endsWith(".nxml") || 
                    entry.getName().endsWith(".xml")) {
                    hasXml = true;
                }
            }
            return hasXml;
        } catch (Exception e) {
            return false; // 손상된 파일
        }
    }
}
```

---

# Part 10: Quick Start

```bash
# 1. 프로젝트 생성
mkdir pubmed-pmc-parser
cd pubmed-pmc-parser

# 2. Gradle 초기화
gradle init --type java-library --dsl kotlin

# 3. 디렉토리 구조 생성
mkdir -p src/main/java/com/brillianttiger/bio/{common,pubmed,pmc}/{model,parser,util}
mkdir -p src/test/java/com/brillianttiger/bio/{pubmed,pmc}/parser
mkdir -p src/test/resources/{pubmed,pmc}

# 4. 샘플 데이터 다운로드
# PubMed
wget https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed24n0001.xml.gz \
     -O src/test/resources/pubmed/sample.xml.gz

# PMC (OA subset에서 샘플 선택)
wget "https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_comm/xml/oa_comm_xml.incr.2024-01-01.tar.gz"

# 5. 구현 순서
# Step 1: Common 모델 클래스 (TextContent, DateComponents, PersonName, Identifier)
# Step 2: 파서 베이스 클래스 (XmlParserBase, StaxParserUtils)
# Step 3: PubMed 모델 클래스 (SKILL-PUBMED.md 체크리스트 기준)
# Step 4: PubMed 파서 구현
# Step 5: PMC 모델 클래스 (SKILL-PMC.md 체크리스트 기준)
# Step 6: PMC 파서 구현
# Step 7: 테스트 작성

# 6. 빌드 & 테스트
./gradlew clean test

# 7. JAR 빌드
./gradlew build
```

---

# Reference Links

## Official Documentation
- **PubMed DTD**: https://dtd.nlm.nih.gov/ncbi/pubmed/doc/out/240101/index.html
- **JATS Tag Library**: https://jats.nlm.nih.gov/archiving/tag-library/1.4/
- **PMC Tagging Guidelines**: https://www.ncbi.nlm.nih.gov/pmc/pmcdoc/tagging-guidelines/

## Data Sources
- **PubMed Baseline**: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/
- **PubMed Updates**: https://ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/
- **PMC Open Access**: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/

## Tools
- **E-Utilities**: https://www.ncbi.nlm.nih.gov/books/NBK25501/
