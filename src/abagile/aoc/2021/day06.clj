(ns abagile.aoc.2021.day06
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample-input (util/read-input "2021/day06.sample.txt"))

(def input (util/read-input "2021/day06.txt"))

(defn parse
  [s]
  (let [freq (->> (re-seq #"\d+" s) (map read-string) frequencies)]
    (mapv #(get freq % 0) (range 9))))

(defn sim
  [days fish]
  (reduce (fn [[fst & tail], _] (conj (update (vec tail) 6 + fst) fst)) fish (range days)))

(comment
  (parse sample-input)
  (apply + (sim 80 (parse sample-input))))

(defn part1
  []
  (time (apply + (sim 80 (parse input)))))

(defn part2
  []
  (time (apply + (sim 256 (parse input)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
