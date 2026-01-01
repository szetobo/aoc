(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0 p2 0
      pos 50)

(setv D (.read sys.stdin)
      lines (gfor line (.split D "\n") :if line line))

(for [line lines]
  (let [d (if (= (get line 0) "L") -1 1)]
    (for [i (range (int (cut line 1 None)))]
      (setv pos (% (+ pos d) 100))
      (when (= 0 pos) (+= p2 1)))
    (when (= 0 pos) (+= p1 1))))

(print f"Part 1: {p1}")
(print f"Part 2: {p2}")
