#!/usr/bin/env bash
# Builds every release JAR into build/release/: one Fabric JAR (26.1.2–26.3) plus one
# NeoForge JAR per entry of neoforge_release_targets in gradle.properties.
set -euo pipefail
cd "$(dirname "$0")/.."

prop() { grep -E "^$1=" gradle.properties | cut -d= -f2-; }
out=build/release
rm -rf "$out" && mkdir -p "$out"

./gradlew :common:test :fabric:build
cp fabric/build/libs/LimitedSpectator-fabric-"$(prop mod_version)".jar "$out"/

for target in $(prop neoforge_release_targets); do
    mc=${target%%:*}
    neo=${target#*:}
    IFS=. read -r major minor _ <<< "$mc"
    range="[$mc,$major.$((minor + 1)))"
    echo "== NeoForge $neo (Minecraft $range)"
    ./gradlew :neoforge:build -Pneoforge_minecraft_version="$mc" -Pneo_version="$neo" \
        -Pneoforge_minecraft_version_range="$range"
    cp neoforge/build/libs/LimitedSpectator-neoforge-"$mc"-"$(prop mod_version)".jar "$out"/
done

ls -1 "$out"
