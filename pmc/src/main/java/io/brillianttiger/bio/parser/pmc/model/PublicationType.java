package io.brillianttiger.bio.parser.pmc.model;

import lombok.Getter;

/**
 * PublicationType / 출판물 유형
 *
 * KR: 인용 참조의 출판물 유형을 나타내는 열거형. JATS 1.4 DTD 완전 준수.
 *     저널, 서적, 학회 논문, 학위논문, 특허 등 다양한 출판물 유형을 포함.
 * EN: Enumeration representing publication type for citations. Fully compliant with JATS 1.4 DTD.
 *     Includes various publication types such as journal, book, conference proceedings, thesis, patent, etc.
 *
 * DTD: <!ATTLIST element-citation
 *          publication-type (book | confproc | gov | journal | other | patent |
 *                            report | standard | thesis | webpage | software |
 *                            data | database | preprint | working-paper) #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/publication-type.html
 *
 * Example:
 * <element-citation publication-type="journal">
 *   ...
 * </element-citation>
 */
@Getter
public enum PublicationType {

    /**
     * 저널/학술지
     *
     * KR: 학술지 논문
     * EN: Journal article
     */
    JOURNAL("journal"),

    /**
     * 서적
     *
     * KR: 서적
     * EN: Book
     */
    BOOK("book"),

    /**
     * 학회 논문
     *
     * KR: 학회 발표 논문
     * EN: Conference proceedings
     */
    CONFPROC("confproc"),

    /**
     * 학위논문
     *
     * KR: 석사/박사 학위논문
     * EN: Thesis or dissertation
     */
    THESIS("thesis"),

    /**
     * 특허
     *
     * KR: 특허
     * EN: Patent
     */
    PATENT("patent"),

    /**
     * 소프트웨어
     *
     * KR: 소프트웨어
     * EN: Software
     */
    SOFTWARE("software"),

    /**
     * 데이터
     *
     * KR: 데이터셋
     * EN: Data or dataset
     */
    DATA("data"),

    /**
     * 데이터베이스
     *
     * KR: 데이터베이스
     * EN: Database
     */
    DATABASE("database"),

    /**
     * 프리프린트
     *
     * KR: 프리프린트 (출판 전 원고)
     * EN: Preprint (manuscript before publication)
     */
    PREPRINT("preprint"),

    /**
     * 웹페이지
     *
     * KR: 웹페이지
     * EN: Webpage
     */
    WEBPAGE("webpage"),

    /**
     * 보고서
     *
     * KR: 보고서 (기술 보고서 등)
     * EN: Report (technical report, etc.)
     */
    REPORT("report"),

    /**
     * 정부 문서
     *
     * KR: 정부 문서
     * EN: Government document
     */
    GOV("gov"),

    /**
     * 표준
     *
     * KR: 표준 문서
     * EN: Standard document
     */
    STANDARD("standard"),

    /**
     * 워킹 페이퍼
     *
     * KR: 워킹 페이퍼
     * EN: Working paper
     */
    WORKING_PAPER("working-paper"),

    /**
     * 레터
     *
     * KR: 레터/서신
     * EN: Letter
     */
    LETTER("letter"),

    /**
     * 편집자에게 보내는 편지
     *
     * KR: 편집자에게 보내는 편지
     * EN: Letter to editor
     */
    LETTER_TO_EDITOR("letter-to-editor"),

    /**
     * 뉴스
     *
     * KR: 뉴스 기사
     * EN: News article
     */
    NEWS("news"),

    /**
     * 커뮤니케이션
     *
     * KR: 커뮤니케이션
     * EN: Communication
     */
    COMMUN("commun"),

    /**
     * 리뷰
     *
     * KR: 리뷰 논문
     * EN: Review article
     */
    REVIEW("review"),

    /**
     * 기타
     *
     * KR: 기타 또는 알 수 없는 유형
     * EN: Other or unknown type
     */
    OTHER("other");

    private final String value;

    PublicationType(String value) {
        this.value = value;
    }

    /**
     * 문자열 값으로부터 PublicationType을 찾아 반환 / Find and return PublicationType from string value
     *
     * @param value 문자열 값 / String value
     * @return 매칭되는 PublicationType, 없으면 OTHER / Matching PublicationType, or OTHER if not found
     */
    public static PublicationType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (PublicationType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }

    @Override
    public String toString() {
        return value;
    }
}
