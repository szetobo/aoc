(import sys)
(import math [prod])
(import collections [defaultdict])
(import itertools [combinations])
(import operator [itemgetter])
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line (tuple (lfor item (.split line ",") (int item))))
      n (len lines)
      E (lfor [[ax ay az] [bx by bz]] (combinations lines 2)
         [#(ax ay az) #(bx by bz) (+ (** (- ax bx) 2) (** (- ay by) 2) (** (- az bz) 2))]))
(.sort E :key (itemgetter 2))

(defclass DSU []

  (defn __init__ [self]
    (setv self.parent {}))

  (defn p [self] self.parent)

  (defn find [self i]
    (when (not-in i self.parent)
      (setv (get self.parent i) i)
      (return i))
    (setv p (get self.parent i))
    (when (!= i p) (setv (get self.parent i) (self.find p)))
    (get self.parent i))

  (defn union [self i j]
    (setv root_i (self.find i)
          root_j (self.find j))
    (when (!= root_i root_j)
      (setv (get self.parent root_j) root_i)
      (return True))
    (return False)))

(setv dsu (DSU))
(for [i (range (if (= n 20) 10 n))] (let [[a b _] (get E i)] (dsu.union a b)))
(setv M (defaultdict int))
(for [line lines]
  (let [root (dsu.find line)]
    (+= (get M root) 1)))
(setv p1 (-> (.values M) sorted (cut -3 None) prod))

(setv dsu (DSU)
      m n)
(for [[a b _] E]
   (when (dsu.union a b)
     (-= m 1)
     (when (= m 1)
       (setv p2 (* (get a 0) (get b 0)))
       (break))))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
