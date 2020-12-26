(ns abagile.aoc.2020.day25
  (:gen-class)
  (:require
    [clojure.test :refer [deftest run-tests is]]))

(def input [18499292 8790390])

(def sample [5764801 17807724])

(defn transform [sn] (iterate #(mod (* sn %) 20201227) 1))

(defn loop-size [pk]
  (ffirst (filter (fn [[_n v]] (= v pk)) (map-indexed vector (transform 7)))))

(defn encrypt-key [[card door]]
  (first (drop (loop-size card) (transform door))))

(defn part1 []
  (time (encrypt-key input)))

(defn part2 [])

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-part1
  (is (= (loop-size (first sample)) 8))
  (is (= (loop-size (second sample)) 11))
  (is (= (encrypt-key sample) 14897079)))

(deftest test-part2
  (is (= 1 1)))

(run-tests)
