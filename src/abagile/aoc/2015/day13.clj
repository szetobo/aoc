(ns abagile.aoc.2015.day13
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.math.combinatorics :refer [permutations]]
    [clojure.string :as cs]))

(def sample1 ["Alice would gain 54 happiness units by sitting next to Bob."
              "Alice would lose 79 happiness units by sitting next to Carol."
              "Alice would lose 2 happiness units by sitting next to David."
              "Bob would gain 83 happiness units by sitting next to Alice."
              "Bob would lose 7 happiness units by sitting next to Carol."
              "Bob would lose 63 happiness units by sitting next to David."
              "Carol would lose 62 happiness units by sitting next to Alice."
              "Carol would gain 60 happiness units by sitting next to Bob."
              "Carol would gain 55 happiness units by sitting next to David."
              "David would gain 46 happiness units by sitting next to Alice."
              "David would lose 7 happiness units by sitting next to Bob."
              "David would gain 41 happiness units by sitting next to Carol."])

(defn parse [s] (->> (re-find #"(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+)" s)
                     rest
                     (#(let [[a b c d] %] (vector (keyword a) b (read-string c) (keyword d))))))

(defn build-map [ctx [p1 lh pt p2]]
  (-> ctx
      (update-in [:family] #(conj % p1 p2))
      (assoc-in [p1 p2] (if (= lh "gain") pt (- pt)))))

(defn cal-points [ctx ps]
  (let [c (concat ps (take 1 ps))
        fx (fn [x] (->> (partition 2 1 x)
                        (map #(get-in ctx % 0))
                        (reduce +)))]
    (+ (fx c) (fx (reverse c)))))

(comment
  (->> (map parse sample1)
       (reduce build-map {:family #{}})
       (#(apply max (for [s (permutations (:family %))] (cal-points % s))))))

(defn -main [& _]
  (println "part 1:"
           (->> (cs/split-lines (slurp (io/resource "2015/day13.txt")))
                (map parse)
                (reduce build-map {:family #{}})
                (#(apply max (for [s (permutations (:family %))] (cal-points % s))))))

  (println "part 2:"
           (->> (cs/split-lines (slurp (io/resource "2015/day13.txt")))
                (map parse)
                (reduce build-map {:family #{:Godwin}})
                (#(apply max (for [s (permutations (:family %))] (cal-points % s)))))))
