(ns abagile.aoc.2015.day3
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(defn input [] (cs/trim-newline (slurp (io/resource "2015/day3.txt"))))

(comment
  (count (seq (input)))
  (frequencies (seq (input))))

(->> (seq (input))
     (reduce #(let [[x y]   (:loc %1)
                    [ox oy] (case %2
                              \^ [0 1]
                              \v [0 -1]
                              \> [1 0]
                              \< [-1 0]
                              [0 0])
                    [x' y'] [(+ x ox) (+ y oy)]]
                {:loc [x' y'] :visited (update (:visited %1) [x' y'] (fnil inc 0))})
             {:loc [0 0] :visited {[0 0] 1}})
     :visited
     keys
     count)

(->> (seq (input))
     (reduce #(let [who     (:who %1)
                    [x y]   (%1 who)
                    [ox oy] (case %2
                              \^ [0 1]
                              \v [0 -1]
                              \> [1 0]
                              \< [-1 0]
                              [0 0])
                    [x' y'] [(+ x ox) (+ y oy)]]
                (merge %1
                  {:who (if (= who :s) :r :s)
                   who [x' y']
                   :visited (update (:visited %1) [x' y'] (fnil inc 0))}))
             {:who :s :s [0 0] :r [0 0] :visited {[0 0] 1}})
     :visited
     keys
     count)
