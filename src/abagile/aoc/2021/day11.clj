(ns abagile.aoc.2021.day11
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample-input (util/read-input "2021/day11.sample.txt"))

(def input (util/read-input "2021/day11.txt"))

(defn parse
  [xs s]
  (->> s (re-seq #"\d") (map read-string) (util/parse-grid xs)))

(defn adjacent-cells
  [[xs ys] [x y]]
  (for [[dx dy] [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]]
        :let [x' (+ x dx) y' (+ y dy)]
        :when (and (> xs x' -1) (> ys y' -1))]
    [x' y']))

(defn play
  [gs grid]
  (loop [grid (util/fmap inc grid)]
    (let [flash-cells (map key (filter #(> (val %) 9) grid))
          adjacents   (frequencies (mapcat #(adjacent-cells gs %) flash-cells))
          grid        (util/fmap #(if (> % 9) ##-Inf %) grid)]
      (if (seq flash-cells)
        (recur (util/fmap-kv #(+ %2 (get adjacents %1 0)) grid))
        (util/fmap #(if (= % ##-Inf) 0 %) grid)))))

(def play* #(partial play %))

(defn part1*
  [[xs _ :as gs] input]
  (->> input (parse xs) (iterate (play* gs)) (drop 1)
       (take 100) (map #(->> (vals %) (filter zero?) count)) (reduce +)))

(defn part2*
  [[xs _ :as gs] input]
  (->> input (parse xs) (iterate (play* gs)) (drop 1)
       (reduce (fn [step grid]
                 (if (every? zero? (vals grid)) (reduced step) (inc step)))
         1)))

(comment
  (count sample-input)
  (adjacent-cells [10 10] [8 8])
  (part1* [10 10] sample-input)
  (->> sample-input (parse 10)
       (iterate (play* [10 10]))
       (map #(->> (vals %) (filter zero?) count))
       (drop 1)
       (take 100)
       (reduce +))
  (part2* [10 10] sample-input)
  (count input)
  (->> sample-input (parse 10)))

(defn part1
  []
  (time (part1* [10 10] input)))

(defn part2
  []
  (time (part2* [10 10] input)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
