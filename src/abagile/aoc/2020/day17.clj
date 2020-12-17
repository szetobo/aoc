(ns abagile.aoc.2020.day17
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2020/day17.txt")))

(def sample [".#."
             "..#"
             "###"])

(defn build-map [coll]
  (->> (map #(when (= %1 \#) %2)
           (mapcat identity coll)
           (for [r (range (count coll))
                 c (range (count (first coll)))]
             [r c 0]))
       (filter some?)
       (into #{})))

(def sample-map (build-map sample))

; #{[0 1 0] [2 2 0] [1 2 0] [2 0 0] [2 1 0]}

(defn get-cell [x y z m] (if (m [x y z]) 1 0))

(defn adjacent-cells [x y z]
  (for [x' [(dec x) x (inc x)]
        y' [(dec y) y (inc y)]
        z' [(dec z) z (inc z)]
        :when (or (not= x' x) (not= y' y) (not= z' z))]
    [x' y' z']))

(defn sum-of-adjacent-cells [x y z m]
  (reduce + (for [[x' y' z'] (adjacent-cells x y z)]
              (get-cell x' y' z' m))))

(defn get-map-size [m]
  (reduce (fn [[xs xl ys yl zs zl] [x y z]]
            [(min x xs) (max x xl)
             (min y ys) (max y yl)
             (min z zs) (max z zl)])
          [0 0 0 0 0 0]
          m))

(defn game1 [m]
  (let [[x x' y y' z z'] (get-map-size m)]
    (->> (for [x (range (dec x) (+ x' 2))
               y (range (dec y) (+ y' 2))
               z (range (dec z) (+ z' 2))]
           [[x y z] (sum-of-adjacent-cells x y z m)])
         (reduce (fn [res [[x y z] n]]
                   (if (= (get-cell x y z res) 1)
                     (if (<= 2 n 3) res (disj res [x y z]))
                     (if (= n 3) (conj res [x y z]) res)))
                 m))))

(defn games [f m n]
  (first (drop (max 0 n) (iterate f m))))

(defn part1 []
  (time (count (games game1 (build-map input) 6))))

(comment
    (sum-of-adjacent-cells 1 2 0 sample-map)
    (get-map-size sample-map)
    (game1 sample-map)
    (count (games game1 sample-map 6)))

(defn build-map2 [coll]
  (->> (map #(when (= %1 \#) %2)
           (mapcat identity coll)
           (for [r (range (count coll))
                 c (range (count (first coll)))]
             [r c 0 0]))
       (filter some?)
       (into #{})))

(build-map2 sample)

(defn get-cell2 [x y z w m] (if (m [x y z w]) 1 0))

(defn adjacent-cells2 [x y z w]
  (for [x' [(dec x) x (inc x)]
        y' [(dec y) y (inc y)]
        z' [(dec z) z (inc z)]
        w' [(dec w) w (inc w)]
        :when (or (not= x' x) (not= y' y) (not= z' z) (not= w' w))]
    [x' y' z' w']))

(defn sum-of-adjacent-cells2 [x y z w m]
  (reduce + (for [[x' y' z' w'] (adjacent-cells2 x y z w)]
              (get-cell2 x' y' z' w' m))))

(defn get-map-size2 [m]
  (reduce (fn [[xs xl ys yl zs zl ws wl] [x y z w]]
            [(min x xs) (max x xl)
             (min y ys) (max y yl)
             (min z zs) (max z zl)
             (min w ws) (max w wl)])
          [0 0 0 0 0 0 0 0]
          m))

(defn game2 [m]
  (let [[x x' y y' z z' w w'] (get-map-size2 m)]
    (->> (for [x (range (dec x) (+ x' 2))
               y (range (dec y) (+ y' 2))
               z (range (dec z) (+ z' 2))
               w (range (dec w) (+ w' 2))]
           [[x y z w] (sum-of-adjacent-cells2 x y z w m)])
         (reduce (fn [res [[x y z w] n]]
                   (if (= (get-cell2 x y z w res) 1)
                     (if (<= 2 n 3) res (disj res [x y z w]))
                     (if (= n 3) (conj res [x y z w]) res)))
                 m))))

(defn part2 []
  (time (count (games game2 (build-map2 input) 6))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
