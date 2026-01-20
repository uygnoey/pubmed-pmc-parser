#!/usr/bin/env python3
"""Extract missed branches by method from JaCoCo HTML index"""

import re

html_file = '/Users/yeongyu.yang/IdeaProjects/pubmed-pmc-parser/pmc/build/reports/jacoco/test/html/com.brillianttiger.bio.parser.pmc.parser/index.html'

with open(html_file, 'r', encoding='utf-8') as f:
    content = f.read()

# Pattern to find method rows with missed branches
# Look for: method name, missed branches count, total branches, coverage %
pattern = r'<a href="ArticleMetaParser\.java\.html#L\d+" class="el_method">([^<]+)</a>.*?title="(\d+)".*?<img src="\.\./jacoco-resources/greenbar\.gif"[^>]+title="(\d+)".*?<td class="ctr2"[^>]*>(\d+)%</td>'

matches = re.findall(pattern, content, re.DOTALL)

# Parse results
methods = []
for match in matches:
    method_name = match[0].replace('(XMLStreamReader)', '')
    missed = int(match[1])
    covered = int(match[2])
    coverage_pct = int(match[3])
    total = missed + covered

    if missed > 0:
        methods.append({
            'name': method_name,
            'missed': missed,
            'total': total,
            'coverage': coverage_pct
        })

# Sort by missed branches descending
methods.sort(key=lambda x: x['missed'], reverse=True)

print("=== Methods with Missed Branches (ArticleMetaParser) ===\n")
print(f"{'Method':<40} {'Missed':>8} {'Total':>8} {'Coverage':>10}")
print("-" * 70)

total_missed = 0
for m in methods:
    print(f"{m['name']:<40} {m['missed']:>8} {m['total']:>8} {m['coverage']:>9}%")
    total_missed += m['missed']

print("-" * 70)
print(f"{'TOTAL':<40} {total_missed:>8}")
print(f"\nMethods with missed branches: {len(methods)}")
