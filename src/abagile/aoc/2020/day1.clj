(ns abagile.aoc.2020.day1
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    ; [clojure.math.combinatorics :as comb]
    [clojure.set :as s]
    [clojure.string :as cs]))

(def input (->> (cs/split-lines (slurp (io/resource "day1.txt")))
                (map read-string)))

;; simplified version, poor-man combinations
(defn combinations [lst n]
  (let [cnt (count lst)]
    (cond
      (> n cnt) '()
      (= n 0)   '()
      (= n 1)   (for [x lst] (list x))
      (= n cnt) (list (seq lst))
      :else (let [[head & tail] lst]
              (concat
                (for [y (combinations tail (dec n))]
                  (conj y head))
                (combinations tail n))))))

(prn (combinations [1 2 3 4] 3))

(defn -main [& _]
  (println "part 1:" (reduce * (s/intersection
                                 (into #{} input)
                                 (into #{} (map #(- 2020 %) input))))) 

  (println "part 2:" (->> (combinations input 3)
                          (filter #(= 2020 (apply + %)))
                          first
                          (reduce *))))

;; smart and clean solution copy from lambda island
(first
  (for [x input
        y input
        :when (= 2020 (+ x y))]
   (* x y)))

(first
  (for [x input
        y input
        z input
        :when (= 2020 (+ x y z))]
   (* x y z)))
