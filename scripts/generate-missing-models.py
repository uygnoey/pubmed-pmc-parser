#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
누락된 JATS 모델 클래스 자동 생성 스크립트
Automatically generate missing JATS model classes
"""

import re
from pathlib import Path
from typing import Dict, List

# Java 모델 클래스 템플릿
MODEL_TEMPLATE = '''package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS {jats_element} 요소 / JATS {jats_element} element
 *
 * <p>DTD: {{@code <!ELEMENT {jats_element} {content_model}>}}</p>
 *
 * <p>
 * KR: {description_kr}<br>
 * EN: {description_en}
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/{element_url}.html">
 *      JATS {jats_element} Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class {class_name} {{

    /**
     * 텍스트 내용 / Text content
     */
    private String value;

    /**
     * 공통 속성: id
     */
    private String id;

    /**
     * 공통 속성: xml:lang
     */
    private String xmlLang;
{additional_fields}
}}
'''

# Element별 특수 정보 매핑
ELEMENT_INFO: Dict[str, Dict] = {
    'Article': {
        'description_kr': '저널 기사의 루트 요소',
        'description_en': 'Root element for a journal article',
        'content_model': '(processing-meta?, front, body?, back?, floats-group?, (sub-article* | response*))',
        'fields': '''
    /**
     * Article type (예: research-article, review-article)
     */
    private String articleType;

    /**
     * DTD version
     */
    private String dtdVersion;

    /**
     * Front matter (메타데이터)
     */
    private Front front;

    /**
     * Body (본문)
     */
    private Body body;

    /**
     * Back matter (부록)
     */
    private Back back;
'''
    },
    'ProcessingMeta': {
        'description_kr': '처리 메타데이터',
        'description_en': 'Processing metadata',
        'content_model': '(EMPTY)',
        'fields': ''
    },
    'ArticleVersion': {
        'description_kr': '기사 버전 정보',
        'description_en': 'Article version information',
        'content_model': '(#PCDATA)',
        'fields': '''
    /**
     * 버전 지정자 (예: preprint, vor)
     */
    private String designator;
'''
    },
    'ArticleVersionAlternatives': {
        'description_kr': '여러 기사 버전의 대안',
        'description_en': 'Alternative article versions',
        'content_model': '(article-version)+',
        'fields': '''
    /**
     * 기사 버전 목록 / Article versions
     */
    @Builder.Default
    private List<ArticleVersion> articleVersions = new ArrayList<>();
'''
    },
    'License': {
        'description_kr': '라이선스 정보',
        'description_en': 'License information',
        'content_model': '(ali:license_ref | license-p | graphic)*',
        'fields': '''
    /**
     * 라이선스 타입
     */
    private String licenseType;

    /**
     * xlink:href
     */
    private String xlinkHref;

    /**
     * 라이선스 단락 / License paragraphs
     */
    @Builder.Default
    private List<LicenseP> licenseParagraphs = new ArrayList<>();
'''
    },
    'LicenseP': {
        'description_kr': '라이선스 단락',
        'description_en': 'License paragraph',
        'content_model': '(#PCDATA | %inline-elements;)*',
        'fields': ''
    },
    'OpenAccess': {
        'description_kr': '오픈 액세스 정보',
        'description_en': 'Open access information',
        'content_model': '(#PCDATA)',
        'fields': ''
    },
    'DispFormula': {
        'description_kr': '표시 수식',
        'description_en': 'Display formula',
        'content_model': '(label?, (tex-math | mml:math | graphic)+, alternatives?)',
        'fields': '''
    /**
     * 라벨
     */
    private String label;

    /**
     * TeX 수식
     */
    private String texMath;

    /**
     * MathML
     */
    private String mmlMath;
'''
    },
    'DispFormulaGroup': {
        'description_kr': '표시 수식 그룹',
        'description_en': 'Display formula group',
        'content_model': '(label?, (disp-formula)+)',
        'fields': '''
    /**
     * 라벨
     */
    private String label;

    /**
     * 수식 목록 / Formulas
     */
    @Builder.Default
    private List<DispFormula> dispFormulas = new ArrayList<>();
'''
    },
    'InlineFormula': {
        'description_kr': '인라인 수식',
        'description_en': 'Inline formula',
        'content_model': '(tex-math | mml:math | graphic)+',
        'fields': '''
    /**
     * TeX 수식
     */
    private String texMath;

    /**
     * MathML
     */
    private String mmlMath;
'''
    },
    'MmlMath': {
        'description_kr': 'MathML 수학 표현',
        'description_en': 'MathML mathematical expression',
        'content_model': 'ANY (MathML content)',
        'fields': '''
    /**
     * MathML 내용 (XML 문자열)
     */
    private String mathmlContent;
'''
    },
    'Question': {
        'description_kr': '질문',
        'description_en': 'Question',
        'content_model': '(label?, title?, (%para-level;)*)',
        'fields': '''
    /**
     * 라벨
     */
    private Label label;

    /**
     * 제목
     */
    private Title title;

    /**
     * 질문 내용 / Question content
     */
    @Builder.Default
    private List<P> paragraphs = new ArrayList<>();
'''
    },
    'Answer': {
        'description_kr': '답변',
        'description_en': 'Answer',
        'content_model': '(label?, title?, (%para-level;)*)',
        'fields': '''
    /**
     * 라벨
     */
    private Label label;

    /**
     * 제목
     */
    private Title title;

    /**
     * 답변 내용 / Answer content
     */
    @Builder.Default
    private List<P> paragraphs = new ArrayList<>();
'''
    },
    'List': {
        'description_kr': '목록',
        'description_en': 'List',
        'content_model': '(label?, title?, list-item+)',
        'fields': '''
    /**
     * 목록 타입 (예: bullet, order, simple)
     */
    private String listType;

    /**
     * 라벨
     */
    private Label label;

    /**
     * 제목
     */
    private Title title;

    /**
     * 목록 항목들 / List items
     */
    @Builder.Default
    private List<ListItem> listItems = new ArrayList<>();
'''
    }
}

# 간단한 텍스트 요소들 (value만 가지는 요소)
SIMPLE_TEXT_ELEMENTS = {
    'Abbrev', 'AccessDate', 'AddrLine', 'City', 'ConfAcronym', 'ConfNum', 'ConfTheme',
    'Country', 'Fax', 'Gov', 'Phone', 'PostalCode', 'Price', 'State', 'X',
    'ContentLanguage', 'DefHead', 'TermHead', 'Explanation',
    'EventDesc', 'AwardDesc', 'AwardName', 'FixedCase',
    'Isbn', 'IssnL', 'IssueSubtitle', 'Speaker', 'Std', 'StdOrganization',
    'StringConf', 'SupportDescription', 'NamedContent', 'StyledContent',
    'See', 'SeeAlso', 'Statement', 'TimeStamp'
}

# ID가 필요한 요소들
ELEMENTS_WITH_ID = {
    'DispFormula', 'DispFormulaGroup', 'InlineFormula', 'Question', 'Answer',
    'List', 'DefHead', 'IndexTerm', 'Target', 'Ruby', 'Sig'
}

def pascal_to_kebab(pascal_str: str) -> str:
    """PascalCase를 kebab-case로 변환"""
    # 특수 케이스
    if pascal_str.startswith('Ali'):
        return 'ali:' + re.sub(r'(?<!^)(?=[A-Z])', '-', pascal_str[3:]).lower()
    if pascal_str.startswith('Mml'):
        return 'mml:' + re.sub(r'(?<!^)(?=[A-Z])', '-', pascal_str[3:]).lower()

    return re.sub(r'(?<!^)(?=[A-Z])', '-', pascal_str).lower()

def generate_simple_model(class_name: str, jats_element: str) -> str:
    """간단한 텍스트 모델 생성"""

    description_kr = f'{jats_element} 요소'
    description_en = f'{jats_element} element'
    content_model = '(#PCDATA)'
    additional_fields = ''

    return MODEL_TEMPLATE.format(
        class_name=class_name,
        jats_element=jats_element,
        element_url=jats_element,
        description_kr=description_kr,
        description_en=description_en,
        content_model=content_model,
        additional_fields=additional_fields
    )

def generate_model_class(class_name: str) -> str:
    """모델 클래스 생성"""

    jats_element = pascal_to_kebab(class_name)
    element_url = jats_element.replace(':', '-')

    # 특수 정보가 있는 경우
    if class_name in ELEMENT_INFO:
        info = ELEMENT_INFO[class_name]
        return MODEL_TEMPLATE.format(
            class_name=class_name,
            jats_element=jats_element,
            element_url=element_url,
            description_kr=info.get('description_kr', f'{jats_element} 요소'),
            description_en=info.get('description_en', f'{jats_element} element'),
            content_model=info.get('content_model', 'MIXED'),
            additional_fields=info.get('fields', '')
        )

    # 간단한 텍스트 요소
    if class_name in SIMPLE_TEXT_ELEMENTS:
        return generate_simple_model(class_name, jats_element)

    # 기본 모델
    description_kr = f'{jats_element} 요소'
    description_en = f'{jats_element} element'

    return MODEL_TEMPLATE.format(
        class_name=class_name,
        jats_element=jats_element,
        element_url=element_url,
        description_kr=description_kr,
        description_en=description_en,
        content_model='MIXED',
        additional_fields=''
    )

def main():
    base_dir = Path("/Users/yeongyu.yang/IdeaProjects/pubmed-pmc-parser")
    missing_file = base_dir / "claudedocs/jats-analysis/missing-models-cleaned.txt"
    output_dir = base_dir / "src/main/java/com/brillianttiger/bio/parser/pmc/model"

    print("=== 누락된 JATS 모델 클래스 생성 ===\n")

    # 누락된 모델 목록 로드
    with open(missing_file, 'r') as f:
        missing_models = [line.strip() for line in f if line.strip()]

    print(f"생성할 모델 개수: {len(missing_models)}\n")

    created_count = 0
    skipped_count = 0

    for class_name in missing_models:
        output_file = output_dir / f"{class_name}.java"

        # 이미 존재하는 경우 스킵
        if output_file.exists():
            print(f"⏭️  SKIP: {class_name} (이미 존재)")
            skipped_count += 1
            continue

        # 모델 클래스 생성
        try:
            class_content = generate_model_class(class_name)

            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(class_content)

            print(f"✅ CREATE: {class_name}")
            created_count += 1

        except Exception as e:
            print(f"❌ ERROR: {class_name} - {e}")

    print(f"\n=== 생성 완료 ===")
    print(f"생성됨: {created_count}개")
    print(f"스킵됨: {skipped_count}개")
    print(f"총계: {created_count + skipped_count}/{len(missing_models)}개")

if __name__ == "__main__":
    main()
