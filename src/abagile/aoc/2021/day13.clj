(ns abagile.aoc.2021.day13
  (:gen-class)
  (:require
    [abagile.aoc.ocr :as ocr]
    [abagile.aoc.util :as util]))

(def sample (util/read-input "2021/day13.sample.txt"))
(def input  (util/read-input "2021/day13.txt"))

(defn parse
  [s]
  [(->> s (re-seq #"(\d+),(\d+)") (map #(mapv read-string (rest %))))
   (->> s (re-seq #"fold along ([xy])=(\d+)") (map #(mapv read-string (rest %))))])

(defn fold
  [grid [axis v]]
  (set (map (fn [[x y]]
              (case axis
                x [(if (> x v) (- (* 2 v) x) x) y]
                y [x (if (> y v) (- (* 2 v) y) y)]))
         grid)))

(comment
  (count sample)
  (->> (parse sample)
       (apply reduce #(fold %1 %2)))
  (count input))

(defn part1
  []
  (time (let [[grid actions] (parse input)]
          (count (fold grid (first actions))))))

(defn part2
  []
  (time (->> (parse input)
             (apply reduce #(fold %1 %2))
             ocr/draw
             ocr/->str)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
