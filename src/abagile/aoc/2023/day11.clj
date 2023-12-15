(ns abagile.aoc.2023.day11
  (:require
   [abagile.aoc.util :as util]
   [clojure.math.combinatorics :as comb]))

(def sample (->> (util/read-input-split "2023/day11-sample.txt" #"\n")
                 (map seq)))
(def input (->> (util/read-input-split "2023/day11.txt" #"\n")
                (map seq)))

(defn expand-row
  [grid]
  (->> (for [row grid]
         (cond-> [row] (every? #{\.} row) (conj row)))
       (apply concat)))

(defn expand
  [grid]
  (->> grid expand-row util/transpose expand-row util/transpose))

(defn draw
  [grid]
  (map #(apply str %) grid))

(defn get-galaxy-cors
  [grid]
  (->> grid
       (map-indexed
        (fn [r-idx row]
          (map-indexed
           (fn [c-idx elm] [[r-idx c-idx] elm])
           row)))
       (apply concat)
       (filter #(->> % last #{\#}))
       (map first)))
(comment
  (->> sample draw)
  (->> sample expand draw)
  (->> sample expand get-galaxy-cors)
  (->> input draw)
  (->> input expand draw))

(defn part1
  []
  (time (let [galaxies (->> input expand get-galaxy-cors)]
           (->> (comb/combinations galaxies 2)
                (map #(apply util/manhattan-distance %))
                (reduce +)))))


(defn part2
  []
  (time (->> input)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
