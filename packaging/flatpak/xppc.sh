#!/bin/sh

export PATH=/app/jre/bin:/usr/bin
export JAVA_HOME=/app/jre
exec /app/xppc/bin/xppc "$@"