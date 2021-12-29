(ns abagile.aoc.2015.day03
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def input (->> (util/read-input-split "2015/day03.txt" #"\n") first))

(defn part1
  []
  (time
    (count (loop [pos [0 0] visited #{[0 0]} [fst & rsts] input]
            (if fst
              (let [pos (mapv + pos (case fst \^ [-1 0] \v [1 0] \> [0 1] \< [0 -1]))]
                (recur pos (conj visited pos) rsts))
              visited)))))

(defn part2
  []
  (time
    (count (loop [cur 0 pos [[0 0] [0 0]] visited #{[0 0]} [fst & rsts] input]
            (if fst
              (let [pos-x (mapv + (get pos cur) (case fst \^ [-1 0] \v [1 0] \> [0 1] \< [0 -1]))]
                (recur (get {0 1 1 0} cur) (assoc pos cur pos-x) (conj visited pos-x) rsts))
              visited)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
