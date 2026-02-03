def main []: string -> nothing {
    let lines = $in | lines | where { $in != "" }

    mut p1 = 0
    mut p2 = 0

    let tokens = ($lines | each { split row " " | where { $in != "" } })
    let widths = ($tokens | each { length })
    let min_width = ($widths | math min)

    for c in 0..($min_width - 1) {
        let col = ($tokens | each {|row| $row | get $c })
        let op = ($col | last)
        let nums = ($col | drop 1 | into int)

        if $op == "+" {
            $p1 += ($nums | math sum)
        } else {
            $p1 += ($nums | math product)
        }
    }

    let chars = ($lines | each { split chars })

    let char_width = ($chars | each { length } | math min)

    mut fs = true
    mut current_op = ""
    mut current_r = 0

    for c in 0..($char_width - 1) {
        let col = ($chars | each {|row| $row | get $c })
        let op = ($col | last)
        let nums_chars = ($col | drop 1)

        if $fs and $op != " " {
            $current_op = $op
            $current_r = if $current_op == "+" { 0 } else { 1 }
            $fs = false
        }

        if ($nums_chars | all {|x| $x == " " }) {
            $fs = true
        } else {
            let v = ($nums_chars | str join | str trim | into int)

            if $current_op == "+" {
                $current_r += $v
            } else {
                $current_r *= $v
            }
        }

        if $fs or ($c == $char_width - 1) {
            $p2 += $current_r
        }
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
