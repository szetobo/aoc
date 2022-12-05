(ns abagile.aoc.2022.day04
  (:require
   [clojure.string :as cs]))

(def input (->> (slurp "resources/2022/day04.txt") cs/split-lines
                (map #(->> % (re-seq #"\d+") (map read-string)))))

(defn fully?
  [[a b x y]]
  (or (<= a x y b) (<= x a b y)))

(defn part1
  []
  (time (->> input (filter fully?) count)))

(defn partial?
  [[a b x y]]
  (or (<= a x b) (<= x a y)))

(defn part2
  []
  (time (->> input (filter partial?) count)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
