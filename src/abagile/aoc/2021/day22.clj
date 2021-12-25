(ns abagile.aoc.2021.day22
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample1 (util/read-input "2021/day22.sample1.txt"))
(def sample2 (util/read-input "2021/day22.sample2.txt"))
(def sample3 (util/read-input "2021/day22.sample3.txt"))
(def input   (util/read-input "2021/day22.txt"))

(defn parse
  [s]
  (->> s (re-seq #"(?:on|off|-?\d+)") (map read-string) (partition 7) (map vec) (map #(update % 0 {'on 1 'off -1}))))

(defn init-step
  [reactor [action x1 x2 y1 y2 z1 z2]]
  (reduce (case action 1 conj disj) reactor
    (for [x (util/range+ x1 x2) y (util/range+ y1 y2) z (util/range+ z1 z2)]
      [x y z])))

(defn volume
  [cube]
  (->> (partition 2 cube) (map #(->> % reverse (reduce -) inc)) (reduce *)))

(defn intersect?
  [[_ x1 x2 y1 y2 z1 z2] [_ x3 x4 y3 y4 z3 z4]]
  (and (>= x2 x3) (>= x4 x1) (>= y2 y3) (>= y4 y1) (>= z2 z3) (>= z4 z1)))

(defn intersection
  [[_ x1 x2 y1 y2 z1 z2 :as a] [bs x3 x4 y3 y4 z3 z4 :as b]]
  (when (intersect? a b)
    [(- bs) (max x1 x3) (min x2 x4) (max y1 y3) (min y2 y4) (max z1 z3) (min z2 z4)]))

(defn reboot-step
  [res [s _ _ _ _ _ _ :as curr]]
  (if (empty? res)
    (cond-> res (pos? s) (conj curr))
    (cond-> (reduce #(cond-> %1 (intersect? curr %2) (conj (intersection curr %2))) res res)
            (pos? s) (conj curr))))

(defn reboot
  [steps]
  (->> (reduce reboot-step [] steps)
       (map (fn [[s & cube]] (* s (volume cube))))
       (apply +)))

(defn init
  [steps]
  (->> steps
       (filter (fn [[_ & xyz]] (every? #(>= 50 % -50) xyz)))
       reboot))
  
(comment
  (count sample1)
  (parse sample1)
  (parse sample2)
  (parse sample3)
  (count input)
  (parse input))

(defn part1
  []
  (time
    (->> (parse input) init)))

(defn part2
  []
  (time
    (->> (parse input) reboot)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 39     (->> (parse sample1) init)))
  (is (= 590784 (->> (parse sample2) init)))
  (is (= 474140 (->> (parse sample3) init)))
  (is (= 27 (volume [10 12 10 12 10 12])))
  (is (= true  (intersect? [1 10 12 10 12 10 12] [1 11 13 11 13 11 13])))
  (is (= false (intersect? [1 10 12 10 12 10 12] [1 13 13 10 12 10 12])))
  (is (= 2758514936282235 (->> (parse sample3) reboot))))
