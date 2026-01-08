package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CommentsCorrectionsList / 코멘트 및 정정 목록
 *
 * DTD: <!ELEMENT CommentsCorrectionsList (CommentsCorrections+)>
 *
 * KR: 코멘트, 정정, 철회 등 관련 정보 목록
 * EN: Comment, correction, retraction information list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsCorrectionsList {

    /**
     * 코멘트 및 정정 목록 / Comments and corrections list
     */
    private List<CommentsCorrections> commentsCorrections;
}
