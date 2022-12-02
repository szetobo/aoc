(ns abagile.aoc.2022.day02
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def input (->> (util/read-input-split-lines "2022/day02.txt")
                (map #(cs/split % #" "))))

(def shape-scores {"X" 1 "Y" 2 "Z" 3})

(def outcome-scores {"A" {"X" 3 "Y" 6 "Z" 0}
                     "B" {"X" 0 "Y" 3 "Z" 6}
                     "C" {"X" 6 "Y" 0 "Z" 3}})

(defn score-1
  [[x y]]
  (+ (get-in outcome-scores [x y]) (get shape-scores y)))

(defn part1
  []
  (time (->> input (map score-1) (reduce +))))

(def outcome-scores'
  (util/fmap (fn [m] (reduce-kv #(assoc %1 %3 %2) {} m)) outcome-scores))

(defn score-2
  [[x y]]
  (let [outcome-score (case y "X" 0 "Y" 3 "Z" 6)]
    (+ outcome-score
       (->> (get-in outcome-scores' [x outcome-score])
            (get shape-scores)))))

(defn part2
  []
  (time (->> input (map score-2) (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
