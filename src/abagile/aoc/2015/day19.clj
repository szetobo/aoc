(ns abagile.aoc.2015.day19
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]
    [clojure.test :refer [deftest is]]))

(def sample (->> (util/read-input-split "2015/day19.sample.txt" #"\n\n")))
(def input  (->> (util/read-input-split "2015/day19.txt" #"\n\n")))

(defn parse
  [[rules input]]
  [(->> rules (re-seq #"(\w+) => (\w+)") (map rest))
   (->> input (re-find #"\w+"))])

(defn replacement
  [[rules input]]
  (->> (for [[fr to] rules :let [fr-size (count fr)]]
         (loop [start 0 rec []]
           (if-let [i (cs/index-of input fr start)]
             (recur (+ i fr-size) (conj rec (str (subs input 0 i) to (subs input (+ i fr-size)))))
             rec)))
       (reduce concat) set))

(defn part1 []
  (time
    (->> (parse input) replacement count)))

(defn part2 []
  (time
    (let [molecule (second (parse input))]
     (- (count (re-seq #"[A-Z]" molecule))
        (+
          (count (re-seq #"Rn" molecule))
          (count (re-seq #"Ar" molecule))
          (* 2 (count (re-seq #"Y" molecule)))
          1)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest test-sample
  (is (= (parse sample) [[["H" "HO"] ["H" "OH"], ["O" "HH"]] "HOH"]))
  (is (= (replacement (parse sample)) #{"HOOH" "HOHO" "OHOH" "HHHH"})))
