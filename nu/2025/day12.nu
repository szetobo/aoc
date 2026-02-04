def main [] {
    let lines = (cat | lines | where { $in != "" })

    mut p1 = 0
    mut p2 = 0
    mut T = []

    for line in $lines {
        if ($line | str length) == 2 {
            $T = ($T | append 0)
        } else if ($line | str starts-with "#") or ($line | str starts-with ".") {
            let count = ($line | split row "" | where { $in == "#" } | length)
            let last_idx = ($T | length) - 1
            let current_val = ($T | get $last_idx)
            $T = ($T | update $last_idx ($current_val + $count))
        } else {
            let nums = ($line | str replace --all --regex "[^0-9]+" " " | split row " " | where { $in != "" } | each { into int })

            if ($nums | length) >= 2 {
                let width = ($nums | get 0)
                let height = ($nums | get 1)
                let rest = ($nums | skip 2)

                let ttl_t = $width * $height
                let min_p = ($width / 3.0) * ($height / 3.0)
                let cnt_p = ($rest | math sum)

                mut cnt_t = 0
                let rest_len = ($rest | length)
                for i in 0..<$rest_len {
                    let v = ($rest | get $i)
                    let t_val = ($T | get $i)
                    $cnt_t += ($v * $t_val)
                }

                if $cnt_p <= $min_p and $cnt_t <= $ttl_t {
                    $p1 += 1
                }
            }
        }
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
