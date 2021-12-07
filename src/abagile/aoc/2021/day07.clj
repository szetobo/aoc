(ns abagile.aoc.2021.day07
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample-input (util/read-input "2021/day07.sample.txt"))

(def input (util/read-input "2021/day07.txt"))

(defn parse
  [s]
  (->> (re-seq #"\d+" s) (map read-string) frequencies))

(def acc #(apply + (range (inc %))))
(def acc' (memoize acc))

(defn fuel
  [fx nos pos]
  (reduce-kv #(+ %1 (* (fx (util/diff %2 pos)) %3)) 0 nos))

(def fuel1 (partial fuel identity))
(def fuel2 (partial fuel acc'))

(comment
  (-> sample-input parse (as-> $ (map #(fuel1 $ %) (range (->> (keys $) (apply max) inc)))))
  (-> sample-input parse (as-> $ (map #(fuel2 $ %) (range (->> (keys $) (apply max) inc))))))

(defn part1
  []
  (time
    (let [nos (parse input)
          m   (apply max (keys nos))]
      (apply min (map #(fuel1 nos %) (range (inc m)))))))

(defn part2
  []
  (time
    (let [nos (parse input)
          m   (apply max (keys nos))]
      (apply min (map #(fuel2 nos %) (range (inc m)))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
