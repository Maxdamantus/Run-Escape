#!/bin/sh
set -e

target=web-app/build/webTarget
empty_tree="$(echo -n | git mktree)"
commit_msg="Generated from $(git describe --always --dirty)"

./gradlew :web-app:clean
while git worktree remove "$target" 2>/dev/null; do :; done
git worktree add "$target" gh-pages
(cd "$target" && git read-tree -m -u "$empty_tree")
./gradlew :web-app:build
(cd "$target" && git add . && git commit -m "$commit_msg")
