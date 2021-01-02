(ns abagile.aoc.2015.day19
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]
    [clojure.test :refer [deftest is]]))

(def input (->> (util/read-input-split "2015/day19.txt" #"\n\n")))

(def sample ["H => HO
              H => OH
              O => HH"
             "HOH"])

(defn parse [s] (->> (re-seq #"(\w+) => (\w+)" s)
                     (map next)))

(defn replacement [data]
  (let [[rr target] data]
    (->> (for [[fr to] (parse rr)
               :let [fr-size (count fr)]]
           (loop [start 0
                  rec   []]
             (if-let [i (cs/index-of target fr start)]
               (recur (+ i fr-size) (conj rec (str (subs target 0 i) to (subs target (+ i fr-size)))))
               rec)))
         (reduce concat)
         set)))

(defn part1 []
  (time (count (replacement input))))

(defn part2 []
  (time (let [molecule (second input)]
         (- (count (re-seq #"[A-Z]" molecule))
            (+
              (count (re-seq #"Rn" molecule))
              (count (re-seq #"Ar" molecule))
              (* 2 (count (re-seq #"Y" molecule)))
              1)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= (parse (first sample)) [["H" "HO"] ["H" "OH"], ["O" "HH"]]))
  (is (= (replacement sample) #{"HOOH" "HOHO" "OHOH" "HHHH"})))
