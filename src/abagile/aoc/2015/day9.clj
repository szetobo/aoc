(ns abagile.aoc.2015.day9
  (:gen-class)
  (:require
    [clojure.data.priority-map :refer [priority-map]]
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(defn input [] (cs/split-lines (slurp (io/resource "2015/day9.txt"))))

(comment
  (count (input)))

(defn build-map [res path]
  (if-let [[_ c1 c2 d] (re-matches #"^(\w+)\s*to\s*(\w+)\s*=\s*(\d+)$" path)]
    (let [d (Integer/parseInt d)]
      (-> res
          (update-in [:cities] (fnil #(conj % c1 c2) #{}))
          (update-in [c1] (fnil #(assoc % c2 d) (priority-map)))
          (update-in [c2] (fnil #(assoc % c1 d) (priority-map)))))
    res))

(defn cal-dist [m]
  (into (priority-map)
    (for [c1 (:cities m)
          c2 (:cities m)
          c3 (:cities m)
          c4 (:cities m)
          c5 (:cities m)
          c6 (:cities m)
          c7 (:cities m)
          c8 (:cities m)
          :when (= 8 (count (set [c1 c2 c3 c4 c5 c6 c7 c8])))]
      [[c1 c2 c3 c4 c5 c6 c7 c8] (+ (get-in m [c1 c2])
                                    (get-in m [c2 c3])
                                    (get-in m [c3 c4])
                                    (get-in m [c4 c5])
                                    (get-in m [c5 c6])
                                    (get-in m [c6 c7])
                                    (get-in m [c7 c8]))])))

(comment
  (->> (input)
       ; (take 1)
       (reduce build-map {})
       cal-dist
       first)

  (->> (input)
       ; (take 1)
       (reduce build-map {})
       cal-dist
       last))
