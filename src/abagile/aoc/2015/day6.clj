(ns abagile.aoc.2015.day6
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(defn input [] (cs/split-lines (slurp (io/resource "2015/day6.txt"))))

(comment
  (count (input)))

(def parse #(->> %
                 (re-matches #"^(turn on|turn off|toggle)\s*(\d+),(\d+) through (\d+),(\d+)$")
                 rest
                 ((fn [[a x y x' y']] [a (read-string x) (read-string y) (read-string x') (read-string y')]))))

(comment
  (parse "turn off 660,55 through 986,197"))

(defn switch1 [grid action x y x' y']
  (reduce
    (fn [grid [action col row]]
      (case action
        "turn on"  (assoc  grid [col row] 1)
        "turn off" (dissoc grid [col row])
        "toggle"   (if (grid [col row])
                     (dissoc grid [col row])
                     (assoc  grid [col row] 1))))
    grid
    (for [col (range x (inc x'))
          row (range y (inc y'))]
      [action col row])))

(defn apply-switch [switch-fn grid action]
  (->> action parse (apply switch-fn grid)))

(comment
  (apply-switch switch1 {} "turn on 0,0 through 2,2")
  (apply-switch switch1 {} "turn off 660,55 through 986,197"))

(comment
  (->> (input)
       (reduce (partial apply-switch switch1) {})
       count))

(defn switch2 [grid action x y x' y']
  (reduce
    (fn [grid [action col row]]
      (case action
        "turn on"  (update-in grid [[col row]] (fnil inc 0))
        "turn off" (if (> (get grid [col row] 0) 1)
                     (update-in grid [[col row]] dec)
                     (dissoc grid [col row]))
        "toggle"   (update-in grid [[col row]] (fnil #(+ 2 %) 0))))
    grid
    (for [col (range x (inc x'))
          row (range y (inc y'))]
      [action col row])))

(comment
  (apply-switch switch2 {} "turn on 0,0 through 2,2")
  (apply-switch switch2 {} "toggle 0,0 through 2,2")
  (apply-switch switch2 {} "turn off 660,55 through 986,197"))

(comment
  (->> (input)
       (reduce (partial apply-switch switch2) {})
       vals
       (reduce +)))
