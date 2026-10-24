#!/usr/bin/env bash
#
# Release helper for JAccessPDF's multi-module Maven build.
#
# Usage:
#   ./release.sh [--no-deploy] <release-version> [<next-snapshot-version>]
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
#   4. Asks whether to upload. If yes, deploys to Maven Central first
#      (mvn -Prelease deploy: full test run, GPG signing) and then to GitHub
#      Packages (mvn deploy). Skip both with --no-deploy. The Central upload is
#      NOT a publication - it waits in the portal until you press Publish.
#   5. Sets every pom.xml to <next-snapshot-version> and commits
#      "Initiating next development iteration".
#   6. Prints the push commands - it does NOT push, you do that yourself.

set -euo pipefail

DO_DEPLOY=1
if [[ "${1:-}" == "--no-deploy" ]]; then
    DO_DEPLOY=0
    shift
fi

if [[ $# -lt 1 ]]; then
    echo "Usage: $0 [--no-deploy] <release-version> [<next-snapshot-version>]" >&2
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

# Upload has to happen HERE: the poms still hold ${RELEASE_VERSION}. After the
# bump below they hold ${NEXT_SNAPSHOT}, and a deploy would push a snapshot
# instead of the release. Runs the full test suite and signs with GPG (unlike
# the -DskipTests sanity build above), then uploads to Maven Central, where the
# deployment waits in VALIDATED state for a manual "Publish" in the portal.
if [[ "$DO_DEPLOY" == "1" ]]; then
    echo
    read -r -p "Upload ${RELEASE_VERSION} to Maven Central and GitHub Packages now? [y/N] " DEPLOY_CONFIRM
    if [[ "$DEPLOY_CONFIRM" == "y" || "$DEPLOY_CONFIRM" == "Y" ]]; then
        # Central first: it validates the bundle, so a problem surfaces before
        # anything has landed on GitHub Packages.
        echo "==> Deploying to Maven Central (gpg will ask for the passphrase)"
        if ! mvn -Prelease deploy; then
            echo >&2
            echo "Central deploy failed. The release commit and tag v${RELEASE_VERSION}" >&2
            echo "exist, but the bump to ${NEXT_SNAPSHOT} has NOT happened, so the poms" >&2
            echo "still hold ${RELEASE_VERSION}. Nothing went to GitHub Packages either." >&2
            echo "Fix the cause and retry with:" >&2
            echo "  mvn -Prelease deploy" >&2
            echo "Or unwind with:" >&2
            echo "  git reset --hard HEAD~1 && git tag -d v${RELEASE_VERSION}" >&2
            exit 1
        fi
        echo "==> Uploaded to Central. Nothing is public yet - inspect it at"
        echo "    https://central.sonatype.com/publishing/deployments"
        echo "    and press Publish (or Drop to discard it)."

        # GitHub Packages second, unchanged from how it worked before Central.
        # No -Prelease here, so no GPG signatures: GitHub does not require them,
        # which means the GitHub artifacts carry no .asc while the Central ones do.
        echo
        echo "==> Deploying to GitHub Packages"
        if ! mvn deploy -DskipTests; then
            echo >&2
            echo "GitHub Packages deploy failed, but the Central upload above went" >&2
            echo "through. The two are now out of step: either retry with" >&2
            echo "  mvn deploy -DskipTests" >&2
            echo "or Drop the Central deployment in the portal before publishing it." >&2
            exit 1
        fi
        echo "==> Uploaded to GitHub Packages."
    else
        echo "Skipped both uploads. To do them later, check out the tag first:"
        echo "  git checkout v${RELEASE_VERSION}"
        echo "  mvn -Prelease deploy     # Maven Central"
        echo "  mvn deploy -DskipTests   # GitHub Packages"
    fi
fi

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
if [[ "$DO_DEPLOY" == "1" ]]; then
    echo
    echo "Reminder: a Central deployment left in VALIDATED state is not published."
    echo "Publish or drop it at https://central.sonatype.com/publishing/deployments"
fi
