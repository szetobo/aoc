(ns abagile.aoc.2015.day15
  (:gen-class)
  (:require
    [abagile.aoc.algo :as algo]
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input-split "2015/day15.sample.txt" #"\n"))
(def input  (util/read-input-split "2015/day15.txt" #"\n"))

(defn parse
  [data]
  (into {} (map vector
             (map #(->> (re-find #"(\w+):" %) second keyword) data)
             (map #(->> (re-seq #"(\w+) (-?\d+)" %)
                        (map (fn [[_ prop units]] [(keyword prop) (read-string units)]))
                        (into {}))
               data))))

(defn recipe-scores
  [ingredients]
  (for [spoons (algo/range-sum-subsets 100 (count ingredients))]
    (->> (map (fn [[_ v] spoon] (util/fmap #(* % spoon) v)) ingredients spoons)
         (apply merge-with +)
         (util/fmap #(max 0 %)))))

(defn part1
  []
  (time
    (->> (parse input) recipe-scores
         (map #(->> (dissoc % :calories) vals (reduce *))) (apply max))))

(defn part2
  []
  (time
    (->> (parse input) recipe-scores
         (filter #(= 500 (:calories %)))
         (map #(->> (dissoc % :calories) vals (reduce *))) (apply max))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 62842880 (->> (parse sample) recipe-scores
                       (map #(->> (dissoc % :calories) vals (reduce *))) (apply max))))
  (is (= 57600000 (->> (parse sample) recipe-scores
                       (filter #(= 500 (:calories %)))
                       (map #(->> (dissoc % :calories) vals (reduce *))) (apply max)))))
