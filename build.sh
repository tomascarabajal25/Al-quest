#!/bin/bash
find src -name "*.java" > /tmp/sources.txt
javac -d out/production/Al-quest -cp out/production/Al-quest @/tmp/sources.txt
# Copy non-Java resources (sprites, images, etc.) preserving package structure
find src -type f ! -name "*.java" -exec bash -c 'target="out/production/Al-quest/${1#src/}"; mkdir -p "$(dirname "$target")"; cp "$1" "$target"' _ {} \;