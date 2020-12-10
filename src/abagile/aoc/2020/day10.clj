(ns abagile.aoc.2020.day10
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.math.combinatorics :refer [combinations]]
    [clojure.string :as cs]))

(def sample1 [16 10 15 5 1 11 7 19 6 12 4])
(def sample2 [28 33 18 42 31 14 46 20 48 47 24 23 49 45 19 38 39 11 1 32 25 35 8 17 7 9 4 2 34 10 3])

(comment
  (-> (map read-string (cs/split-lines (slurp (io/resource "day10.txt"))))
      (as-> $ {:min (apply min $)
               :max (apply max $)
               :count (count $)})
      (as-> $ (assoc $ :rating (+ 3 (:max $))))))

(def ways
  (memoize (fn [n coll]
            (let [cnt (count coll)]
              (if (= n (dec cnt))
                1
                (reduce + (for [j (range (inc n) (min cnt (+ n 4)))
                                :when (<= (- (coll j) (coll n)) 3)]
                            (ways j coll))))))))

(def sample3 (into [0] (concat (sort sample2) [(+ 3 (apply max sample2))])))

(comment
  (ways 0 sample1)
  (ways 0 sample3))

(defn -main [& _]
  (println "part 1:"
           (-> (map read-string (cs/split-lines (slurp (io/resource "day10.txt"))))
               (as-> $ (concat $ [0 (+ 3 (apply max $))]))
               sort
               vec
               (#(reduce (fn [{:keys [d1 d3]} [x y]]
                           {:d1 (if (= (- y x) 1) (inc d1) d1)
                            :d3 (if (= (- y x) 3) (inc d3) d3)})
                         {:d1 0 :d3 0}
                         (partition 2 1 %)))
               (#(reduce * (vals %)))))
               ; (#(let [cnt (count %)]
               ;     (loop [d1 0 d3 0 i 0]
               ;       (if (>= i (dec cnt))
               ;         (* d1 d3)
               ;         (let [diff (- (% (inc i)) (% i))]
               ;           (recur (if (= diff 1) (inc d1) d1)
               ;                  (if (= diff 3) (inc d3) d3)
               ;                  (inc i)))))))))

  (println "part 2:" (-> (map read-string (cs/split-lines (slurp (io/resource "day10.txt"))))
                         (as-> $ (concat $ [0 (+ 3 (apply max $))]))
                         sort
                         vec
                         (#(ways 0 %)))))
