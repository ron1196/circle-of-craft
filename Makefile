JAVA_HOME := /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export JAVA_HOME

.PHONY: build run format check debug scan ralph ralph-all

build:
	./gradlew build

run:
	./gradlew runClient

format:
	./gradlew spotlessApply

check:
	./gradlew spotlessCheck

ralph:
	@test -n "$(LABEL)" || (echo "Usage: make ralph LABEL=<feature-label>  or  make ralph-all" && exit 1)
	npx tsx .sandcastle/main.mts $(LABEL)

ralph-all:
	npx tsx .sandcastle/main.mts all
