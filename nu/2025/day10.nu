def solve_lights [target: string, buttons_map: any] {
    # target is string like "#.#", buttons_map is list of list of pressed indices
    # We want min buttons to reach target from "..." (all 0)
    # This is equivalent to finding a combination of buttons that XOR to target
    # Since n_buttons is small, we iterate 2^N

    let n_buttons = ($buttons_map | length)
    let n_lights = ($target | str length)
    let limit = (1 bit-shl $n_buttons)
    mut min_pressed = 1000000000 # sys.maxsize equivalent

    for i in 0..<$limit {
        mut current_lights = (0..<$n_lights | each { 0 })
        mut current_pressed_count = 0

        for btn_idx in 0..<$n_buttons {
            if (($i bit-shr $btn_idx) bit-and 1) == 1 {
                $current_pressed_count += 1
                let affected = ($buttons_map | get $btn_idx)
                for pos in $affected {
                    let old_val = ($current_lights | get $pos)
                    let new_val = ($old_val bit-xor 1)
                    $current_lights = ($current_lights | update $pos $new_val)
                }
            }
        }

        let pattern = ($current_lights | each { if $in == 1 { "#" } else { "." } } | str join)
        if $pattern == $target {
             if $current_pressed_count < $min_pressed {
                 $min_pressed = $current_pressed_count
             }
        }
    }
    return $min_pressed
}

# Helper to precompute reachability for Part 2 logic
def get_reachable [buttons_map: any, n_lights: int] {
    let n_buttons = ($buttons_map | length)
    let limit = (1 bit-shl $n_buttons)
    mut reachable = {}

    for i in 0..<$limit {
        mut current_lights = (0..<$n_lights | each { 0 })
        mut pressed = (0..<$n_buttons | each { 0 })

        for btn_idx in 0..<$n_buttons {
            if (($i bit-shr $btn_idx) bit-and 1) == 1 {
                $pressed = ($pressed | update $btn_idx 1)
                let affected = ($buttons_map | get $btn_idx)
                for pos in $affected {
                    let old_val = ($current_lights | get $pos)
                    $current_lights = ($current_lights | update $pos ($old_val bit-xor 1))
                }
            }
        }
        let pattern = ($current_lights | each { if $in == 1 { "#" } else { "." } } | str join)

        let existing = ($reachable | get -i $pattern | default [])
        $reachable = ($reachable | upsert $pattern ($existing | append [$pressed]))
    }
    $reachable
}

def solve_part2 [joltages: list<int>, reachable: record, buttons_map: any, memo: record] {
    let key = ($joltages | each { $in | into string } | str join ",")
    if ($memo | get -i $key) != null {
        return {val: ($memo | get $key), memo: $memo}
    }

    if ($joltages | all { $in == 0 }) {
        return {val: 0, memo: $memo}
    }

    # Derive target lights from joltages (lsb)
    let lights = ($joltages | each { if ($in bit-and 1) == 1 { "#" } else { "." } } | str join)

    let possible_moves = ($reachable | get -i $lights)
    if ($possible_moves == null) {
        return {val: 1000000000, memo: $memo}
    }

    mut min_res = 1000000000
    mut current_memo = $memo

    for move in $possible_moves {
        mut next_joltages = $joltages
        let move_len = ($move | length)

        for i in 0..<$move_len {
            if ($move | get $i) == 1 {
                let affected = ($buttons_map | get $i)
                for b in $affected {
                    let old = ($next_joltages | get $b)
                    $next_joltages = ($next_joltages | update $b ($old - 1))
                }
            }
        }

        if ($next_joltages | any { $in < 0 }) {
            continue
        }

        # joltage = [v >> 1 for v in joltage]
        $next_joltages = ($next_joltages | each { $in bit-shr 1 })

        let res = (solve_part2 $next_joltages $reachable $buttons_map $current_memo)
        $current_memo = $res.memo
        let p = $res.val

        if $p != 1000000000 {
            let move_cost = ($move | math sum)
            let total = $move_cost + 2 * $p
            if $total < $min_res {
                $min_res = $total
            }
        }
    }

    $current_memo = ($current_memo | insert $key $min_res)
    return {val: $min_res, memo: $current_memo}
}

def main [] {
    let lines = (cat | lines | where { $in != "" })

    mut p1 = 0
    mut p2 = 0

    for line in $lines {
        # Format: [lights] [btn1] [btn2] ... [joltages]
        # Example: [#.#] [0,1] [1] [10,20]
        let parts = ($line | split row " ")
        let lights_part = ($parts | first)
        let joltages_part = ($parts | last)
        let buttons_parts = ($parts | skip 1 | drop 1)

        # Parse lights: remove [ and ]
        let lights_str = ($lights_part | str substring 1..-2)

        # Parse joltages: remove [ and ], split comma
        let joltages = ($joltages_part | str substring 1..-2 | split row "," | each { into int })

        # Parse buttons
        let buttons = ($buttons_parts | each { |b|
            $b | str substring 1..-2 | split row "," | each { into int }
        })

        # Part 1
        let reach = (get_reachable $buttons ($lights_str | str length))
        let ways = ($reach | get -i $lights_str | default [])

        # Find min weight in ways
        mut local_p1 = 1000000000
        for w in $ways {
            let s = ($w | math sum)
            if $s < $local_p1 { $local_p1 = $s }
        }
        if $local_p1 != 1000000000 {
            $p1 += $local_p1
        }

        # Part 2
        let res = (solve_part2 $joltages $reach $buttons {})
        let local_p2 = $res.val
        if $local_p2 != 1000000000 {
            $p2 += $local_p2
        }
    }

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
