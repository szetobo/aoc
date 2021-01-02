(ns abagile.aoc.2015.template
  (:gen-class)
  (:require
    [clojure.test :refer [deftest is]]))

(defn factors [n]
  (->> (mapcat (fn [x] [x (/ n x)])
              (filter #(zero? (mod n %)) (range 1 (inc (Math/sqrt n)))))
       distinct))

(defn no-of-presents [n]
  (->> (factors n) (reduce +) (* 10)))

(defn part1 []
  (let [target 34000000
        step   60]
    (time (ffirst (filter (fn [[_ p]] (> p target)) (map #(vector % (no-of-presents %)) (range step target step)))))))

(defn factors2 [n]
  (filter #(>= (* % 50) n) (factors n)))

(defn no-of-presents2 [n]
  (->> (factors2 n) (reduce +) (* 11)))

(defn part2 []
  (let [target 34000000
        step   60]
    (time (ffirst (filter (fn [[_ p]] (> p target)) (map #(vector % (no-of-presents2 %)) (range step target step)))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-input
  (is (= (map no-of-presents (range 1 10)) [10 30 40 70 60 120 80 150 130])))
