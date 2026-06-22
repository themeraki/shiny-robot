#!/usr/bin/env sh
# This is a standard Gradle wrapper script.
set -o errexit
set -o nounset
# Default JVM options
DEFAULT_JVM_OPTS=""
# Find the java executable
if [ -n "${JAVA_HOME:-}" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi
# Run the Gradle Wrapper jar
exec "$JAVACMD" $DEFAULT_JVM_OPTS -jar "$(dirname "$0")/gradle/wrapper/gradle-wrapper.jar" "$@"
