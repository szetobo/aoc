(ns abagile.aoc.2015.day06
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def input (util/read-input-split "2015/day06.txt" #"\n"))

(defn parse
  [s]
  (->> s (mapcat #(re-seq #"(on|off|toggle).*?(\d+),(\d+).*?(\d+),(\d+)" %))
       (map (fn [[_ op & rsts]] (into [(keyword op)] (map read-string rsts))))))

(defn part1
  []
  (time
    (->> (parse input)
         (reduce
           (fn [grid [op r1 c1 r2 c2]]
             (let [pts (for [r (util/range+ r1 r2) c (util/range+ c1 c2)] (+ r (* 1000 c)))]
               (case op
                 :on     (reduce #(assoc! %1 %2 1) grid pts)
                 :off    (reduce #(assoc! %1 %2 0) grid pts)
                 :toggle (reduce #(assoc! %1 %2 ({0 1 1 0} (grid %2))) grid pts))))
           (transient (vec (repeat (* 1000 1000) 0))))
         persistent!  
         (reduce +))))

(defn part2
  []
  (time
    (->> (parse input)
         (reduce
           (fn [grid [op r1 c1 r2 c2]]
             (let [pts (for [r (util/range+ r1 r2) c (util/range+ c1 c2)] (+ r (* 1000 c)))]
               (case op
                 :on     (reduce #(assoc! %1 %2 (inc (grid %2))) grid pts)
                 :off    (reduce #(assoc! %1 %2 (max 0 (dec (grid %2)))) grid pts)
                 :toggle (reduce #(assoc! %1 %2 (+ 2 (grid %2))) grid pts))))
           (transient (vec (repeat (* 1000 1000) 0))))
         persistent!  
         (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
