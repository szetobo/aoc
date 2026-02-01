def main []: string -> nothing {
    let lines: list<record<dir: string, steps: int>> = $in
            | lines | where { $in != "" }
            | parse --regex '(?P<dir>[LR])(?P<steps>\d+)'
            | into int steps

    mut p1 = 0
    mut p2 = 0
    mut pos = 50

    for ln in $lines {
        let d: int = if $ln.dir == "L" { -1 } else { 1 }
        for _ in 1..$ln.steps {
            $pos = ($pos + $d) mod 100
            if $pos == 0 { $p2 += 1 }
        }
        if $pos == 0 { $p1 += 1 }
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
