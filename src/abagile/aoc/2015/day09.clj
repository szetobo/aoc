(ns abagile.aoc.2015.day09
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.math.combinatorics :as comb]
    [clojure.test :refer [deftest is]]))

(def input (util/read-input-split "2015/day09.txt" #"\n"))

(defn parse
  [s]
  (->> s (map #(re-seq #"(?:\w+|\d+)" %))
       (map (fn [[c1 _ c2 d]] [(keyword c1) (keyword c2) (read-string d)]))
       (reduce (fn [res [c1 c2 d]]
                 (-> res
                     (assoc-in [c1 c2] d)
                     (assoc-in [c2 c1] d)))
         {})))

(defn ttl-dst
  [nbr-dsts paths]
  (->> (partition 2 1 paths) (map #(get-in nbr-dsts %)) (reduce +)))

(defn part1
  []
  (time
    (let [nbr-dsts (parse input)]
      (apply min (map (partial ttl-dst nbr-dsts) (comb/permutations (keys nbr-dsts)))))))

(defn part2
  []
  (time
    (let [nbr-dsts (parse input)]
      (apply max (map (partial ttl-dst nbr-dsts) (comb/permutations (keys nbr-dsts)))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
