(ns abagile.aoc.2020.day5
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def input (->> (cs/split-lines (slurp (io/resource "day5.txt")))))

(defn -main [& _]
  (println "part 1:" (->> input
                          (map #(cs/replace % #"B|R" "1")) 
                          (map #(cs/replace % #"F|L" "0")) 
                          (map (partial str "2r"))
                          (map read-string)
                          (apply max)))

  (println "part 2:" (->> input
                          (map #(cs/replace % #"B|R" "1")) 
                          (map #(cs/replace % #"F|L" "0")) 
                          (map (partial str "2r"))
                          (map read-string)
                          sort
                          (partition 2 1)
                          (filter #(let [[n1 n2] %] (not= 1 (- n2 n1))))
                          ffirst
                          (+ 1))))
