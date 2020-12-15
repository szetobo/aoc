(ns abagile.aoc.2020.day6
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as s]
    [clojure.string :as cs]))

(def input (->> (util/read-input-split "2020/day6.txt" #"\n\n")
                (map cs/split-lines)))

(defn part1 []
  (->> input
       ; (take 3)
       (map #(set (apply str %)))
       (map count)
       (reduce +)))

(defn part2 []
  (->> input
       ; (take 10)
       (map #(map set %))
       (map #(apply s/intersection %))
       (map count)
       (reduce +)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
