(ns abagile.aoc.2015.day1
  (:gen-class)
  (:require
    [clojure.java.io :as io]))

(def input (slurp (io/resource "2015/day1.txt")))

(let [m (frequencies input)]
  (- (m \() (m \))))

(->> (seq input)
     (reduce
       (fn [[sum n] x]
         (if (= sum -1)
           (reduced [sum n])
           [(+ sum (case x \( 1 \) -1 0)) (inc n)]))
       [0 0]))
