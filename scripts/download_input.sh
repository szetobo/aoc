#!/bin/bash

# Configuration
SESSION_FILE=".session"
BASE_URL="https://adventofcode.com"

# Check for session file
if [ ! -f "$SESSION_FILE" ]; then
  echo "Error: Session file not found at $SESSION_FILE"
  echo "Please create the file and add your session cookie in this format:"
  echo "session=YOUR_SESSION_COOKIE"
  exit 1
fi

# Read session cookie
SESSION=$(cat "$SESSION_FILE")

# Get the current year and day
CURRENT_YEAR=$(date +%Y)
CURRENT_DAY=$(date +%d | sed 's/^0//') # Removes leading zero

# Prompt for year and default to current year if empty
read -p "Enter the Advent of Code year (default: $CURRENT_YEAR): " YEAR
YEAR=${YEAR:-$CURRENT_YEAR}

# Prompt for day and default to today's day if empty
read -p "Enter the day (1-25, default: $CURRENT_DAY): " DAY
DAY=${DAY:-$CURRENT_DAY}

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

# Define output file
OUTPUT_FILE="resources/${YEAR}/day${DAY_NUM}.txt"

mkdir -p `dirname "$OUTPUT_FILE"`

# Download input file
echo "Downloading Advent of Code input for Year $YEAR, Day $DAY_NUM..."
curl -s -b "$SESSION" "$BASE_URL/$YEAR/day/$DAY/input" -o "$OUTPUT_FILE"

# Check if download was successful
if [ $? -eq 0 ]; then
  echo "Input file saved as $OUTPUT_FILE"
else
  echo "Error: Failed to download input file."
fi
