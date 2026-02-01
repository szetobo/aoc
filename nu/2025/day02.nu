def main []: string -> nothing {
    let ranges = $in | lines | where { $in != "" }
        | each {|line| $line | split row "," } | flatten
        | parse "{n1}-{n2}" | into int n1 n2

    mut p1 = 0
    mut p2 = 0

    for r in $ranges {
        for i in ($r.n1..$r.n2) {
            let s = $i | into string
            let n = $s | str length

            if $n mod 2 == 0 {
                let half = $n // 2
                let first = $s | str substring 0..($half - 1)
                let second = $s | str substring $half..
                if $first == $second { $p1 += $i }
            }

            let doubled = $s + $s
            let inner = $doubled | str substring 1..-2
            if ($inner | str contains $s) { $p2 += $i }
        }
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
