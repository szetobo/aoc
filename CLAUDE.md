# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Polyglot Advent of Code solutions repository. Solutions are implemented in multiple languages: Clojure (legacy/historical), Go, TypeScript, Python, Hy (Lisp dialect on Python), and Nushell.

## Commands

All commands use `just` (justfile task runner). Day/year default to current date.

**Scaffolding and setup:**
```bash
just prepare [day] [year]   # Create solution files from templates for all languages
just download [day] [year]  # Download input (requires AOC_SESSION in .env)
just edit [day] [year]      # Open all language solutions + input in $EDITOR
```

**Running solutions (input from stdin):**
```bash
just gorun [day] [year]     # Go: go run ./go/YYYY/dayNN < input
just tsrun [day] [year]     # TypeScript: bun run ./ts/YYYY/dayNN.ts < input
just pyrun [day] [year]     # Python: uv run ./py/YYYY/dayNN.py < input
just hyrun [day] [year]     # Hy: uv run hy ./hy/YYYY/dayNN.hy < input
just nurun [day] [year]     # Nushell: nu ./nu/YYYY/dayNN.nu < input
```

**Testing with sample input:**
```bash
just got [day] [year]       # Go test with .sample file
just tst [day] [year]       # TypeScript test
just pyt [day] [year]       # Python test
just hyt [day] [year]       # Hy test
just nut [day] [year]       # Nushell test
```

**Clojure (legacy):**
```bash
clj -X abagile.aoc.YYYY.dayNN/-main  # Run solution
clj -M:nrepl                          # Start nrepl for development
```

## Project Structure

```
go/YYYY/dayNN/main.go       # Go solutions (2024-2025)
ts/YYYY/dayNN.ts            # TypeScript solutions (2025)
py/YYYY/dayNN.py            # Python solutions (2025)
hy/YYYY/dayNN.hy            # Hy solutions (2025)
nu/YYYY/dayNN.nu            # Nushell solutions (2025)
src/abagile/aoc/YYYY/       # Clojure solutions (2015-2023)
resources/YYYY/dayNN.txt    # Puzzle inputs
resources/YYYY/dayNN.sample # Test inputs (optional)
```

## Solution Pattern

All solutions read from stdin and print "Part 1: X" and "Part 2: Y". Templates exist in:
- `go/main.go` - Go template
- `ts/template.ts` - TypeScript template
- `py/template.py` - Python template
- `hy/template.hy` - Hy template
- `nu/template.nu` - Nushell template

## Clojure Utilities (for Clojure solutions)

**src/abagile/aoc/util.clj** - Input parsing, math helpers (`manhattan-distance`, `transpose`, `parse-int`)

**src/abagile/aoc/grid.clj** - 2D grid operations, direction offsets, adjacency functions (`adjacent-4`, `adjacent-8`)

**src/abagile/aoc/algo.clj** - Algorithms: `dijkstra`, `a*`, `subset-sum-01`, `subset-sum-unbounded`

## Environment Setup

- `.env` file with `AOC_SESSION=<session_cookie>` for downloading inputs
- Version management via `.tool-versions` (asdf-vm)
- Go deps: `github.com/spakin/awk`, `gonum.org/v1/gonum`
- Python/Hy deps: managed via `uv` with `pyproject.toml`
