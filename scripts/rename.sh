#!/usr/bin/env bash
# Renames the project, like the KMP wizard's "Project Name" and "Project ID":
#
#   scripts/rename.sh <base.package> <ProjectName> ["Display Name"]
#   scripts/rename.sh com.acme.notes AcmeNotes "Acme Notes"
#
# Changes app.basePackage, every Kotlin/SQLDelight package and its directories, the Android applicationId and
# the iOS bundle id (both = the package), the Gradle root project, Xcode product and Application class names
# (KmpTemplate -> ProjectName), and the display name in the app, the stores' metadata and the docs.
# The current values are read from the project, so it can be run again later to rename once more.
# A repository created from the GitHub template runs it automatically (.github/workflows/template-init.yml).
set -euo pipefail

usage() { sed -n '2,12p' "$0" | sed 's/^# \{0,1\}//'; exit 1; }
[[ $# -ge 2 ]] || usage

NEW_PKG="$1"
NEW_NAME="$2"
NEW_DISPLAY="${3:-$2}"

cd "$(dirname "$0")/.."

OLD_PKG="$(sed -n 's/^app\.basePackage=//p' gradle.properties)"
OLD_NAME="$(sed -n 's/^rootProject\.name = "\(.*\)"/\1/p' settings.gradle.kts)"
OLD_DISPLAY="$(sed -n 's:.*<string name="app_name">\(.*\)</string>.*:\1:p' \
  core/resources/src/commonMain/composeResources/values/strings.xml | head -1)"

[[ "$NEW_PKG" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]] || { echo "Not a package name: $NEW_PKG"; exit 1; }
[[ "$NEW_NAME" =~ ^[A-Z][A-Za-z0-9]*$ ]] || { echo "ProjectName must be PascalCase letters and digits: $NEW_NAME"; exit 1; }
[[ -n "$OLD_PKG" && -n "$OLD_NAME" && -n "$OLD_DISPLAY" ]] || { echo "Cannot read the current names"; exit 1; }

OLD_DIR="${OLD_PKG//.//}"
NEW_DIR="${NEW_PKG//.//}"

echo "Package: $OLD_PKG -> $NEW_PKG"
echo "Name:    $OLD_NAME -> $NEW_NAME"
echo "Display: $OLD_DISPLAY -> $NEW_DISPLAY"

prune=(-path ./.git -o -path ./.gradle -o -path ./.kotlin -o -path ./.idea -o -name build -o -name node_modules)

# 1. Move package directories (kotlin/ and sqldelight/ roots of every source set), through a temporary
#    directory so a new package nested in the old one (com.a.b -> com.a.b.c) works too.
if [[ "$OLD_PKG" != "$NEW_PKG" ]]; then
  find . \( "${prune[@]}" \) -prune -o -type d -path "*/$OLD_DIR" -print | sort -r | while IFS= read -r dir; do
    root="${dir%/$OLD_DIR}"
    tmp="$(mktemp -d "$root/.rename.XXXXXX")"
    (shopt -s dotglob nullglob; mv "$dir"/* "$tmp"/)
    rmdir -p "$dir" 2>/dev/null || true
    mkdir -p "$root/$NEW_DIR"
    (shopt -s dotglob nullglob; mv "$tmp"/* "$root/$NEW_DIR"/)
    rmdir "$tmp"
  done
fi

# 2. Rename files named after the project (KmpTemplateApplication.kt -> <Name>Application.kt).
find . \( "${prune[@]}" \) -prune -o -type f -name "*$OLD_NAME*" -print | while IFS= read -r file; do
  base="$(basename "$file")"
  mv "$file" "$(dirname "$file")/${base//$OLD_NAME/$NEW_NAME}"
done

# 3. Rewrite text references.
grep -rlI --exclude-dir={.git,.gradle,.kotlin,.idea,build,node_modules} \
  -e "$OLD_PKG" -e "$OLD_DIR" -e "$OLD_NAME" -e "$OLD_DISPLAY" . | while IFS= read -r f; do
  [[ "$f" == "./scripts/rename.sh" ]] && continue
  OLD_PKG="$OLD_PKG" NEW_PKG="$NEW_PKG" OLD_DIR="$OLD_DIR" NEW_DIR="$NEW_DIR" \
  OLD_NAME="$OLD_NAME" NEW_NAME="$NEW_NAME" OLD_DISPLAY="$OLD_DISPLAY" NEW_DISPLAY="$NEW_DISPLAY" \
  perl -pi -e '
    s{\Q$ENV{OLD_PKG}\E}{$ENV{NEW_PKG}}g;
    s{\Q$ENV{OLD_DIR}\E}{$ENV{NEW_DIR}}g;
    s{\Q$ENV{OLD_NAME}\E}{$ENV{NEW_NAME}}g;
    s{\Q$ENV{OLD_DISPLAY}\E}{$ENV{NEW_DISPLAY}}g' "$f"
done

# 4. The project is initialized: the template workflow skips it from now on.
rm -f .github/template-init

echo "Done. Next: ./gradlew testAndroidHostTest :androidApp:assembleDebug"
