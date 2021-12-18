(ns abagile.aoc.2021.day17
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample (util/read-input "2021/day17.sample.txt"))
(def input  (util/read-input "2021/day17.txt"))

(defn parse
  [s]
  (->> s (re-find #"x=([0-9]+)\.\.([0-9]+), y=([-]?[0-9]+)\.\.([-]?[0-9]+)") rest (map read-string)))

(comment
  (count sample)
  (parse sample)
  (count input)
  (parse input))

(defn part1
  []
  (time
    (let [[x1 x2 y1 y2] (parse input)
          max-x (max x1 x2) min-x (min x1 x2)
          max-y (max y1 y2) min-y (min y1 y2)]
      (->> (for [y (util/range+ (- y1) (- y2))
                 x (util/range+ (int (Math/sqrt min-x)) (inc max-x))
                 :when (and
                         (<= min-x (/ (* x (inc x)) 2) max-x)
                         (<= min-y (apply + (util/range+ y (- (inc y)))) max-y))]
             [(apply + (util/range+ y 1)) [x y]])
           (sort-by first >) ffirst))))

(defn part2
  []
  (time
    (let [[x1 x2 y1 y2] (parse input)
          max-x (max x1 x2) min-x (min x1 x2)
          max-y (max y1 y2) min-y (min y1 y2)]
      (->> (for [y (util/range+ min-y (- min-y))
                 x (util/range+ max-x 0)]
              (loop [x' x y' y]
                (let [sum-x (reduce + (util/range+ x x'))
                      sum-y (reduce + (util/range+ y y'))]
                  (cond
                    (and (<= min-x sum-x max-x) (<= min-y sum-y max-y)) [x y]
                    (or (> sum-x max-x) (< sum-y min-y)) nil
                    :else (recur (max (dec x') 0) (dec y'))))))
           (remove nil?)
           count))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
