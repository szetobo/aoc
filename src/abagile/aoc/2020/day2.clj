(ns abagile.aoc.2020.day2
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def input (->> (cs/split-lines (slurp (io/resource "day2.txt")))))

(defn -main [& _]
  (println "part 1:" (->> input
                          (map #(rest (re-matches #"(\d+)-(\d+)\s+(.):\s*(.*)$" %)))
                          (filter #(let [[n1 n2 s1 s2] %
                                         n1 (read-string n1)
                                         n2 (read-string n2)
                                         s1 (first s1)]
                                     (<= n1 (get (frequencies s2) s1 0) n2)))
                          count))

  (println "part 2:" (->> input
                          (map #(rest (re-matches #"(\d+)-(\d+)\s+(.):\s*(.*)$" %)))
                          (filter #(let [[n1 n2 s1 s2] %
                                         n1  (read-string n1)
                                         n2  (read-string n2)
                                         s1  (first s1)
                                         len (count s2)
                                         b1  (and (>= len n1) (= s1 (nth s2 (dec n1))))
                                         b2  (and (>= len n2) (= s1 (nth s2 (dec n2))))]
                                     (if b1 (not b2) b2)))
                          count)))
