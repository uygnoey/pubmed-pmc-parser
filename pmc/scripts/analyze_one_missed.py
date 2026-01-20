#!/usr/bin/env python3
"""Analyze methods with exactly 1 missed branch"""

import re

html_file = 'build/reports/jacoco/test/html/com.brillianttiger.bio.parser.pmc.parser/ArticleMetaParser.html'

with open(html_file, 'r', encoding='utf-8') as f:
    content = f.read()

# Find all rows with exactly 1 missed branch
# Look for: redbar title="1" in branch coverage column
pattern = r'<tr>.*?<a href="#L(\d+)" class="el_method">([^<]+)</a>.*?redbar\.gif"[^>]+title="1".*?</tr>'
matches = re.findall(pattern, content, re.DOTALL)

print(f"=== Methods with exactly 1 missed branch: {len(matches)} ===\n")

# Read the source to check what kind of branches they are
source_pattern = r'<span class="[^"]*bpc[^"]*" id="L(\d+)" title="1 of 2 branches missed\.">([^<]+)</span>'
source_matches = re.findall(source_pattern, content)

branch_types = {}
for line_num, code in source_matches:
    if 'equals(' in code and 'reader.getLocalName()' in code:
        branch_types[line_num] = 'END_ELEMENT equals false (structural)'
    elif 'isEmpty()' in code:
        branch_types[line_num] = 'isEmpty() check (testable)'
    elif 'hasNext()' in code:
        branch_types[line_num] = 'hasNext() false (structural)'
    else:
        branch_types[line_num] = f'Other: {code[:50]}'

print("Branch type distribution:")
for branch_type in set(branch_types.values()):
    count = list(branch_types.values()).count(branch_type)
    print(f"  {branch_type}: {count}")

print(f"\nTotal 1-missed branches analyzed: {len(branch_types)}")
