(ns abagile.aoc.2023.dayXX
  (:require
   [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split "2023/dayXX.txt" #"\n")))

(defn part1
  []
  (time (->> input)))


(defn part2
  []
  (time (->> input)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
