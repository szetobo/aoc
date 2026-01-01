(import sys)
(import itertools [count product])
(import hyrule *)
(require hyrule *)

(setv p1 0 p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line (list line)))

(defn adjacent8 [c limit]
  (setv [row col] c
        offsets [[-1 -1] [-1 0] [-1 1]
                 [0 -1]         [0 1]
                 [1 -1]  [1 0]  [1 1]])
  (lfor [dr dc] offsets
    :setv nr (+ row dr)
    :setv nc (+ col dc)
    :if (and (<= 0 nr (dec limit)) (<= 0 nc (dec limit)))
    #(nr nc)))

(setv n (len lines))
(for [i (count 0)]
  (setv done p2)
  (for [[row col] (product (range n) (range n))]
    (when (= "@" (get lines row col))
      (when (< (sum (lfor [r c] (adjacent8 [row col] n) :if (= "@" (get lines r c)) 1)) 4)
        (if (= i 0)
          (+= p1 1)
          (do (setv (get lines row col) "x")
              (+= p2 1))))))
  (when (and (> i 0) (= done p2))
    (break)))

(print f"Part 1: {p1}")
(print f"Part 2: {p2}")
