(ns abagile.aoc.2023.day06
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def input (util/read-input-split "2023/day06.txt" #"\n"))

(defn dist-travel
  [dur hold]
  (* hold (- dur hold)))

(comment
  (let [dur 7]
    (for [t (range (inc dur))]
      (dist-travel dur t))))

(defn wins
  [dur dist]
  (for [t (range (inc dur))
        :let [dist' (dist-travel dur t)]
        :when (> dist' dist)]
    [t dist']))

(comment
  (apply wins [40 215])
  (apply wins [79 1005]))

(defn part1
  []
  (time (->> input
             (map #(map read-string (re-seq #"\d+" %)))
             (apply map vector)
             (map #(count (apply wins %)))
             (reduce *))))

(defn part2
  []
  (time (let [[dur dist] (->> (cs/replace input " " "")
                              (re-seq #"\d+")
                              (map read-string))
              win-threshold (ffirst (wins dur dist))]
          (- (inc dur) (* 2 win-threshold)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
