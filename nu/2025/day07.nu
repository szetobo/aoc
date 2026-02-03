def main []: string -> nothing {
    let lines = $in | lines | where { $in != "" }

    mut p1 = 0
    mut p2 = 0

    mut grid = ($lines | each { split chars })
    let n = ($grid | length)
    mut M = {}

    let inc_M = {|m, k, v|
        $m | upsert $k (($m | get -i $k | default 0) + $v)
    }

    for row in 0..($n - 2) {
        let current_row = ($grid | get $row)
        for col in 0..($n - 2) {
            let ch = ($current_row | get $col)
            if $ch != "|" and $ch != "S" { continue }

            let cnt = ($M | get -i $"($row),($col)" | default 1)
            let next_char = $grid | get ($row + 1) | get $col

            if $next_char == "^" {
                $grid = ($grid | upsert ([($row + 1), ($col - 1)] | into cell-path) "|")
                $M = (do $inc_M $M $"($row + 1),($col - 1)" $cnt)
                $grid = ($grid | upsert ([($row + 1), ($col + 1)] | into cell-path) "|")
                $M = (do $inc_M $M $"($row + 1),($col + 1)" $cnt)
                $p1 += 1
            } else if $next_char == "." or $next_char == "|" {
                $grid = ($grid | upsert ([($row + 1), $col] | into cell-path) "|")
                $M = (do $inc_M $M $"($row + 1),($col)" $cnt)
            }
        }
    }

    let final_M = $M
    $p2 = $grid | last | enumerate
        | where item == "|"
        | each {|e| $final_M | get -i $"($n - 1),($e.index)" | default 0}
        | math sum

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
