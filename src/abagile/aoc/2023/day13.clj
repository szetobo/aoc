(ns abagile.aoc.2023.day13
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def input (-> (util/read-input "2023/day13.txt")
               (cs/split #"\n\n")
               (as-> $ (map #(cs/split % #"\n") $))))

(defn h-idx
  [grid]
  (let [idxes (->> (partition 2 1 grid)
                   (map-indexed (fn [idx [a b]] [(inc idx) (= a b)]))
                   (filter last) (map first))]
    (for [idx idxes
          :when (every? true? (mapv = (->> grid (take idx) reverse) (->> grid (drop idx))))]
      idx)))

(defn v-idx
  [grid]
  (->> grid util/transpose h-idx))

(defn summarize
  ([grid] (summarize grid 0))
  ([grid orig]
   (->> (concat (map #(* % 100) (h-idx grid)) (v-idx grid))
        (remove #(= % orig))
        first)))

(defn part1
  []
  (time (->> input (map summarize)
             (reduce +))))


(defn sim-smudge
  [grid]
  (let [orig (summarize grid)]
    (-> (for [row (range (count grid))
              col (range (count (first grid)))
              :let [grid' (update-in grid [row col] {\. \# \# \.})
                    score (summarize grid' orig)]
              :when score]
          score)
        first)))


(defn part2
  []
  (time (->> input (map #(mapv (comp vec seq) %)) (map sim-smudge)
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
