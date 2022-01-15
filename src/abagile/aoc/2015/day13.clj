(ns abagile.aoc.2015.day13
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.math.combinatorics :as comb]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input-split "2015/day13.sample.txt" #"\n"))
(def input  (util/read-input-split "2015/day13.txt" #"\n"))

(defn parse
  [s]
  (reduce #(let [[_ a sign units b] (re-find #"(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+)" %2)]
              (assoc %1 (list (keyword a) (keyword b)) (cond-> (read-string units) (= "lose" sign) (* -1))))
    {}
    s))

(defn cal-happiness
  [happiness]
  (let [guests (-> happiness keys flatten set)]
    (->> (for [seating (comb/permutations (rest guests))
               :let [seating        (concat (take 1 guests) seating (take 1 guests))
                     pair-happiness (juxt happiness (comp happiness reverse))]]
           (->> (partition 2 1 seating) (reduce #(+ %1 (apply (fnil + 0 0) (pair-happiness %2))) 0)))
         (apply max))))

(defn part1
  []
  (time
    (->> (parse input) cal-happiness)))

(defn part2
  []
  (time
    (->> (assoc (parse input) '(:me :me) 0) cal-happiness)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
