(ns abagile.aoc.2020.day11
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def sample1 ["L.LL.LL.LL"
              "LLLLLLL.LL"
              "L.L.L..L.."
              "LLLL.LL.LL"
              "L.LL.LL.LL"
              "L.LLLLL.LL"
              "..L.L....."
              "LLLLLLLLLL"
              "L.LLLLLL.L"
              "L.LLLLL.LL"])

(defn build-map [coll]
  (loop [res [] i 0]
    (if (>= i (count coll))
      res
      (recur
        (conj res (into [] (for [cell (seq (coll i))]
                             (case cell \L 1 \# 9 \. 0))))
        (inc i)))))

(comment)
(def sample-map1 (build-map sample1))

;   [[1 0 1 1 0 1 1 0 1 1]
;    [1 1 1 1 1 1 1 0 1 1]
;    [1 0 1 0 1 0 0 1 0 0]
;    [1 1 1 1 0 1 1 0 1 1]
;    [1 0 1 1 0 1 1 0 1 1]
;    [1 0 1 1 1 1 1 0 1 1]
;    [0 0 1 0 1 0 0 0 0 0]
;    [1 1 1 1 1 1 1 1 1 1]
;    [1 0 1 1 1 1 1 1 0 1]
;    [1 0 1 1 1 1 1 0 1 1]])

(defn get-cell [x y m] ((m x) y))

(defn get-cell-by-dir [x y dx dy m]
  (let [x-size (-> m count)
        y-size (-> m first count)]
    (loop [x' (+ x dx)  y' (+ y dy)]
      (if (not (and (> x-size x' -1) (> y-size y' -1)))
        0
        (let [v (get-cell x' y' m)]
          (if (not= v 0) v (recur (+ x' dx) (+ y' dy))))))))

(defn sum-of-eight-dir [x y m]
  (reduce + (for [dx [-1 0 1]
                  dy [-1 0 1]
                  :when (or (not= dx 0) (not= dy 0))]
              (get-cell-by-dir x y dx dy m))))

(defn adjacent-cells [x y]
  (for [x' [(dec x) x (inc x)]
        y' [(dec y) y (inc y)]
        :when (or (not= x' x) (not= y' y))]
    [x' y']))

(defn sum-of-adjacent-cells [x y m]
  (reduce + (for [[x' y'] (adjacent-cells x y)
                  :when (and (>= x' 0) (>= y' 0)
                             (< x' (-> m count)) (< y' (-> m first count)))]
              (get-cell x' y' m))))

(defn game1 [m]
  (let [x-size (-> m count)
        y-size (-> m first count)]
    (->> (for [x (range x-size)
               y (range y-size)]
           (if (zero? (get-cell x y m))
             0
             (sum-of-adjacent-cells x y m)))
         (map #(case %1
                 0 0
                 1 (if (< %2 9) 9 1)
                 9 (if (< %2 36) 9 1))
              (flatten m))
         (partition y-size)
         (map vec)
         vec)))

(defn game2 [m]
  (let [x-size (-> m count)
        y-size (-> m first count)]
    (->> (for [x (range x-size)
               y (range y-size)]
           (if (zero? (get-cell x y m))
             0
             (sum-of-eight-dir x y m)))
         (map #(case %1
                 0 0
                 1 (if (< %2 9) 9 1)
                 9 (if (< %2 45) 9 1))
              (flatten m))
         (partition y-size)
         (map vec)
         vec)))

(comment
  (get-cell 0 0 sample-map1)
  (adjacent-cells 0 10)
  (sum-of-adjacent-cells 0 10 sample-map1)

  (game1 sample-map1)
  (do
    (println sample-map1)
    (loop [m sample-map1]
      (let [m' (game1 m)]
        (println m')
        (if (= m m') m' (recur m')))))

  (game2 sample-map1)
  (do
    (println sample-map1)
    (loop [m sample-map1]
      (let [m' (game2 m)]
        (println m')
        (if (= m m') m' (recur m'))))))

(defn -main [& _]
  (println "part 1:"
           (->> (cs/split-lines (slurp (io/resource "day11.txt")))
                build-map
                (#(loop [coll %]
                    (let [coll' (game1 coll)]
                      (if (= coll coll')
                        coll'
                        (recur coll')))))
                flatten
                (filter #(= % 9))
                count))

  (println "part 2:"
           (->> (cs/split-lines (slurp (io/resource "day11.txt")))
                build-map
                (#(loop [coll %]
                    (let [coll' (game2 coll)]
                      (if (= coll coll') coll' (recur coll')))))
                flatten
                (filter #(= % 9))
                count)))
