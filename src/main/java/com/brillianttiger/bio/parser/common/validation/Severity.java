package com.brillianttiger.bio.parser.common.validation;

/**
 * Severity / 검증 오류 심각도
 *
 * KR: 검증 오류의 심각도 수준
 * EN: Severity level of validation errors
 */
public enum Severity {
    /**
     * 오류 / Error
     *
     * KR: 치명적 오류, 반드시 수정되어야 함
     * EN: Critical error, must be fixed
     */
    ERROR,

    /**
     * 경고 / Warning
     *
     * KR: 잠재적 문제, 확인 권장
     * EN: Potential issue, review recommended
     */
    WARNING,

    /**
     * 정보 / Information
     *
     * KR: 참고 정보, 선택적 개선 사항
     * EN: Informational, optional improvement
     */
    INFO
}
