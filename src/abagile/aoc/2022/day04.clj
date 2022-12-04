(ns abagile.aoc.2022.day04
  (:require
   [clojure.string :as cs]))

(def input (->> (slurp "resources/2022/day04.txt") cs/split-lines
                (map #(->> % (re-seq #"\d+") (map read-string)))))

(defn fully-overlapped
  [[a b x y]]
  (or (and (<= a x b) (<= a y b))
      (and (<= x a y) (<= x b y))))

(defn part1
  []
  (time (->> input (filter fully-overlapped) count)))

(defn partial-overlapped
  [[a b x y]]
  (or (<= a x b) (<= a y b)
      (<= x a y) (<= x b y)))

(defn part2
  []
  (time (->> input (filter partial-overlapped) count)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
