(ns abagile.aoc.2022.day01
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def input (->> (util/read-input-split "2022/day01.txt" #"\n\n")
                (map #(map util/parse-int (cs/split % #"\n")))))

(defn part1
  []
  (time (->> input (map #(apply + %)) (apply max))))

(defn part2
  []
  (time (->> input (map #(apply + %)) (sort >) (take 3) (apply +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
