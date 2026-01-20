#!/usr/bin/env python3
"""Final analysis of remaining 35 missed branches"""

import re

html_file = 'build/reports/jacoco/test/html/com.brillianttiger.bio.parser.pmc.parser/ArticleMetaParser.java.html'

with open(html_file, 'r', encoding='utf-8') as f:
    content = f.read()

# Find all "pc bpc" (partially covered branches) lines
pattern = r'<span class="pc bpc" id="L(\d+)" title="(\d+) of (\d+) branches missed\.">([^<]+)</span>'
matches = re.findall(pattern, content)

print(f"=== Final Analysis of {len(matches)} Partially Covered Lines ===\n")

# Categorize by pattern
categories = {
    'END_ELEMENT equals false': 0,
    'isEmpty() check': 0,
    'hasNext() false': 0,
    'CHARACTERS || CDATA': 0,
    'Other event types': 0,
    'Other': 0
}

details = []
for line_num, missed, total, code_snippet in matches:
    snippet = code_snippet.strip()[:80]
    
    if 'reader.getLocalName().equals(' in snippet and 'END_ELEMENT' in code_snippet:
        category = 'END_ELEMENT equals false'
    elif '.isEmpty()' in snippet:
        category = 'isEmpty() check'
    elif 'hasNext()' in snippet:
        category = 'hasNext() false'
    elif 'CHARACTERS || CDATA' in code_snippet or 'XMLStreamConstants.CDATA' in snippet:
        category = 'CHARACTERS || CDATA'
    elif 'END_ELEMENT' in snippet and 'else if' in snippet:
        category = 'Other event types'
    else:
        category = 'Other'
    
    categories[category] += int(missed)
    details.append((line_num, missed, total, category, snippet))

print("Branch Category Distribution:")
print("-" * 60)
for cat, count in sorted(categories.items(), key=lambda x: -x[1]):
    if count > 0:
        print(f"  {cat:<30} {count:>3} branches")

print(f"\n{'='*60}")
print("Structural Analysis:")
print(f"{'='*60}")

structural_impossible = categories['END_ELEMENT equals false']
potentially_testable = sum(count for cat, count in categories.items() 
                           if cat not in ['END_ELEMENT equals false'])

print(f"\nStructurally Impossible:  {structural_impossible} branches")
print(f"  (equals() false in while(true) END_ELEMENT checks)")
print(f"\nPotentially Testable:     {potentially_testable} branches")
print(f"\nTotal Missed:             {structural_impossible + potentially_testable} branches")

print(f"\n{'='*60}")
print("Top Lines to Investigate (Non-Structural):")
print(f"{'='*60}")

for line_num, missed, total, category, snippet in sorted(details, key=lambda x: -int(x[1])):
    if category != 'END_ELEMENT equals false' and int(missed) > 0:
        print(f"Line {line_num}: {missed}/{total} missed - {category}")
        print(f"  Code: {snippet}")
        print()

