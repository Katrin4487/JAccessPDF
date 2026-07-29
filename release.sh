#!/usr/bin/env bash
#
# Release helper for JAccessPDF's multi-module Maven build.
#
# Usage:
#   ./release.sh <release-version> [<next-snapshot-version>]
#
# Example:
#   ./release.sh 0.10.3
#   (next dev version defaults to 0.10.4-SNAPSHOT, bumping the patch number)
#
# What it does:
#   1. Verifies the working tree is clean and CHANGELOG.md has an entry for
#      the release version (you must have written that by hand beforehand).
#   2. Strips "-SNAPSHOT" from every pom.xml (root + all modules) and commits
#      "Bump version to <release-version>".
#   3. Creates an annotated tag v<release-version>.
#   4. Sets every pom.xml to <next-snapshot-version> and commits
#      "Initiating next development iteration".
#   5. Prints the push commands - it does NOT push, you do that yourself.

set -euo pipefail

if [[ $# -lt 1 ]]; then
    echo "Usage: $0 <release-version> [<next-snapshot-version>]" >&2
    exit 1
fi

RELEASE_VERSION="$1"

if [[ $# -ge 2 ]]; then
    NEXT_SNAPSHOT="$2"
else
    IFS='.' read -r MAJOR MINOR PATCH <<< "$RELEASE_VERSION"
    NEXT_SNAPSHOT="${MAJOR}.${MINOR}.$((PATCH + 1))-SNAPSHOT"
fi

CURRENT_VERSION=$(sed -n 's/.*<version>\(.*-SNAPSHOT\)<\/version>.*/\1/p' pom.xml | head -1)

if [[ -z "$CURRENT_VERSION" ]]; then
    echo "Could not determine current SNAPSHOT version from pom.xml" >&2
    exit 1
fi

echo "Current version:      $CURRENT_VERSION"
echo "Release version:      $RELEASE_VERSION"
echo "Next snapshot version: $NEXT_SNAPSHOT"
echo

if ! git diff --quiet || ! git diff --cached --quiet; then
    echo "There are uncommitted changes to tracked files. Commit or stash them first." >&2
    echo "(Untracked files are ignored by this check.)" >&2
    git status --short >&2
    exit 1
fi

if ! grep -q "## \[${RELEASE_VERSION}\]" CHANGELOG.md; then
    echo "CHANGELOG.md has no '## [${RELEASE_VERSION}]' section yet." >&2
    echo "Move the [Unreleased] entries into a dated section for ${RELEASE_VERSION} first." >&2
    exit 1
fi

POM_FILES=(pom.xml */pom.xml)

read -r -p "Proceed with release ${RELEASE_VERSION}? [y/N] " CONFIRM
if [[ "$CONFIRM" != "y" && "$CONFIRM" != "Y" ]]; then
    echo "Aborted."
    exit 1
fi

echo "==> Checking license headers (license-header.txt)"
if ! mvn -q license:check; then
    echo "License header check failed - run 'mvn license:format' to fix, then commit and re-run." >&2
    exit 1
fi

echo "==> Stripping -SNAPSHOT (${CURRENT_VERSION} -> ${RELEASE_VERSION})"
for f in "${POM_FILES[@]}"; do
    [[ -f "$f" ]] || continue
    sed -i.bak "s#<version>${CURRENT_VERSION}</version>#<version>${RELEASE_VERSION}</version>#g" "$f"
    rm -f "${f}.bak"
done

echo "==> Building to verify the release version compiles"
mvn -q clean install -DskipTests

git add "${POM_FILES[@]}" CHANGELOG.md
git commit -m "Bump version to ${RELEASE_VERSION}"

echo "==> Tagging v${RELEASE_VERSION}"
git tag -a "v${RELEASE_VERSION}" -m "Release ${RELEASE_VERSION}"

echo "==> Opening next development iteration (${RELEASE_VERSION} -> ${NEXT_SNAPSHOT})"
for f in "${POM_FILES[@]}"; do
    [[ -f "$f" ]] || continue
    sed -i.bak "s#<version>${RELEASE_VERSION}</version>#<version>${NEXT_SNAPSHOT}</version>#g" "$f"
    rm -f "${f}.bak"
done

git add "${POM_FILES[@]}"
git commit -m "Initiating next development iteration"

echo
echo "Done. Review the last two commits and the tag, then push explicitly:"
echo "  git push"
echo "  git push origin v${RELEASE_VERSION}"
