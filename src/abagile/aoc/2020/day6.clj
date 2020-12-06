(ns abagile.aoc.2020.day6
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.set :as s]
    [clojure.string :as cs]))

(defn input [] (->> (cs/split (slurp (io/resource "day6.txt")) #"\n\n")
                    (map cs/split-lines)))

(comment
  (->> (input)
       count))

(defn -main [& _]
  (println "part 1:" (->> (input)
                          ; (take 3)
                          (map #(set (apply str %)))
                          (map count)
                          (reduce +)))

  (println "part 2:" (->> (input)
                          ; (take 10)
                          (map #(map set %))
                          (map #(apply s/intersection %))
                          (map count)
                          (reduce +))))
