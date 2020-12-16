(ns abagile.aoc.2020.day1
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    ; [clojure.math.combinatorics :as comb]
    [clojure.core.logic :as l]
    [clojure.core.logic.fd :as fd]
    [clojure.set :as s]))

(def input (->> (util/read-input-split-lines "2020/day1.txt")
                (map util/parse-int)))

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

(comment
  (prn (combinations [1 2 3 4] 3)))

(defn part1 []
  (time (reduce * (s/intersection
                    (into #{} input)
                    (into #{} (map #(- 2020 %) input))))))

(defn part2 []
  (time (->> (combinations input 3)
            (filter #(= 2020 (apply + %)))
            first
            (reduce *))))

;; smart and clean solution copy from lambda island
(defn part2x []
  (time (first (for [x input
                     y input
                     :when (= 2020 (+ x y))]
                (* x y)))))

(defn part2x []
  (time (first (for [x input
                     y input
                     z input
                     :when (= 2020 (+ x y z))]
                (* x y z)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (l/run* [q]
          (l/fresh [x y]
                   (l/membero x input)
                   (l/membero y input)
                   (fd/+ x y 2020)
                   (l/== q [x y])))

  (let [vars (repeatedly 3 l/lvar)
        [x y z] vars]
    (l/run 1 [q]
      (l/== q vars)
      (l/membero x input)
      (l/membero y input)
      (l/membero z input)
      (fd/eq (= (+ x y z) 2020)))))
