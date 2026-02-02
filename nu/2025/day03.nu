def main []: string -> nothing {
    let lines = $in | lines | where { $in != "" }

    mut p1 = 0
    mut p2 = 0

    for line in $lines {
        $p1 += (pick $line 2)
        $p2 += (pick $line 12)
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}

def pick [s: string, n: int] {
    let chars = $s | split chars
    let len = ($chars | length)
    mut res = []
    mut sta = 0

    for i in $n..1 {
        mut p = $sta
        mut d = "0"

        let end = $len - $i
        if $sta <= $end {
            for j in $sta..$end {
                let v = ($chars | get $j)
                if $v > $d {
                    $p = $j
                    $d = $v
                }
            }
        }
        $res = ($res | append $d)
        $sta = $p + 1
    }
    $res | str join | into int
}
