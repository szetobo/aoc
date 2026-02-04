def count_paths [node: string, graph: record, memo: record] {
    if ($memo | get -i $node) != null {
        return {val: ($memo | get $node), memo: $memo}
    }

    if $node == "out" {
        return {val: 1, memo: $memo}
    }

    let children = ($graph | get -i $node | default [])
    mut current_memo = $memo
    mut total = 0

    for child in $children {
        let res = (count_paths $child $graph $current_memo)
        $total += $res.val
        $current_memo = $res.memo
    }

    $current_memo = ($current_memo | insert $node $total)
    return {val: $total, memo: $current_memo}
}

def count_paths_p2 [node: string, fft: bool, dac: bool, graph: record, memo: record] {
    let key = $"($node);($fft);($dac)"

    if ($memo | get -i $key) != null {
        return {val: ($memo | get $key), memo: $memo}
    }

    if $node == "out" {
        let res = if $fft and $dac { 1 } else { 0 }
        return {val: $res, memo: $memo}
    }

    let children = ($graph | get -i $node | default [])
    mut current_memo = $memo
    mut total = 0

    for child in $children {
        let new_fft = ($fft or ($child == "fft"))
        let new_dac = ($dac or ($child == "dac"))

        let res = (count_paths_p2 $child $new_fft $new_dac $graph $current_memo)
        $total += $res.val
        $current_memo = $res.memo
    }

    $current_memo = ($current_memo | insert $key $total)
    return {val: $total, memo: $current_memo}
}

def main [] {
    let lines = (cat | lines | where { $in != "" })

    mut graph = {}

    for line in $lines {
        let parts = ($line | split row ":")
        let src = ($parts.0 | str trim)
        let dests = ($parts.1 | split row " " | where { $in != "" })
        $graph = ($graph | insert $src $dests)
    }

    let p1_res = (count_paths "you" $graph {})
    let p1 = $p1_res.val

    let p2_res = (count_paths_p2 "svr" false false $graph {})
    let p2 = $p2_res.val

    print $"Part 1: ($p1)"
    print $"Part 2: ($p2)"
}
