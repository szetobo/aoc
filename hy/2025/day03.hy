(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0 p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line))

(defn pick [s n]
  (setv res (* ["0"] n)
        sta 0)
  (for [i (range n 0 -1)]
    (setv [p d] [sta "0"])
    (for [j (range sta (inc (- (len s) i)))]
       (when (> (setx v (get s j)) d)
         (setv [p d] [j v])))
    (setv (get res (- n i)) d
          sta (inc p)))
  (int (.join "" (lfor d res (str d)))))

(for [line lines]
  (+= p1 (pick line 2))
  (+= p2 (pick line 12)))

(print f"Part 1: {p1}")
(print f"Part 2: {p2}")
