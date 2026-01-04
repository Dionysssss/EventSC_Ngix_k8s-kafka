#!/usr/bin/env bash
# curl-timing.sh - measure timing for an endpoint
URL=${1:-http://eventsc.test/events?includeOutdated=yes}

curl -o /dev/null -s -w "DNS: %{time_namelookup}\nConnect: %{time_connect}\nTLS: %{time_appconnect}\nStartTransfer: %{time_starttransfer}\nTotal: %{time_total}\n" "$URL"
