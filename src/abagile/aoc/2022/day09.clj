(ns abagile.aoc.2022.day09
  (:require
   [clojure.string :as cs]))

(def input
  (->> "resources/2022/day09.txt" slurp cs/split-lines
       (map #(mapcat rest (re-seq #"([UDLR]) (\d+)$" %)))
       (map (fn [[d n]] [(keyword d) (read-string n)]))))

(defn move-knots
  [[hx hy] [x y]]
  (if (or (> (Math/abs (- hx x)) 1) (> (Math/abs (- hy y)) 1))
    [(+ x (compare hx x)) (+ y (compare hy y))]
    [x y]))

(defn move
  [n]
  (fn [knots d]
    (reduce #(update %1 %2 (partial move-knots (get %1 (dec %2))))
            (update knots 0 (fn [[x y]]
                              (case d
                                :U [x (inc y)]
                                :D [x (dec y)]
                                :L [(dec x) y]
                                :R [(inc x) y])))
            (range 1 n))))

(defn part1
  []
  (->> input
       (mapcat (fn [[d n]] (repeat n d)))
       (reductions (move 2) (vec (repeat 2 [0 0])))
       (map last) (into #{}) count))

(defn part2
  []
  (->> input
       (mapcat (fn [[d n]] (repeat n d)))
       (reductions (move 10) (vec (repeat 10 [0 0])))
       (map last) (into #{}) count))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
