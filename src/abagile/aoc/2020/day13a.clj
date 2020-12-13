(ns abagile.aoc.2020.day13a
  (:gen-class))
  ; (:require))
  ;   [clojure.string :as cs]))

(def sample "939
             7,13,x,x,59,x,31,19")

(def input "1006697
            13,x,x,41,x,x,x,x,x,x,x,x,x,641,x,x,x,x,x,x,x,x,x,x,x,19,x,x,x,x,17,x,x,x,x,x,x,x,x,x,x,x,29,x,661,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,23")

(def sample1 "17,x,13,19")
(def sample2 "67,7,59,61")
(def sample3 "67,x,7,59,61")
(def sample4 "67,7,x,59,61")
(def sample5 "1789,37,47,1889")

(def parse (fn [data] (map #(Long/parseLong %) (re-seq #"\d+" data))))

(let [[ts & busses] (parse input)]
  (apply * (first (sort-by second (for [bus busses] [bus (- bus (mod ts bus))])))))


(defn parse2 [input] (->> input
                          (re-seq #"[\dx]+")
                          (next)
                          (map read-string)
                          (map-indexed (fn [i n]
                                         (when (not= 'x n)
                                           [i n])))
                          (filter some?)))

(parse2 sample)
(parse2 input)

; (def real-parsed2 (parse2 (slurp (util/puzzle-input 13))))

(defn bus-cycle [[^long idx ^long bus]]
  (iterate #(+ bus ^long %) (- idx)))

(take 10 (bus-cycle [0 7]))
(take 10 (bus-cycle [1 13]))

(defn narrow-cycle [bc [^long idx ^long bus]]
  (let [;; This is basically our first attempt, just do a filter over the
        ;; bus-cycle
        new-cycle (filter #(= 0 (mod (+ ^long % idx) bus)) bc)
        ;; But once you have that new cycle, you know that it'll skip ahead by
        ;; fixed increments
        [n1 n2] (take 2 new-cycle)
        diff (- ^long n2 ^long n1)]
    ;; So we basically re-create it as a single iteration sequence
    (iterate #(+ ^long % diff) n1)))

(time
 (first
  (let [[s & ss] (sort-by second (parse2 input))]
    (reduce narrow-cycle (bus-cycle s) ss))))
