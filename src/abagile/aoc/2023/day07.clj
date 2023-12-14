(ns abagile.aoc.2023.day07
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def scores {\2 2 \3 3 \4 4 \5 5 \6 6 \7 7 \8 8 \9 9 \T 10 \J 11 \Q 12 \K 13 \A 14})

(defn type-rank
  [hand]
  (let [m (frequencies hand)]
    (case (sort (vals m))
      ((5))         5
      ((1 4))       4
      ((2 3))       3.5
      ((1 1 3))     3
      ((1 2 2))     2
      ((1 1 1 2))   1
      ((1 1 1 1 1)) 0)))

(comment
  (type-rank [11 5 10 9 3])
  (type-rank [10 6 8 14 14])
  (type-rank [3 14 3 2 14])
  (type-rank [7 14 7 7 10])
  (type-rank [7 10 7 7 10])
  (type-rank [7 7 7 7 10])
  (type-rank [7 7 7 7 7]))

(def input (->> (util/read-input-split "2023/day07.txt" #"\n")
                (map #(let [[hand bid] (cs/split % #" ")]
                        [(vec hand) (read-string bid)]))))

(defn part1
  []
  (time (->> input
             (mapv #(let [[hand bid] %]
                      [(type-rank hand) (mapv scores hand) bid]))
             sort
             (map-indexed #(* (inc %1) (last %2)))
             (reduce +))))

(defn wildcards-rank
  [hand]
  (let [m    (frequencies hand)
        js   (get m \J)
        rank (type-rank hand)]
    (if js
     (case rank
       (3.5 4 5) 5
       3         4
       2         (if (= js 1) 3.5 4)
       1         3
       0         1)
     rank)))

(type-rank [\T \3 \J \K \A])
(wildcards-rank [\T \3 \J \K \A])

(defn part2
  []
  (time (->> input
             (mapv #(let [[hand bid] %]
                      [(wildcards-rank hand) (mapv (assoc scores \J 1) hand) bid]))
             sort
             (map-indexed #(* (inc %1) (last %2)))
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
