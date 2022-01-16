(ns abagile.aoc.algo
  (:require
    [abagile.aoc.util :as util]
    [clojure.data.priority-map :refer [priority-map]]))

(defn dijkstra
  [start nbr-dsts]
  (loop [que (priority-map start 0) visited {}]
    (if-let [[pos dst] (peek que)]
      (let [dsts (->> (nbr-dsts pos)
                      (util/remove-keys visited)
                      (util/fmap (partial + dst)))]
        (recur (merge-with min (pop que) dsts) (assoc visited pos dst)))
      visited)))

(defn subset-sum-01
  [ttl [item & items]]
  (cond
    (zero? ttl) [[]]
    (or (neg? ttl) (nil? item)) []
    :else (concat
            (map #(conj % item) (subset-sum-01 (- ttl item) items))
            (subset-sum-01 ttl items))))

(defn subset-sum-unbounded
  [ttl [item & items :as all-items]]
  (cond
    (zero? ttl) [[]]
    (or (neg? ttl) (nil? item)) []
    :else (concat
            (map #(conj % item) (subset-sum-unbounded (- ttl item) all-items))
            (subset-sum-unbounded ttl items))))

(defn subset-sum-ranged
  [ttl n]
  (if (= 1 n)
    [[ttl]]
    (mapcat (fn [x] (map #(conj % x) (subset-sum-ranged (- ttl x) (dec n)))) (util/range+ 0 ttl))))

(comment
  (subset-sum-ranged 10 4)
  (subset-sum-01 25 [20 15 10 5 5])
  (subset-sum-unbounded 18 [1 5 10 25])
  (subset-sum-unbounded 18 (sort > [1 5 10 25])))
