package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SpaceFlightMission / 우주비행 미션
 *
 * DTD: <!ELEMENT SpaceFlightMission (#PCDATA)>
 *
 * KR: NASA 우주비행 미션 관련 정보
 * EN: NASA space flight mission information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceFlightMission {

    /**
     * 미션명 / Mission name
     */
    private String value;
}
