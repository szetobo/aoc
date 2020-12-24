(ns abagile.aoc.2020.day23
  (:gen-class)
  (:require
    ; [abagile.aoc.util :as util]
    [clojure.test :refer [deftest run-tests is]]))

(def input [9 2 5 1 7 6 8 3 4])

(def sample [3 8 9 1 2 5 4 6 7])

(defn move1 [cups]
  (let [[h x y z & r] cups
        n (or (->> (filter #(> h %) r) sort last)
              (apply max r))]
    (loop [res []
           [f & t] r]
      (if (= n f)
        (-> (conj res n x y z)
            (into t)
            (conj h))
        (recur (conj res f) t)))))

(defn label [data n]
  (->> (first (drop n (iterate move1 data)))
       (partition-by #(= % 1))
       (#(let [[x y z] %] (if (= x [1]) y (concat z x))))
       (apply str)))

; (label sample 100)
; (label input 100)

(defn initial-state [cups]
  {:next-cup (->> (partition 2 1 cups)
                  (concat [[0 nil] [(last cups) (first cups)]])
                  (sort-by first)
                  (mapv second)
                  transient)
   :current (first cups)
   :maxcup (apply max cups)})

(defn move2 [{:keys [next-cup current maxcup] :as state}]
  ;; ... current a b c d ... destination e ...
  (let [a (next-cup current) b (next-cup a) c (next-cup b) d (next-cup c)
        destination (->> (iterate #(if (= % 1) maxcup (dec %)) current)
                         rest
                         (some #(when (not (#{a b c} %)) %)))
        e (next-cup destination)]
    (assoc state
           :next-cup (assoc! next-cup current d destination a c e)
           :current d)))

(defn next-two [data n]
  (let [nc (:next-cup (first (drop n (iterate move2 (initial-state data)))))
        x (nc 1)
        y (nc x)]
    [x y]))

(comment)

(defn part1 []
  (time (label input 100)))

(defn part2 []
  (time (apply * (next-two (concat input (range 10 1000001)) 10000000))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= 1 1)))

(deftest test-input
  (is (= 1 1)))

(run-tests)
