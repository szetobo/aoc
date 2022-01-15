(ns abagile.aoc.2015.day12
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.data.json :as json]
    [clojure.test :refer [deftest is]]))

(def input (util/read-input "2015/day12.txt"))

(defn sum
  [v]
  (cond
    (integer? v) v
    (string? v)  0
    (vector? v)  (reduce + (map sum v))
    (map? v)     (let [vs (vals v)] (if (some #{"red"} vs) 0 (reduce + (map sum vs))))))

(defn part1
  []
  (time
    (->> input (re-seq #"-?\d+") (map read-string) (reduce +))))

(defn part2
  []
  (time
    (->> input json/read-str sum)))


(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
