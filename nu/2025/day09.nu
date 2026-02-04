def valid [x: int, y: int, edges: any, memo: record] {
    let key = $"v:($x),($y)"
    if ($memo | get -i $key) != null {
        return {val: ($memo | get $key), memo: $memo}
    }

    mut cnt = 0
    for edge in $edges {
        # On segment checks
        if $x == $edge.x1 and $x == $edge.x2 and $y >= $edge.y1 and $y <= $edge.y2 {
            return {val: true, memo: ($memo | insert $key true)}
        }
        if $y == $edge.y1 and $y == $edge.y2 and $x >= $edge.x1 and $x <= $edge.x2 {
            return {val: true, memo: ($memo | insert $key true)}
        }

        # Ray casting
        if ($edge.oy1 > $y) != ($edge.oy2 > $y) {
            let intersect_x = ($edge.ox1 + ($edge.ox2 - $edge.ox1) * ($y - $edge.oy1) / ($edge.oy2 - $edge.oy1))
            if $x < $intersect_x {
                $cnt = $cnt + 1
            }
        }
    }
    let res = (($cnt mod 2) == 1)
    return {val: $res, memo: ($memo | insert $key $res)}
}

def intersect [ax: int, ay: int, bx: int, by: int, edges: any, memo: record] {
    let key = $"i:($ax),($ay),($bx),($by)"
    if ($memo | get -i $key) != null {
        return {val: ($memo | get $key), memo: $memo}
    }

    mut res = false
    for edge in $edges {
        if $edge.x1 == $edge.x2 {
            if $ax < $edge.x1 and $edge.x1 < $bx and $ay < $edge.y2 and $edge.y1 < $by {
                $res = true
                break
            }
        } else {
            if $ay < $edge.y1 and $edge.y1 < $by and $ax < $edge.x2 and $edge.x1 < $bx {
                $res = true
                break
            }
        }
    }
    return {val: $res, memo: ($memo | insert $key $res)}
}

def main [] {
    let coords = (cat | lines | where { $in != "" } | each { split row "," | into int })
    let n = ($coords | length)

    # Pre-calculate edges
    let edges = (0..<$n | each { |i|
        let p1 = ($coords | get $i)
        let p2 = ($coords | get (($i + 1) mod $n))
        let ox1 = ($p1 | get 0)
        let oy1 = ($p1 | get 1)
        let ox2 = ($p2 | get 0)
        let oy2 = ($p2 | get 1)
        {
            ox1: $ox1, oy1: $oy1, ox2: $ox2, oy2: $oy2,
            x1: (if $ox1 <= $ox2 { $ox1 } else { $ox2 }),
            x2: (if $ox1 <= $ox2 { $ox2 } else { $ox1 }),
            y1: (if $oy1 <= $oy2 { $oy1 } else { $oy2 }),
            y2: (if $oy1 <= $oy2 { $oy2 } else { $oy1 })
        }
    })

    mut p1 = 0
    mut p2 = 0
    mut memo = {}

    for i in 0..<$n {
        for j in ($i + 1)..<$n {
            let a = ($coords | get $i)
            let b = ($coords | get $j)

            let ax = ($a | get 0)
            let ay = ($a | get 1)
            let bx = ($b | get 0)
            let by = ($b | get 1)

            let x1 = if $ax <= $bx { $ax } else { $bx }
            let x2 = if $ax <= $bx { $bx } else { $ax }
            let y1 = if $ay <= $by { $ay } else { $by }
            let y2 = if $ay <= $by { $by } else { $ay }

            if $x1 == $x2 or $y1 == $y2 {
                continue
            }

            let area = ($x2 - $x1 + 1) * ($y2 - $y1 + 1)
            if $area > $p1 {
                $p1 = $area
            }

            if $area <= $p2 {
                continue
            }

            # Check corners with memoization
            mut all_valid = true
            let corners = [[$x1, $y1], [$x1, $y2], [$x2, $y1], [$x2, $y2]]
            for corner in $corners {
                let res = (valid ($corner | get 0) ($corner | get 1) $edges $memo)
                $memo = $res.memo
                if not $res.val {
                    $all_valid = false
                    break
                }
            }

            if $all_valid {
                let res = (intersect $x1 $y1 $x2 $y2 $edges $memo)
                $memo = $res.memo
                if not $res.val {
                     $p2 = $area
                }
            }
        }
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
