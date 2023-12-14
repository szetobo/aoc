(ns abagile.aoc.2023.day01
  (:require
   [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split "2023/day01.txt" #"\n")))

(defn part1
  []
  (time (->> input
             (map #(->> (re-seq #"\d" %)
                        ((juxt first last))
                        (map read-string)
                        ((fn [[x y]] (+ (* x 10) y)))))
             (reduce +))))

(def digit-map {'one 1 'two 2 'three 3 'four 4 'five 5 'six 6 'seven 7 'eight 8 'nine 9})
(def ->digit #(get digit-map % %))

(comment
  ; part-2 require overlapping matching, therefore need to use lookahead/lookbehind assertion
  (re-seq #"(two|eight|nine)" "nineightwo")       ; (["nine" "nine"] ["two" "two"])
  (re-seq #"(?=(two|eight|nine))" "nineightwo"))  ; (["" "nine"] ["" "eight"] ["" "two"])

(defn part2
  []
  (time (->> input
             (map #(->> (re-seq #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))" %)
                        (map last)
                        ((juxt first last))
                        (map read-string)
                        (map ->digit)
                        ((fn [[x y]] (+ (* x 10) y)))))
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
