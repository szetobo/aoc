(ns abagile.aoc.2022.day02
  (:require
   [clojure.string :as cs]))

(def input (->> (slurp "resources/2022/day02.txt")
                cs/split-lines
                (map #(cs/split % #" "))
                (map #(map {"A" 1 "B" 2 "C" 3 "X" 1 "Y" 2 "Z" 3} %))))

(defn score1
  [[a b]]
  (+ b
     (-> (- b a) inc (mod 3) (* 3))))

(defn part1
  []
  (time (->> input (map score1) (reduce +))))

(defn score2
  [[a b]]
  (+ (-> (+ a (- b 2)) dec (mod 3) inc)
     (-> (dec b) (* 3))))

(defn part2
  []
  (time (->> input (map score2) (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
