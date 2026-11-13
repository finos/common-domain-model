"""Fail if a dependency in a license-maven-plugin report has no allowed licence.

Usage: python3 check_licenses.py <licenses.xml> <allowed-licenses.txt>
"""
import sys
import xml.etree.ElementTree as ET


def read_allowed(path):
    with open(path, encoding="utf-8") as f:
        lines = (line.strip() for line in f)
        return {line for line in lines if line and not line.startswith("#")}


def main(report_path, allowed_path):
    allowed = read_allowed(allowed_path)
    dependencies = ET.parse(report_path).getroot().iter("dependency")
    rejected = []
    count = 0
    for dependency in dependencies:
        count += 1
        gav = ":".join(dependency.findtext(tag, "") for tag in ("groupId", "artifactId", "version"))
        names = [(name or "").strip() for name in
                 (license.findtext("name") for license in dependency.iterfind("licenses/license"))]
        if not any(name in allowed for name in names):
            rejected.append((gav, names))

    if not rejected:
        print(f"All {count} dependencies have an allowed licence.")
        return 0

    print(f"{len(rejected)} of {count} dependencies have no allowed licence:")
    for gav, names in rejected:
        print(f"  {gav}: {' | '.join(names) if names else '(no licence declared)'}")
    print(f"\nIf a licence is acceptable, add its name to {allowed_path}.")
    return 1


if __name__ == "__main__":
    sys.exit(main(*sys.argv[1:]))
