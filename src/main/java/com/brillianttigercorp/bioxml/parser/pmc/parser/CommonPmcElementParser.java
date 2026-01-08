package com.brillianttigercorp.bioxml.parser.pmc.parser;

import com.brillianttigercorp.bioxml.parser.pmc.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CommonPmcElementParser / PMC 공통 요소 파서
 *
 * KR: PMC XML에서 공통으로 사용되는 요소들을 파싱하는 유틸리티 클래스
 * EN: Utility class for parsing common elements used in PMC XML
 */
public class CommonPmcElementParser {

    /**
     * 텍스트 콘텐츠 추출 / Extract text content
     *
     * KR: 요소의 텍스트 내용을 추출
     * EN: Extract text content from element
     */
    public static String parseTextContent(XMLStreamReader reader, String elementName) throws XMLStreamException {
        StringBuilder content = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.CHARACTERS) {
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
     * 요소 건너뛰기 / Skip element
     *
     * KR: 처리하지 않는 요소를 건너뜀
     * EN: Skip unprocessed element
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
     * Label 파싱 / Parse Label
     * DTD: <!ELEMENT label (#PCDATA | %label-elements;)*>
     */
    public static Label parseLabel(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "label");
        return Label.builder().value(value).build();
    }

    /**
     * Title 파싱 / Parse Title
     * DTD: <!ELEMENT title (#PCDATA | %title-elements;)*>
     */
    public static Title parseTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "title");
        return Title.builder().value(value).build();
    }

    /**
     * P (Paragraph) 파싱 / Parse P (Paragraph)
     * DTD: <!ELEMENT p (#PCDATA | %p-elements;)*>
     */
    public static P parseP(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "p");
        return P.builder().value(value).build();
    }

    /**
     * Year 파싱 / Parse Year
     */
    public static Year parseYear(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "year");
        return Year.builder().value(value).build();
    }

    /**
     * Month 파싱 / Parse Month
     */
    public static Month parseMonth(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "month");
        return Month.builder().value(value).build();
    }

    /**
     * Day 파싱 / Parse Day
     */
    public static Day parseDay(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "day");
        return Day.builder().value(value).build();
    }

    /**
     * Volume 파싱 / Parse Volume
     */
    public static Volume parseVolume(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "volume");
        return Volume.builder().value(value).build();
    }

    /**
     * VolumeId 파싱 / Parse VolumeId
     */
    public static VolumeId parseVolumeId(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "volume-id");
        return VolumeId.builder().value(value).build();
    }

    /**
     * VolumeSeries 파싱 / Parse VolumeSeries
     */
    public static VolumeSeries parseVolumeSeries(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "volume-series");
        return VolumeSeries.builder().value(value).build();
    }

    /**
     * PmcIssue 파싱 / Parse PmcIssue
     */
    public static PmcIssue parsePmcIssue(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "issue");
        return PmcIssue.builder().value(value).build();
    }

    /**
     * IssueId 파싱 / Parse IssueId
     */
    public static IssueId parseIssueId(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "issue-id");
        return IssueId.builder().value(value).build();
    }

    /**
     * IssueTitle 파싱 / Parse IssueTitle
     */
    public static IssueTitle parseIssueTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "issue-title");
        return IssueTitle.builder().value(value).build();
    }

    /**
     * IssueSponsor 파싱 / Parse IssueSponsor
     */
    public static IssueSponsor parseIssueSponsor(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "issue-sponsor");
        return IssueSponsor.builder().value(value).build();
    }

    /**
     * IssuePart 파싱 / Parse IssuePart
     */
    public static IssuePart parseIssuePart(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "issue-part");
        return IssuePart.builder().value(value).build();
    }

    /**
     * Fpage 파싱 / Parse Fpage
     */
    public static Fpage parseFpage(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "fpage");
        return Fpage.builder().value(value).build();
    }

    /**
     * Lpage 파싱 / Parse Lpage
     */
    public static Lpage parseLpage(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "lpage");
        return Lpage.builder().value(value).build();
    }

    /**
     * PageRange 파싱 / Parse PageRange
     */
    public static PageRange parsePageRange(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "page-range");
        return PageRange.builder().value(value).build();
    }

    /**
     * ElocationId 파싱 / Parse ElocationId
     */
    public static ElocationId parseElocationId(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "elocation-id");
        return ElocationId.builder().value(value).build();
    }

    /**
     * Email 파싱 / Parse Email
     */
    public static Email parseEmail(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "email");
        return Email.builder().value(value).build();
    }

    /**
     * ExtLink 파싱 / Parse ExtLink
     * DTD: <!ATTLIST ext-link xlink:href CDATA #IMPLIED>
     */
    public static ExtLink parseExtLink(XMLStreamReader reader) throws XMLStreamException {
        String href = reader.getAttributeValue(null, "xlink:href");
        if (href == null) {
            href = reader.getAttributeValue("http://www.w3.org/1999/xlink", "href");
        }
        String value = parseTextContent(reader, "ext-link");

        return ExtLink.builder()
                .xlinkHref(href)
                .value(value)
                .build();
    }

    /**
     * Uri 파싱 / Parse Uri
     */
    public static Uri parseUri(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "uri");
        return Uri.builder().value(value).build();
    }

    /**
     * SelfUri 파싱 / Parse SelfUri
     */
    public static SelfUri parseSelfUri(XMLStreamReader reader) throws XMLStreamException {
        String href = reader.getAttributeValue(null, "xlink:href");
        if (href == null) {
            href = reader.getAttributeValue("http://www.w3.org/1999/xlink", "href");
        }
        String value = parseTextContent(reader, "self-uri");

        return SelfUri.builder()
                .xlinkHref(href)
                .value(value)
                .build();
    }

    /**
     * PublisherName 파싱 / Parse PublisherName
     */
    public static PublisherName parsePublisherName(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "publisher-name");
        return PublisherName.builder().value(value).build();
    }

    /**
     * PublisherLoc 파싱 / Parse PublisherLoc
     */
    public static PublisherLoc parsePublisherLoc(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "publisher-loc");
        return PublisherLoc.builder().value(value).build();
    }

    /**
     * Edition 파싱 / Parse Edition
     */
    public static Edition parseEdition(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "edition");
        return Edition.builder().value(value).build();
    }

    /**
     * Source 파싱 / Parse Source
     */
    public static Source parseSource(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "source");
        return Source.builder().value(value).build();
    }

    /**
     * Comment 파싱 / Parse Comment
     */
    public static Comment parseComment(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "comment");
        return Comment.builder().value(value).build();
    }

    /**
     * ChapterTitle 파싱 / Parse ChapterTitle
     */
    public static ChapterTitle parseChapterTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "chapter-title");
        return ChapterTitle.builder().value(value).build();
    }

    /**
     * ConfName 파싱 / Parse ConfName
     */
    public static ConfName parseConfName(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "conf-name");
        return ConfName.builder().value(value).build();
    }

    /**
     * ConfLoc 파싱 / Parse ConfLoc
     */
    public static ConfLoc parseConfLoc(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "conf-loc");
        return ConfLoc.builder().value(value).build();
    }

    /**
     * Supplement 파싱 / Parse Supplement
     */
    public static Supplement parseSupplement(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "supplement");
        return Supplement.builder().value(value).build();
    }

    /**
     * PmcIsbn 파싱 / Parse PmcIsbn
     */
    public static PmcIsbn parsePmcIsbn(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "isbn");
        return PmcIsbn.builder().value(value).build();
    }

    /**
     * PmcArticleId 파싱 / Parse PmcArticleId
     * DTD: <!ATTLIST article-id pub-id-type CDATA #IMPLIED>
     */
    public static PmcArticleId parsePmcArticleId(XMLStreamReader reader, String elementName) throws XMLStreamException {
        String pubIdType = reader.getAttributeValue(null, "pub-id-type");
        String value = parseTextContent(reader, elementName);

        return PmcArticleId.builder()
                .pubIdType(pubIdType)
                .value(value)
                .build();
    }

    /**
     * Xref 파싱 / Parse Xref
     * DTD: <!ATTLIST xref ref-type CDATA #IMPLIED rid IDREFS #IMPLIED>
     */
    public static Xref parseXref(XMLStreamReader reader) throws XMLStreamException {
        String refType = reader.getAttributeValue(null, "ref-type");
        String rid = reader.getAttributeValue(null, "rid");
        String value = parseTextContent(reader, "xref");

        return Xref.builder()
                .refType(refType)
                .rid(rid)
                .value(value)
                .build();
    }

    /**
     * CopyrightStatement 파싱 / Parse CopyrightStatement
     */
    public static CopyrightStatement parseCopyrightStatement(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "copyright-statement");
        return CopyrightStatement.builder().value(value).build();
    }

    /**
     * CopyrightYear 파싱 / Parse CopyrightYear
     */
    public static CopyrightYear parseCopyrightYear(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "copyright-year");
        return CopyrightYear.builder().value(value).build();
    }

    /**
     * CopyrightHolder 파싱 / Parse CopyrightHolder
     */
    public static CopyrightHolder parseCopyrightHolder(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "copyright-holder");
        return CopyrightHolder.builder().value(value).build();
    }

    /**
     * License 파싱 / Parse License
     * DTD: <!ATTLIST license license-type CDATA #IMPLIED xlink:href CDATA #IMPLIED>
     */
    public static License parseLicense(XMLStreamReader reader) throws XMLStreamException {
        String licenseType = reader.getAttributeValue(null, "license-type");
        String xlinkHref = reader.getAttributeValue(null, "href");
        if (xlinkHref == null) {
            xlinkHref = reader.getAttributeValue("http://www.w3.org/1999/xlink", "href");
        }

        // Skip license element content (we don't parse license-p here)
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals("license")) {
                break;
            }
        }

        return License.builder()
                .licenseType(licenseType)
                .xlinkHref(xlinkHref)
                .build();
    }

    /**
     * PmcSuffix 파싱 / Parse PmcSuffix
     */
    public static PmcSuffix parsePmcSuffix(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "suffix");
        return PmcSuffix.builder().value(value).build();
    }
}
