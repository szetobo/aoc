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
  (let [cols (->> s (re-find #"\d+\n") count dec)
        elms (->> s (re-seq #"\d") (map util/parse-int)) 
        grid (into {} (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]})))

(defn ->prn
  [xs grid]
  (->> grid (into (sorted-map)) vals (partition xs)))

(defn bounded
  ([grid]      (apply bounded (-> grid meta :dim)))
  ([rows cols] (fn [[row col]] (and (> rows row -1) (> cols col -1)))))

(defn- adjacent
  ([offsets [row col]]   (for [[dr dc] offsets] [(+ row dr) (+ col dc)]))
  ([offsets f [row col]] (filter f (adjacent offsets [row col]))))

(def adjacent-4 (partial adjacent (map offsets [:north :east :south :west])))
(def adjacent-8 (partial adjacent (vals offsets)))
(def adjacent-9 (partial adjacent (conj (vals offsets) [0 0])))

(defn dijkstra
  [start nbr-dsts]
  (loop [que (priority-map start 0) visited {}]
    (if-let [[pos dst] (peek que)]
      (let [dsts (->> (nbr-dsts pos)
                      (util/remove-keys visited)
                      (util/fmap (partial + dst)))]
        (recur (merge-with min (pop que) dsts) (assoc visited pos dst)))
      visited)))
