(ns abagile.aoc.2021.day07
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample-input (util/read-input "2021/day07.sample.txt"))

(def input (util/read-input "2021/day07.txt"))

(defn parse
  [s]
  (->> (re-seq #"\d+" s) (map read-string)))

;; (def acc #(apply + (range (inc %))))
;; (def acc' (memoize acc))
(def pascal-triangle #(-> % (* (inc %)) (/ 2)))

(defn fuel
  [fx freq pos]
  (reduce-kv #(+ %1 (* (fx (util/diff %2 pos)) %3)) 0 freq))

(def fuel1 (partial fuel identity))
(def fuel2 (partial fuel pascal-triangle))

(comment
  (-> sample-input parse frequencies (as-> $ (map #(fuel1 $ %) (range (->> (keys $) (apply max) inc)))))
  (-> sample-input parse frequencies (as-> $ (map #(fuel2 $ %) (range (->> (keys $) (apply max) inc))))))

(defn part1
  []
  (time
    (let [nos  (parse input)
          freq (frequencies nos)
          m    (apply max nos)]
      (apply min (map #(fuel1 freq %) (range (inc m)))))))

(defn part2
  []
  (time
    (let [nos  (parse input)
          freq (frequencies nos)
          m    (apply max nos)]
      (apply min (map #(fuel2 freq %) (range (inc m)))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
