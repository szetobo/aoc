(ns abagile.aoc.2021.day11
  (:gen-class)
  (:require
    [abagile.aoc.grid :as grid]
    [abagile.aoc.util :as util]))

(def sample (util/read-input "2021/day11.sample.txt"))
(def input  (util/read-input "2021/day11.txt"))

(defn play
  [nbrs-fn octopuses]
  (loop [octopuses (util/fmap inc octopuses)]
    (let [flash-cells (map key (filter #(> (val %) 9) octopuses))
          nbrs-freq   (frequencies (mapcat nbrs-fn flash-cells))
          octopuses   (util/fmap #(if (> % 9) ##-Inf %) octopuses)]
      (if (seq flash-cells)
        (recur (util/fmap-kv #(+ %2 (get nbrs-freq %1 0)) octopuses))
        (util/fmap #(if (= % ##-Inf) 0 %) octopuses)))))

(comment
  (count sample)
  (meta (grid/parse sample))
  (count input)
  (meta (grid/parse input)))

(defn part1
  []
  (time
    (let [octopuses (grid/parse input)
          nbrs-fn   #(grid/adjacent-8 (grid/bounded octopuses) %)]
      (->> (iterate (partial play nbrs-fn) octopuses) (drop 1)
           (take 100) (map #(->> (vals %) (filter zero?) count)) (reduce +)))))

(defn part2
  []
  (time
    (let [octopuses (grid/parse input)
          nbrs-fn   #(grid/adjacent-8 (grid/bounded octopuses) %)]
      (->> (iterate (partial play nbrs-fn) octopuses) (drop 1)
           (reduce (fn [step grid]
                     (if (every? zero? (vals grid)) (reduced step) (inc step)))
             1)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
