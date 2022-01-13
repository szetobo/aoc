(ns abagile.aoc.2015.day08
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (util/read-input-split "2015/day08.txt" #"\n"))

(def decode-diff #(+ 2
                    (count (re-seq #"\\\"|\\\\" %))
                    (* 3 (count (re-seq #"\\x[\da-f]{2}" %)))))

(def encode-diff #(+ 4
                    (* 2 (count (re-seq #"\\\"|\\\\" %)))
                    (count (re-seq #"\\x[\da-f]{2}" %))))

(defn part1
  []
  (time
    (->> input (map decode-diff) (reduce +))))
    

(defn part2
  []
  (time
    (->> input (map encode-diff) (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
