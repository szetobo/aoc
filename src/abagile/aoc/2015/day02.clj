(ns abagile.aoc.2015.day02
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def input (->> (util/read-input-split "2015/day02.txt" #"\n")
                (map #(map read-string (re-seq #"\d+" %)))))

(defn paper [l w h]
  (let [area  (+ (* 2 l w) (* 2 w h) (* 2 l h))
        extra (min (* l w) (* w h) (* l h))]
    (+ area extra)))

(defn ribbon [l w h]
  (let [sides (->> (sort [l w h]) (take 2) (map #(* 2 %)) (reduce +))
        bow   (* l w h)]
    (+ sides bow)))

(defn part1
  []
  (time
    (->> input (map #(apply paper %)) (reduce +))))

(defn part2
  []
  (time
    (->> input (map #(apply ribbon %)) (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 58 (paper 2 3 4)))
  (is (= 43 (paper 1 1 10)))
  (is (= 34 (ribbon 2 3 4)))
  (is (= 14 (ribbon 1 1 10))))
