JAVA_HOME := /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export JAVA_HOME

.PHONY: build run format check debug scan ralph

build:
	./gradlew build

run:
	./gradlew runClient

format:
	./gradlew spotlessApply

check:
	./gradlew spotlessCheck

ralph:
	npx tsx .sandcastle/main.mts
