(ns abagile.aoc.2015.day14
  (:gen-class))
  ; (:require
  ;   [clojure.java.io :as io]
  ;   [clojure.string :as cs]))

(def sample ["Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds."
             "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."])

(def input ["Rudolph can fly 22 km/s for 8 seconds, but then must rest for 165 seconds."
            "Cupid can fly 8 km/s for 17 seconds, but then must rest for 114 seconds."
            "Prancer can fly 18 km/s for 6 seconds, but then must rest for 103 seconds."
            "Donner can fly 25 km/s for 6 seconds, but then must rest for 145 seconds."
            "Dasher can fly 11 km/s for 12 seconds, but then must rest for 125 seconds."
            "Comet can fly 21 km/s for 6 seconds, but then must rest for 121 seconds."
            "Blitzen can fly 18 km/s for 3 seconds, but then must rest for 50 seconds."
            "Vixen can fly 20 km/s for 4 seconds, but then must rest for 75 seconds."
            "Dancer can fly 7 km/s for 20 seconds, but then must rest for 119 seconds."])

(defn parse [s] (->> (re-find #"(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds." s)
                     rest
                     (#(let [[a b c d] %] (vector (keyword a) (read-string b) (read-string c) (read-string d))))))

(def second-seq #(fn [n] (take n (cycle (concat (repeat %2 %1) (repeat %3 0))))))

(defn build-map [ctx [p f s r]]
  (-> ctx
      (assoc-in [p] [f s r])
      (assoc-in [:seqs p] (second-seq f s r))))

(comment
  ((apply second-seq [8 4 50]) 10)
  (->> (map parse input)
       (reduce build-map {})
       (map (fn [[k v]] [k (reduce + ((apply second-seq v) 2503))]))
       (sort-by second)
       last))

(defn score [input n]
  (let [ctx (->> (map parse input)
                 (reduce build-map {}))
        seqs (:seqs ctx)]
    (loop [t 1
           ds {}
           ps {}]
      (if (> t n)
        {:ds ds :ps ps}
        (let [ds' (reduce-kv (fn [res k v]
                               (update res k #(+ (or % 0) (last (v t)))))
                             ds
                             seqs)
              mx  (apply max (vals ds'))
              ps' (reduce-kv (fn [res k v]
                               (update res k #(+ (or % 0) (if (= v mx) 1 0))))
                             ps
                             ds')]
          (recur (inc t) ds' ps'))))))

(defn -main [& _]
  (println "part 1:"
           (->> (map parse input)
                (reduce build-map {})
                (map (fn [[k v]] [k (reduce + ((apply second-seq v) 2503))]))
                (sort-by second)
                last))

  (println "part 2:"
           (->> (score input 2503)
                :ps
                (sort-by (fn [[_ v]] v))
                last)))

