#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
JATS Elements와 현재 Java 모델 비교 스크립트
Compare JATS elements with current Java models
"""

import re
from pathlib import Path
from typing import Set, List, Tuple

def kebab_to_pascal(kebab_str: str) -> str:
    """
    kebab-case를 PascalCase로 변환
    예: article-meta -> ArticleMeta
    """
    # 특수 케이스 처리
    if kebab_str.startswith('ali:'):
        # ali:free_to_read -> AliFreeToRead
        kebab_str = 'ali-' + kebab_str[4:].replace('_', '-')
    elif kebab_str.startswith('mml:'):
        # mml:math -> MmlMath
        kebab_str = 'mml-' + kebab_str[4:]
    
    # kebab-case를 PascalCase로 변환
    parts = kebab_str.split('-')
    return ''.join(word.capitalize() for word in parts)

def load_jats_elements(file_path: Path) -> Set[str]:
    """JATS element 목록을 로드하고 PascalCase로 변환"""
    elements = set()
    with open(file_path, 'r', encoding='utf-8') as f:
        for line in f:
            element = line.strip()
            if element:
                java_class_name = kebab_to_pascal(element)
                elements.add(java_class_name)
    return elements

def load_current_models(base_dir: Path) -> Set[str]:
    """현재 구현된 Java 모델 클래스 목록 로드"""
    model_dir = base_dir / "src/main/java/com/brillianttiger/bio/parser/pmc/model"
    models = set()
    
    if model_dir.exists():
        for java_file in model_dir.glob("*.java"):
            # 파일명에서 .java 제거
            class_name = java_file.stem
            models.add(class_name)
    
    return models

def categorize_elements(jats_elements: Set[str], current_models: Set[str]) -> Tuple[List[str], List[str], List[str]]:
    """
    Element를 카테고리별로 분류
    Returns: (missing, extra, common)
    """
    missing = sorted(list(jats_elements - current_models))
    extra = sorted(list(current_models - jats_elements))
    common = sorted(list(jats_elements & current_models))
    
    return missing, extra, common

def generate_report(output_file: Path, missing: List[str], extra: List[str], common: List[str],
                   jats_total: int, model_total: int):
    """비교 리포트 생성"""
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("# JATS 1.4 vs 현재 PMC 모델 비교 리포트\n\n")
        f.write("## 📊 요약 / Summary\n\n")
        f.write(f"- **JATS 1.4 총 Element 개수**: {jats_total}\n")
        f.write(f"- **현재 구현된 모델 개수**: {model_total}\n")
        f.write(f"- **일치하는 모델**: {len(common)} ({len(common)/jats_total*100:.1f}%)\n")
        f.write(f"- **누락된 모델**: {len(missing)} ({len(missing)/jats_total*100:.1f}%)\n")
        f.write(f"- **DTD에 없는 추가 모델**: {len(extra)}\n\n")
        
        # 누락된 모델
        f.write("## ❌ 누락된 모델 (JATS에는 있지만 구현 안됨)\n\n")
        f.write(f"총 {len(missing)}개의 모델이 누락되었습니다.\n\n")
        
        if missing:
            # 알파벳별로 그룹화
            current_letter = ''
            for class_name in missing:
                first_letter = class_name[0].upper()
                if first_letter != current_letter:
                    current_letter = first_letter
                    f.write(f"\n### {current_letter}\n\n")
                
                # kebab-case 원본 이름도 표시
                kebab_name = re.sub(r'(?<!^)(?=[A-Z])', '-', class_name).lower()
                f.write(f"- `{class_name}` (JATS: `<{kebab_name}>`)\n")
        
        # 추가 모델
        f.write("\n\n## ➕ DTD에 없는 추가 모델 (구현되었지만 JATS 1.4에 없음)\n\n")
        f.write(f"총 {len(extra)}개의 추가 모델이 있습니다.\n\n")
        f.write("**주의**: 이들은 이전 버전 호환성, PMC 특화 요소, 또는 enum 타입일 수 있습니다.\n\n")
        
        if extra:
            current_letter = ''
            for class_name in extra:
                first_letter = class_name[0].upper()
                if first_letter != current_letter:
                    current_letter = first_letter
                    f.write(f"\n### {current_letter}\n\n")
                f.write(f"- `{class_name}`\n")
        
        # 구현된 모델
        f.write(f"\n\n## ✅ 이미 구현된 모델 ({len(common)}개)\n\n")
        f.write("이 모델들은 JATS 1.4와 일치합니다.\n\n")
        
        # 처음 50개만 표시
        if common:
            f.write("<details>\n<summary>구현된 모델 목록 보기 (처음 50개)</summary>\n\n")
            for class_name in common[:50]:
                kebab_name = re.sub(r'(?<!^)(?=[A-Z])', '-', class_name).lower()
                f.write(f"- `{class_name}` (`<{kebab_name}>`)\n")
            if len(common) > 50:
                f.write(f"\n... 외 {len(common) - 50}개\n")
            f.write("\n</details>\n")
        
        # 액션 아이템
        f.write("\n\n## 🎯 Action Items\n\n")
        f.write("### 우선순위 1: 핵심 누락 모델\n\n")
        f.write("다음 핵심 element들이 누락되어 있습니다:\n\n")
        
        # 중요한 누락 요소들을 식별
        critical_elements = [
            'License', 'LicenseP', 'OpenAccess',  # 라이선스 관련
            'DispFormula', 'DispFormulaGroup', 'InlineFormula',  # 수식
            'Verse', 'Poetry',  # 시/운문
            'Speech', 'Speaker',  # 대화
            'Question', 'Answer',  # Q&A
            'ProcessingMeta',  # 메타데이터
            'ArticleVersion', 'ArticleVersionAlternatives',  # 버전 관리
        ]
        
        for elem in critical_elements:
            if elem in missing:
                kebab_name = re.sub(r'(?<!^)(?=[A-Z])', '-', elem).lower()
                f.write(f"- [ ] `{elem}` (`<{kebab_name}>`)\n")
        
        f.write("\n### 우선순위 2: 컨텐츠 관련 누락 모델\n\n")
        
        content_elements = [name for name in missing if any(x in name.lower() for x in 
                          ['formula', 'citation', 'contrib', 'award', 'funding', 
                           'verse', 'question', 'index', 'milestone'])]
        
        for elem in sorted(content_elements)[:20]:
            kebab_name = re.sub(r'(?<!^)(?=[A-Z])', '-', elem).lower()
            f.write(f"- [ ] `{elem}` (`<{kebab_name}>`)\n")
        
        f.write("\n### 우선순위 3: 나머지 누락 모델\n\n")
        f.write(f"나머지 {len(missing) - len([e for e in critical_elements if e in missing]) - len(content_elements)}개의 모델은 필요시 구현\n")

def main():
    base_dir = Path("/Users/yeongyu.yang/IdeaProjects/pubmed-pmc-parser")
    jats_file = base_dir / "claudedocs/jats-analysis/jats-1.4-elements.txt"
    output_file = base_dir / "claudedocs/jats-analysis/comparison-report.md"
    
    print("=== JATS 1.4 vs 현재 모델 비교 분석 ===\n")
    
    # 데이터 로드
    print("1. JATS 1.4 element 목록 로드 중...")
    jats_elements = load_jats_elements(jats_file)
    print(f"   JATS element 개수: {len(jats_elements)}")
    
    print("\n2. 현재 Java 모델 목록 로드 중...")
    current_models = load_current_models(base_dir)
    print(f"   현재 모델 개수: {len(current_models)}")
    
    # 비교 분석
    print("\n3. 비교 분석 중...")
    missing, extra, common = categorize_elements(jats_elements, current_models)
    
    print(f"   일치: {len(common)}개")
    print(f"   누락: {len(missing)}개")
    print(f"   추가: {len(extra)}개")
    
    # 리포트 생성
    print(f"\n4. 리포트 생성 중: {output_file}")
    generate_report(output_file, missing, extra, common, len(jats_elements), len(current_models))
    
    print("\n=== 분석 완료 ===")
    print(f"\n상세 리포트: {output_file}")
    print(f"\n주요 통계:")
    print(f"  - 구현률: {len(common)/len(jats_elements)*100:.1f}%")
    print(f"  - 누락 모델: {len(missing)}개")
    print(f"  - 우선 구현 필요: ~20개 핵심 모델")

if __name__ == "__main__":
    main()
