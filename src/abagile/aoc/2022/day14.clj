(ns abagile.aoc.2022.day14
  (:require
    [clojure.string :as cs]
    [clojure.test :refer [deftest is testing]]))

(defn range+ [x y]
  (if (<= x y) (range x (inc y)) (range y (inc x))))

(def input
  (->> "resources/2022/day14.txt" slurp cs/split-lines
       (map #(partition 2 (map read-string (re-seq #"\d+" %))))
       (reduce (fn [grid path]
                 (->> (partition 2 1 path)
                      (mapcat (fn [[[x y] [x' y']]]
                                (for [col (range+ x x') row (range+ y y')]
                                  [col row])))
                      (into grid)))
               #{})))

(defn part1
  []
  (let [floor (->> input (map first) set
                   (map (fn [i] [i (->> input (filter #(= (first %) i)) (map second) (apply max))]))
                   (into {}))]
    (loop [i 0 grid input x 500 y -1]
      (let [max-y (floor x)]
        (cond
          (or (nil? max-y) (> y max-y)) i
          (nil? (grid [x (inc y)]))       (recur i grid x (inc y))
          (nil? (grid [(dec x) (inc y)])) (recur i grid (dec x) (inc y))
          (nil? (grid [(inc x) (inc y)])) (recur i grid (inc x) (inc y))
          :else (recur (inc i) (conj grid [x y]) 500 -1))))))

(defn part2
  []
  (let [max-y (->> input (map second) (apply max) (+ 1))]
    (loop [i 0 grid input x 500 y -1]
      (cond
        (grid [500 0]) i
        (= y max-y) (recur (inc i) (conj grid [x y]) 500 -1)
        (nil? (grid [x (inc y)]))       (recur i grid x (inc y))
        (nil? (grid [(dec x) (inc y)])) (recur i grid (dec x) (inc y))
        (nil? (grid [(inc x) (inc y)])) (recur i grid (inc x) (inc y))
        :else (recur (inc i) (conj grid [x y]) 500 -1)))))

(defn -main [& _]
  (println "part 1:" (time (part1)))
  (println "part 2:" (time (part2))))

(comment
  (-main))

(deftest day-14
  (testing "part1"
    (is (= 1 1))))
