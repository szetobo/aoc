(ns abagile.aoc.2015.day2
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(defn input [] (cs/split-lines (slurp (io/resource "2015/day2.txt"))))

(defn paper [l w h]
  (let [area  (+ (* 2 l w) (* 2 w h) (* 2 l h))
        extra (min (* l w) (* w h) (* l h))]
    (+ area extra)))

(comment
  (paper 2 3 4)
  (paper 1 1 10))

(->> (input)
     (map #(->> %
                (re-matches #"(\d+)x(\d+)x(\d+)")
                rest
                (map read-string)
                (apply paper)))
     (reduce +))

(defn ribbon [l w h]
  (let [sides (->> [l w h] sort (take 2) (map #(* 2 %)) (reduce +))
        bow   (* l w h)]
    (+ sides bow)))

(comment
  (ribbon 2 3 4)
  (ribbon 1 1 10))

(->> (input)
     (map #(->> %
                (re-matches #"(\d+)x(\d+)x(\d+)")
                rest
                (map read-string)
                (apply ribbon)))
     (reduce +))
