(ns abagile.aoc.2021.day11
  (:gen-class)
  (:require
    [abagile.aoc.grid :as grid]
    [abagile.aoc.util :as util]))

(def sample (util/read-input "2021/day11.sample.txt"))
(def input  (util/read-input "2021/day11.txt"))

(defn play
  [dim grid]
  (loop [grid (util/fmap inc grid)]
    (let [flash-cells (map key (filter #(> (val %) 9) grid))
          adjacents   (frequencies (mapcat #(util/adjacent-8 dim %) flash-cells))
          grid        (util/fmap #(if (> % 9) ##-Inf %) grid)]
      (if (seq flash-cells)
        (recur (util/fmap-kv #(+ %2 (get adjacents %1 0)) grid))
        (util/fmap #(if (= % ##-Inf) 0 %) grid)))))

(def play* #(partial play %))

(comment
  (count sample)
  (meta (grid/parse sample))
  (grid/adjacent-8 [10 10] [8 8])
  (count input)
  (meta (grid/parse sample)))

(defn part1
  []
  (time (let [grid (grid/parse input) dim (:dim (meta grid))]
          (->> (iterate (play* dim) grid) (drop 1)
               (take 100) (map #(->> (vals %) (filter zero?) count)) (reduce +)))))

(defn part2
  []
  (time (let [grid (grid/parse input) dim (:dim (meta grid))]
          (->> (iterate (play* dim) grid) (drop 1)
               (reduce (fn [step grid]
                         (if (every? zero? (vals grid)) (reduced step) (inc step)))
                 1)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
