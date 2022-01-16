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

(defn range-sum-subsets
  [sum n]
  (if (= 1 n)
    [[sum]]
    (mapcat (fn [x] (map #(conj % x) (range-sum-subsets (- sum x) (dec n)))) (util/range+ 0 sum))))

(defn coin-change-combinations
  [amount coins]
  (->> (cond
         (zero? amount) [[]]
         (pos? amount)  (for [coin coins
                              res  (coin-change-combinations (- amount coin) coins)]
                          (conj res coin)))
       (map #(sort > %))
       distinct))

(comment
  (coin-change-combinations 18 (sort > #{1 5 10 25}))
  (range-sum-subsets 3 3))
