(ns abagile.aoc.2015.day17
  (:gen-class)
  (:require
    [clojure.test :refer [deftest is]]
    [clojure.math.combinatorics :as comb]))

(def input [11 30 47 31 32 36 3 1 5 3 32 36 15 11 46 26 28 1 19 3])

(def sample [20 15 10 5 5])

(defn selections [data ttl]
  (->> (map (fn [l1 l2] (when (= ttl (reduce + (map #(* %1 %2) l1 l2))) l2))
            (repeatedly (constantly data))
            (comb/selections [0 1] (count data)))
       (filter some?)))

(defn min-selections [data ttl]
  (->> (selections data ttl)
       (map #(reduce + %))
       sort
       (partition-by identity)
       first
       count))

(defn part1 []
  (time (count (selections input 150))))

(defn part2 []
  (time (min-selections input 150)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= (count (selections sample 25)) 4))
  (is (= (min-selections sample 25) 3)))
