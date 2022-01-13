(ns abagile.aoc.2015.day10
  (:gen-class))

(def input "1113222113")

(def sample "111221")

(defn look-and-say [coll]
  (mapcat (juxt count first) (partition-by identity coll)))

;                   11132-22113
;                   31133-22113
;                 1321232-22113
;           1113121112133-22113
;       31131112311211232-22113
; 13211331121321122112133-22113

(defn part1
  []
  (time
    (->> (seq input) (map #(Character/digit % 10)) (iterate look-and-say)
         (drop 40) first count)))

(defn part2
  []
  (time
    (->> (seq input) (map #(Character/digit % 10)) (iterate look-and-say)
         (drop 50) first count)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
