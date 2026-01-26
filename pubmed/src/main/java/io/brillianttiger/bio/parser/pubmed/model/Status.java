package io.brillianttiger.bio.parser.pubmed.model;

/**
 * Status / MedlineCitation 상태
 *
 * DTD: Status (Completed | In-Process | PubMed-not-MEDLINE |
 *              In-Data-Review | Publisher | MEDLINE | OLDMEDLINE) #REQUIRED
 *
 * KR: MEDLINE 인용의 처리 상태
 * EN: Processing status of MEDLINE citation
 */
public enum Status {
    /**
     * 완료 / Completed
     *
     * KR: MEDLINE 색인 작업 완료
     * EN: MEDLINE indexing completed
     */
    Completed("Completed"),

    /**
     * 처리 중 / In-Process
     *
     * KR: MEDLINE 색인 작업 진행 중
     * EN: MEDLINE indexing in progress
     */
    In_Process("In-Process"),

    /**
     * PubMed이지만 MEDLINE 아님 / PubMed but not MEDLINE
     *
     * KR: PubMed에는 있지만 MEDLINE 기준에 미달
     * EN: In PubMed but does not meet MEDLINE criteria
     */
    PubMed_not_MEDLINE("PubMed-not-MEDLINE"),

    /**
     * 데이터 검토 중 / In-Data-Review
     *
     * KR: 데이터 품질 검토 중
     * EN: Under data quality review
     */
    In_Data_Review("In-Data-Review"),

    /**
     * 출판사 제공 / Publisher
     *
     * KR: 출판사가 직접 제공한 데이터
     * EN: Data provided directly by publisher
     */
    Publisher("Publisher"),

    /**
     * MEDLINE / MEDLINE
     *
     * KR: MEDLINE 데이터베이스 포함
     * EN: Included in MEDLINE database
     */
    MEDLINE("MEDLINE"),

    /**
     * 구 MEDLINE / OLDMEDLINE
     *
     * KR: 1966년 이전 MEDLINE 데이터
     * EN: MEDLINE data from before 1966
     */
    OLDMEDLINE("OLDMEDLINE");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 Status enum 변환 / Convert from string to Status enum
     *
     * @param value 문자열 값 / String value
     * @return Status enum
     */
    public static Status fromValue(String value) {
        for (Status status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Status value: " + value);
    }
}
