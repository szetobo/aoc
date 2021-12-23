(ns abagile.aoc.2021.day21
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def input  (util/read-input "2021/day21.txt"))

(defn parse
  [s]
  (->> s (re-seq #"(\d)\n") (map #(read-string (second %)))))

(defn game1
  [target p1 p2]
  (loop [n 0 rolls (cycle (util/range+ 1 100))
         p1 p1 s1 0 p2 p2 s2 0]
    (let [p1 (inc (mod (dec (apply + p1 (take 3 rolls))) 10))
          s1 (+ s1 p1)
          n  (+ n 3)]
      (if (>= s1 target)
        (* s2 n)
        (recur n (drop 3 rolls) p2 s2 p1 s1)))))

(def game2
  (memoize
    (fn
      ([target p1 p2] (game2 target p1 0 p2 0))
      ([target p1 t1 p2 t2]
       (if (>= t2 target)
         [0 1]
         (reduce (fn [[w1 w2] roll]
                   (let [p         (inc (mod (dec (+ p1 roll)) 10))
                         [w2' w1'] (game2 target p2 t2 p (+ t1 p))]
                     [(+ w1 w1') (+ w2 w2')]))
           [0 0]
           (for [a [1 2 3] b [1 2 3] c [1 2 3]] (+ a b c))))))))

(comment
  (count input)
  (apply game1 1000 [4 8])
  (apply game2 21 [4 8]))

(defn part1
  []
  (time
    (apply game1 1000 (parse input))))

(defn part2
  []
  (time
    (apply game2 21 (parse input))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
