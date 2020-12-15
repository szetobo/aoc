(ns abagile.aoc.2020.day15
  (:gen-class))
  ; (:require
  ;   [abagile.aoc.util :as util]))

(defn say [input n]
  (loop [i (count input)
         prev (last input)
         pos (into {} (map vector input (range 1 i)))]
    (if (> i (dec n))
      [i prev]
      (if (nil? (pos prev))
        (recur (inc i) 0 (assoc pos prev i))
        (recur (inc i) (- i (pos prev)) (assoc pos prev i))))))

(defn -main [& _]
  (println "part 1:"
           (time (say [0 3 6] 2020)))

  (println "part 2:"
           (time (say [1 0 18 10 19 6] 30000000))))
