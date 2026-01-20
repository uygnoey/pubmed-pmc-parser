#!/usr/bin/env python3
"""
Analyze JaCoCo HTML coverage report to find uncovered lines in specific methods
"""

import re
import sys

def analyze_coverage(html_file, method_start_lines):
    """
    Parse JaCoCo HTML and find uncovered/partially covered lines

    Args:
        html_file: Path to JaCoCo HTML file
        method_start_lines: Dict of method_name -> start_line
    """
    with open(html_file, 'r', encoding='utf-8') as f:
        html_content = f.read()

    # Find all span elements with line IDs using regex
    # Pattern: <span class="..." id="L123">
    pattern = r'<span class="([^"]*)" id="L(\d+)">'
    matches = re.finditer(pattern, html_content)

    # Group lines by coverage status
    uncovered = []
    partially_covered = []

    for match in matches:
        coverage_class = match.group(1)
        line_num = int(match.group(2))

        if 'nc' in coverage_class:  # not covered
            # Extract text between this span and next span/tag
            start_pos = match.end()
            end_pos = html_content.find('<', start_pos)
            text = html_content[start_pos:end_pos].strip()[:100]
            uncovered.append((line_num, text))
        elif 'pc' in coverage_class:  # partially covered
            start_pos = match.end()
            end_pos = html_content.find('<', start_pos)
            text = html_content[start_pos:end_pos].strip()[:100]
            partially_covered.append((line_num, text))

    # Find which method each uncovered line belongs to
    method_coverage = {}
    for method_name, start_line in method_start_lines.items():
        method_coverage[method_name] = {
            'uncovered': [],
            'partially': []
        }

        # Assume method ends at next method start or +100 lines
        end_line = start_line + 100
        for next_method, next_start in method_start_lines.items():
            if next_start > start_line and next_start < end_line:
                end_line = next_start

        for line_num, text in uncovered:
            if start_line <= line_num < end_line:
                method_coverage[method_name]['uncovered'].append((line_num, text))

        for line_num, text in partially_covered:
            if start_line <= line_num < end_line:
                method_coverage[method_name]['partially'].append((line_num, text))

    return method_coverage

def main():
    html_file = '/Users/yeongyu.yang/IdeaProjects/pubmed-pmc-parser/pmc/build/reports/jacoco/test/html/com.brillianttiger.bio.parser.pmc.parser/ArticleMetaParser.java.html'

    # Methods with missed branches (from coverage report)
    methods = {
        'parseAff': 433,
        'parsePubHistory': 2460,
        'parseAffAlternatives': 2397,
        'parseEvent': 2507,
        'parseChemStruct': 2291,
        'parseChemStructWrap': 2105,
        'parsePreformat': 1910,
        'parseSupplementaryMaterial': 761,
        'parseArticleMeta': 27,
        'parseContrib': 309,
    }

    coverage = analyze_coverage(html_file, methods)

    # Print results
    print("=== Uncovered Lines Analysis ===\n")
    for method, data in sorted(coverage.items(), key=lambda x: len(x[1]['uncovered']) + len(x[1]['partially']), reverse=True):
        total_missed = len(data['uncovered']) + len(data['partially'])
        if total_missed > 0:
            print(f"\n{method}:")
            print(f"  Total missed lines: {total_missed}")

            if data['uncovered']:
                print(f"  Not covered ({len(data['uncovered'])}):")
                for line_num, text in data['uncovered']:
                    print(f"    Line {line_num}: {text[:80]}")

            if data['partially']:
                print(f"  Partially covered ({len(data['partially'])}):")
                for line_num, text in data['partially']:
                    print(f"    Line {line_num}: {text[:80]}")

if __name__ == '__main__':
    main()
