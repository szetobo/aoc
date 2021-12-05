(ns abagile.aoc.2021.day05
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample-input (util/read-input-split-lines "2021/day05.sample.txt"))

(def input (util/read-input-split-lines "2021/day05.txt"))

(defn parse
  [s]
  (->> (cs/split s #" -> ")
       (map #(map read-string (cs/split % #",")))))

(defn straight? [[[y x] [y' x']]] (or (= y y') (= x x')))

(defn draw
  [board [[y x] [y' x']]]
  (let [ry (if (= y y') (repeat y) (util/range+ y y'))
        rx (if (= x x') (repeat x) (util/range+ x x'))]
    (reduce (fn [m [y x]] (update m [y x] (fnil inc 0))) board (map list ry rx))))

(comment
  (count sample-input)
  (take 10 sample-input)
  (count input)
  (take 10 input)
  (->> (map parse sample-input) (filter straight?))
  (draw (sorted-map) [[0 0] [0 9]])
  (-> (sorted-map) (draw [[0 0] [0 9]]) (draw [[0 3] [3 3]]))
  (->> (map parse sample-input)
       (reduce draw (sorted-map))
       vals
       (filter #(> % 1))
       count))

(defn part1
  []
  (time (->> (map parse input) (filter straight?) (reduce draw {}) (filter #(> (val %) 1)) count)))

(defn part2
  []
  (time (->> (map parse input) (reduce draw {}) (filter #(> (val %) 1)) count)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
