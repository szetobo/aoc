(import sys)
(import functools [cache])
(import itertools [combinations])
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin))
(setv lines (lfor line (.split D "\n") :if line
                  (list (map int (.split line ",")))))

(defn [cache] valid [x y]
  (setv cnt 0)
  (for [[i [x1 y1]] (enumerate lines)]
    (setv [x2 y2] (get lines (% (+ i 1) (len lines)))
          [x1 x2] (if (<= x1 x2) [x1 x2] [x2 x1])
          [y1 y2] (if (<= y1 y2) [y1 y2] [y2 y1]))
    (when (and (= x x1 x2) (<= y1 y y2)) (return True))
    (when (and (= y y1 y2) (<= x1 x x2)) (return True))
    (when (and (!= (> y1 y) (> y2 y))
               (< x (+ x1 (/ (* (- x2 x1) (- y y1)) (- y2 y1)))))
        (+= cnt 1)))
  (= (% cnt 2) 1))

(defn [cache] intersect [a b]
  (setv [ax ay] a
        [bx by] b)
  (for [[i [x1 y1]] (enumerate lines)]
    (setv [x2 y2] (get lines (% (+ i 1) (len lines))))
    (setv [x1 x2] (if (<= x1 x2) [x1 x2] [x2 x1]))
    (setv [y1 y2] (if (<= y1 y2) [y1 y2] [y2 y1]))
    (if (= x1 x2)
      (when (and (< ax x1 bx) (< ay y2) (< y1 by))
        (return True))
      (when (and (< ay y1 by) (< ax x2) (< x1 bx))
        (return True))))
  False)

(for [[[x1 y1] [x2 y2]] (combinations lines 2)]
  (setv [x1 x2] (if (<= x1 x2) [x1 x2] [x2 x1])
        [y1 y2] (if (<= y1 y2) [y1 y2] [y2 y1]))
  (when (or (= x1 x2) (= y1 y2)) (continue))
  (setv area (* (+ (- x2 x1) 1) (+ (- y2 y1) 1))
        p1 (max area p1))

  (when (<= area p2) (continue))
  (when (and (valid x1 y1)
             (valid x1 y2)
             (valid x2 y1)
             (valid x2 y2)
             (not (intersect #(x1 y1) #(x2 y2))))
    (setv p2 (max area p2))))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
