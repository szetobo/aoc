def main []: string -> nothing {
    let lines = $in | lines | where { $in != "" }
    print $lines

    mut p1 = 0
    mut p2 = 0

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
