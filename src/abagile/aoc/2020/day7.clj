(ns abagile.aoc.2020.day7
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as s]))

(def input (->> (util/read-input-split-lines "2020/day7.txt")))

(comment
  (->> (input)
       count))

(defn build-map [m rule]
  (if-let [[_ b1] (re-matches #"^(.+) bags contain.+\.$" rule)]
    (reduce
      (fn [res [_ n b2]]
        (-> res
          (update-in [:content b1] (fnil #(assoc % b2 (util/parse-int n)) {}))
          (update-in [:container b2] (fnil #(conj % b1) #{}))))
      m
      (re-seq #"(\d+) (.*?) bag[s]?" rule))
    m))

(defn find-container [b m]
  (let [s (get-in m [:container b])]
    (when (seq s)
      (reduce s/union s (for [b' s] (find-container b' m))))))

(defn bags-contain [b m]
  (let [bs (get-in m [:content b])]
    (if (seq bs)
      (reduce + (reduce + (vals bs)) (for [[b' c] bs] (* c (bags-contain b' m))))
      0)))

(defn part1 []
  (time (->> input
            (reduce build-map {})
            (find-container "shiny gold")
            (count))))

(defn part2 []
  (time (->> input
            (reduce build-map {})
            (bags-contain "shiny gold"))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
