def main []: string -> nothing {
    let input = $in
    let points = ($input | lines | where { $in != "" } | each { split row "," | into int })
    let n = ($points | length)

    # Pre-extract coordinates for faster access in loops
    let X = ($points | each { $in.0 })
    let Y = ($points | each { $in.1 })
    let Z = ($points | each { $in.2 })

    # Generate all edges using parallel processing and list structures
    # We iterate i from 0..n-2 and j from i+1..n-1, effectively calculating each pair once (N^2/2).
    # Using 'zip' and 'range' avoids costly random access 'get' calls in the inner loop.
    let edges = (
        0..($n - 2) | par-each {|i|
            let xi = ($X | get $i)
            let yi = ($Y | get $i)
            let zi = ($Z | get $i)

            # Slice the remaining points (j starts at i + 1)
            let rest_X = ($X | slice ($i + 1)..)
            let rest_Y = ($Y | slice ($i + 1)..)
            let rest_Z = ($Z | slice ($i + 1)..)

            $rest_X | zip $rest_Y | zip $rest_Z | enumerate | each {|it|
                # Structure: [[x_j, y_j], z_j]
                let x_j = $it.item.0.0
                let y_j = $it.item.0.1
                let z_j = $it.item.1

                let dx = $xi - $x_j
                let dy = $yi - $y_j
                let dz = $zi - $z_j

                # Manual squaring is faster than math pow
                let w = ($dx * $dx) + ($dy * $dy) + ($dz * $dz)

                # j index reconstruction
                let j = $i + 1 + $it.index
                [$i, $j, $w]
            }
        } | flatten
    )
    let sorted_edges = ($edges | sort-by 2 0 1)

    # DSU Find Helper
    let find = {|i, p_list|
        mut root = $i
        while ($p_list | get $root) != $root {
            $root = ($p_list | get $root)
        }
        $root
    }

    # Part 1
    mut parent = (0..($n - 1) | each { $in })
    let limit = (if $n == 20 { 10 } else { $n })

    for k in 0..($limit - 1) {
        let edge = ($sorted_edges | get $k)
        let u = ($edge | get 0)
        let v = ($edge | get 1)

        let root_u = (do $find $u $parent)
        let root_v = (do $find $v $parent)

        if $root_u != $root_v {
            $parent = ($parent | upsert $root_v $root_u)
        }
    }

    let final_parent = $parent
    let counts = (
        0..($n - 1)
        | each {|i| do $find $i $final_parent }
        | uniq -c
        | sort-by count
        | last 3
        | get count
    )
    let p1 = ($counts | math product)

    # Part 2
    mut parent = (0..($n - 1) | each { $in })
    mut m = $n
    mut p2 = 0

    for edge in $sorted_edges {
        let u = ($edge | get 0)
        let v = ($edge | get 1)

        let root_u = (do $find $u $parent)
        let root_v = (do $find $v $parent)

        if $root_u != $root_v {
            $parent = ($parent | upsert $root_v $root_u)
            $m -= 1

            if $m == 1 {
                let p_u = ($points | get $u)
                let p_v = ($points | get $v)
                $p2 = ($p_u.0 * $p_v.0)
                break
            }
        }
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
