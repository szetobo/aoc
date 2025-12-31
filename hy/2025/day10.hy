(import sys)
(import collections [defaultdict])
(import functools [cache])
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line (.split line)))

(for [[L #* B J] lines]
  (setv L (list (cut L 1 -1))
        B (lfor bs B (lfor b (.split (cut bs 1 -1) ",") (int b)))
        J (lfor j (.split (cut J 1 -1) ",") (int j))
        S (defaultdict list))
  (for [i (range (<< 1 (len B)))]
    (setv res (* [0] (len L))
          pressed (* [0] (len B)))
    (for [[btn bits] (enumerate B)]
      (when (= 1 (-> i (>> btn) (& 1)))
        (for [b bits] (^= (get res b) 1))
        (+= (get pressed btn) 1)))
    (.append (get S (.join "" (lfor x res (if (= x 1) "#" ".")))) pressed))

  (+= p1 (min (lfor s (get S (.join "" L)) (sum s))))

  (defn [cache] f2 [T]
    (when (all (gfor v T (= v 0))) (return 0))
    (setv pressed sys.maxsize
          lights (* ["."] (len T)))
    (for [[i joltage] (enumerate T)]
      (when (= 1 (& 1 joltage)) (setv (get lights i) "#")))
    (for [pre_pressed (get S (.join "" lights))]
      (setv joltage (list T))
      (for [[i v] (enumerate pre_pressed)]
        (when (= v 1) (for [b (get B i)] (-= (get joltage b) 1))))
      (when (any (gfor v joltage (< v 0))) (continue))
      (setv joltage (lfor v joltage (>> v 1))
            p (f2 (tuple joltage)))
      (when (!= p sys.maxsize)
        (setv pressed (min pressed (+ (sum pre_pressed) (* 2 p))))))
    pressed)

  (+= p2 (f2 (tuple J))))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
