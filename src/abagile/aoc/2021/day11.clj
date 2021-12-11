(ns abagile.aoc.2021.day11
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample-input (util/read-input "2021/day11.sample.txt"))

(def input (util/read-input "2021/day11.txt"))

;; (defn parse [s] (->> (cs/split s #"\n") (map seq) (map (fn [i] (mapv #(- (int %) (int \0)) i)))))

(defn parse
  [xs s]
  (->> s (re-seq #"\d") (map read-string)
       (map-indexed #(vector [(int (/ %1 xs)) (mod %1 xs)] %2))
       (into {})))

(defn prn-grid
  [xs grid]
  (->> grid (into (sorted-map)) vals (partition xs)))

(defn adjacent-cells
  [[xs ys] [x y]]
  (for [[dx dy] [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]]
        :let [x' (+ x dx) y' (+ y dy)]
        :when (and (> xs x' -1) (> ys y' -1))]
    [x' y']))

(defn play
  [gs grid]
  (loop [before grid after (util/fmap inc grid)]
    (let [flash-cells (map key (filter (fn [[cell energy]] (and (> energy 9) (<= (before cell) 9))) after))
          adjacents   (reduce #(apply conj %1 (adjacent-cells gs %2)) [] flash-cells)]
      (if (seq flash-cells)
        (recur after (reduce (fn [grid' cell] (update grid' cell inc)) after adjacents))
        (reduce (fn [grid' [cell energy]] (assoc grid' cell (if (> energy 9) 0 energy))) after after)))))

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
