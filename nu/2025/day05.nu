def main []: string -> nothing {
    let parts = $in | split row "\n\n"

    let ranges_raw = ($parts | get 0 | lines)
    let R = ($ranges_raw | each { split row "-" | into int })

    let numbers_raw = ($parts | get 1 | lines)
    let L = ($numbers_raw | into int)

    let p1 = ($L | filter {|n|
        $R | any {|r| $n >= ($r | get 0) and $n <= ($r | get 1) }
    } | length)

    let sorted_R = ($R | sort-by 0)

    let res = ($sorted_R | reduce --fold {p2: 0, lst: 0} {|it, acc|
        let s = ($it | get 0)
        let e = ($it | get 1)
        mut new_p2 = $acc.p2

        if $e > $acc.lst {
            let limit = ([$s ($acc.lst + 1)] | math max)
            let added = $e - $limit + 1
            $new_p2 += $added
        }

        let new_lst = ([$acc.lst $e] | math max)

        {p2: $new_p2, lst: $new_lst}
    })
    let p2 = $res.p2

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
