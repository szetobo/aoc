(ns abagile.aoc.grid
  (:require
    [abagile.aoc.util :as util]
    [clojure.data.priority-map :refer [priority-map]]))

(def offsets
  {:north [-1 0] :north-east [-1 1]
   :east  [0 1]  :south-east [1 1]
   :south [1 0]  :south-west [1 -1]
   :west  [0 -1] :north-west [-1 -1]})

(defn parse
  [s]
  (let [xs   (->> s (re-find #"\d+\n") count dec)
        elms (->> s (re-seq #"\d") (map util/parse-int)) 
        grid (into {} (map-indexed #(vector [(int (/ %1 xs)) (mod %1 xs)] %2) elms))]
     (with-meta grid {:dim [xs (/ (count elms) xs)]})))

(defn ->prn
  [xs grid]
  (->> grid (into (sorted-map)) vals (partition xs)))

(defn- adjacent
  [offsets [xs ys] [x y]]
  (for [[dx dy] offsets
        :let [x' (+ x dx) y' (+ y dy)]
        :when (and (> xs x' -1) (> ys y' -1))]
    [x' y']))

(def adjacent-4 (partial adjacent (map offsets [:north :east :south :west])))
(def adjacent-8 (partial adjacent (vals offsets)))
(def adjacent-9 (partial adjacent (sort (conj (vals offsets) [0 0]))))

(defn dijkstra
  [start adj-dsts]
  (loop [que (priority-map start 0) seen {}]
    (if-let [[pos dst] (peek que)]
      (let [dsts (->> (adj-dsts pos)
                      (util/remove-keys seen)
                      (util/fmap (partial + dst)))]
        (recur (merge-with min (pop que) dsts) (assoc seen pos dst)))
      seen)))
