def main []: string -> nothing {
    let input = $in | lines | where { $in != "" }

    let width = $input | first | str length
    let height = $input | length
    let border_row = "." | fill --width ($width + 2) --character "."

    mut grid = [$border_row]
        | append ($input | each {|line| $'.' + $line + $'.'})
        | append [$border_row]
        | each { split chars }

    mut p1 = 0
    mut p2 = 0

    mut part = 1
    loop {
        let done = $p2
        for r in 1..$height {
            let row_prev = ($grid | get ($r - 1))
            let row_curr = ($grid | get $r)
            let row_next = ($grid | get ($r + 1))
            for c in 1..$width {
                if ($row_curr | get $c) == "@" {
                    mut neighbors = 0
                    if ($row_prev | get ($c - 1)) == "@" { $neighbors += 1 }
                    if ($row_prev | get $c)       == "@" { $neighbors += 1 }
                    if ($row_prev | get ($c + 1)) == "@" { $neighbors += 1 }
                    if ($row_curr | get ($c - 1)) == "@" { $neighbors += 1 }
                    if ($row_curr | get ($c + 1)) == "@" { $neighbors += 1 }
                    if ($row_next | get ($c - 1)) == "@" { $neighbors += 1 }
                    if ($row_next | get $c)       == "@" { $neighbors += 1 }
                    if ($row_next | get ($c + 1)) == "@" { $neighbors += 1 }
                    if $neighbors < 4 {
                        if $part == 1 {
                            $p1 += 1
                        } else {
                            let row = ($grid | get $r | upsert $c "x")
                            $grid = ($grid | upsert $r $row)
                            $p2 += 1
                        }
                    }
                }
            }
        }
        if $part == 2 and $p2 == $done {
            break
        }
        $part = 2
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
