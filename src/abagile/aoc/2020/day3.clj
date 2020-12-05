(ns abagile.aoc.2020.day3
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def input (->> (cs/split-lines (slurp (io/resource "day3.txt")))))

(defn count-of-trees [r d]
  (let [len (count (first input))]
    (->> (map #(nth %1 (mod %2 len)) 
              (take-nth d input)
              (range 0 (* r (count input)) r))
         (filter #(= \# %))
         count)))

(defn -main [& _]
  (println "part 1:" (count-of-trees 3 1))

  (println "part 2:" (->> [[1 1] [3 1] [5 1] [7 1] [1 2]]
                          (map #(apply count-of-trees %))
                          (reduce *))))
