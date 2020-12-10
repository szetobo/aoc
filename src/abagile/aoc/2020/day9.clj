(ns abagile.aoc.2020.day9
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def input [35 20 15 25 47 40 62 55 65 95 102 117 150 182 127 219 299 277 309 576])

(defn weakness1 [p-count input]
  (loop [lst input]
    (let [[preamble [n & _]] (split-at p-count lst)]
      (cond
        (nil? n)
        :not-found

        (every? nil? (for [x preamble
                           y preamble
                           :when (and (not= x y) (= n (+ x y)))]
                       [x y]))
        n

        :else
        (recur (subvec lst 1))))))

(comment
  (weakness1 5 input))

(defn weakness2 [attack input]
  (loop [lst input]
    (cond
      (< (count lst) 2)
      :not-found

      :else
      (if-let [res (first (for [n (range 2 (inc (count lst)))
                                :let [res (take n lst)]
                                :when (= attack (reduce + res))]
                            res))]
        res
        (recur (subvec lst 1))))))

(comment
  (weakness2 127 input))

(defn -main [& _]
  (println "part 1:" (->> (mapv read-string (cs/split-lines (slurp (io/resource "day9.txt"))))
                          (#(weakness1 25 %))))

  (println "part 2:" (->> (mapv read-string (cs/split-lines (slurp (io/resource "day9.txt"))))
                          (#(weakness2 1492208709 %))
                          (#(+ (apply max %) (apply min %))))))
