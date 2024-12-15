#!/bin/bash

# Get the current year and day
CURRENT_YEAR=$(date +%Y)
CURRENT_DAY=$(date +%d | sed 's/^0//') # Removes leading zero

DAY=${1:-$CURRENT_DAY}
YEAR=${2:-$CURRENT_YEAR}

# Validate input
if ! [[ "$YEAR" =~ ^[0-9]{4}$ ]]; then
  echo "Error: Invalid year format."
  exit 1
fi

if ! [[ "$DAY" =~ ^[0-9]+$ ]] || [ "$DAY" -lt 1 ] || [ "$DAY" -gt 25 ]; then
  echo "Error: Day must be a number between 1 and 25."
  exit 1
fi

# Format day to two digits (e.g., 01, 02)
DAY_NUM=$(printf "%02d" "$DAY")

PROG_FILE="cmd/${YEAR}/day${DAY_NUM}/main.go"
INPUT_FILE="resources/${YEAR}/day${DAY_NUM}.txt"

go run "$PROG_FILE" < "$INPUT_FILE"
