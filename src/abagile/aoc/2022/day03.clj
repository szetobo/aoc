(ns abagile.aoc.2022.day03
  (:require
   [clojure.set :refer [intersection]]
   [clojure.string :as cs]))

(def input (->> (slurp "resources/2022/day03.txt") cs/split-lines (map seq)))

(defn char->priority
  [ch]
  (let [n (int ch)]
    (if (> n 96) (- n 96) (- n (- 64 26)))))

(defn part1
  []
  (time (->> input
             (map #(split-at (/ (count %) 2) %))
             (map #(->> % (map set) (apply intersection) first char->priority))
             (reduce +))))

(defn part2
  []
  (time (->> input
             (partition 3)
             (map #(->> % (map set) (apply intersection) first char->priority))
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
